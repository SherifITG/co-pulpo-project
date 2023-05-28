package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.enumerations.DataStatus
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomGiveawayModule
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomManagerModule
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomProductModule
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Account
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Doctor
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.AccountType
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Division
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import com.itgates.ultra.pulpo.cira.ui.activities.actualTabs.ActualNavigation
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.composeUI.DeviationMessageDialog
import com.itgates.ultra.pulpo.cira.ui.theme.PulpoUltraTheme
import com.itgates.ultra.pulpo.cira.ui.utils.ActualCurrentValues
import com.itgates.ultra.pulpo.cira.utilities.*
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_PlannedVisitObj
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_isPlannedVisit
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_startDate
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_startLocation
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

@AndroidEntryPoint
class ActualActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = ActualCurrentValues(this@ActualActivity)
    private val dataStateFlow = MutableStateFlow(DataStatus.LOADING)
    val isRoomDataFetchedToRefresh = MutableStateFlow(false)
    private var actualVisit: ActualVisit? = null
    private var visitDeviation: Long? = null
    val isPlanned = actualActivity_isPlannedVisit
    private val passedPlannedVisit = actualActivity_PlannedVisitObj

    private val messageVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    private val messageTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    fun getDataStoreService() = cacheViewModel.getDataStoreService()

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareDataForPlanned()
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name)) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Box {
                            SwitchUI()
                            DeviationMessageDialog(
                                messageVisibleStateFlow,
                                messageTextStateFlow,
                                "Deviation Dialog"
                            ) {
                                saveVisit()
                            }
                        }
                    }
                }
            }
        }

        setObservers()
        loadMandatoryData()
    }
    @Composable
    fun SwitchUI() {
        val status = dataStateFlow.collectAsState()
        when(status.value) {
            DataStatus.LOADING -> {}
            DataStatus.DONE -> { ActualNavigation(this@ActualActivity) }
            DataStatus.REFRESH -> { ActualNavigation(this@ActualActivity) }
            DataStatus.ERROR -> {}
            DataStatus.NO_DATA -> {}
        }
    }

    fun endVisit() {
        checkEndVisitDateAndLocation()

        val account = currentValues.accountCurrentValue as Account
        val accountHasLocation = account.llFirst.trim().isNotEmpty() && account.lgFirst.trim().isNotEmpty()
        if (accountHasLocation) {
            visitDeviation = currentValues.startLocation?.distanceTo(Location(LocationManager.GPS_PROVIDER).apply {
                latitude = account.llFirst.trim().toDouble()
                longitude = account.lgFirst.trim().toDouble()
            })?.toLong()

            val distanceSettingIsAvailable = currentValues.settingMap.containsKey(
                SettingEnum.METERS_TO_ACCEPT_DEVIATION.text
            )

            if (
                distanceSettingIsAvailable && visitDeviation != null
                && visitDeviation!! > currentValues.settingMap[SettingEnum.METERS_TO_ACCEPT_DEVIATION.text]!!.toFloat()
            ) {
                showLocationDeviationDialog(visitDeviation!!.toLong())
            }
            else {
                saveVisit()
            }
        }
        else {
            if (currentValues.startLocation != null) {
                cacheViewModel.updateAccountLocation(
                    currentValues.startLocation!!.latitude.toString(),
                    currentValues.startLocation!!.longitude.toString(),
                    currentValues.accountCurrentValue.id
                )
            }

            saveVisit()
        }
    }

    private fun checkEndVisitDateAndLocation() {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            // Take an offline log *
            cacheViewModel.saveOfflineLog(Utilities.createOfflineLog(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: getString(R.string.some_error_with_location),
                0,
                0
            ))
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: getString(R.string.some_error_with_location)
            )
            Utilities.navigateToMainActivity(applicationContext)
        }
        else {
            if (Utilities.isAutomaticTimeEnabled(applicationContext)) {
                currentValues.endDate = Date()
                currentValues.endLocation = locationInfo.location
            }
            else {
                // Take an offline log *
                cacheViewModel.saveOfflineLog(Utilities.createOfflineLog(
                    applicationContext,
                    getString(R.string.enable_automatic_time_option),
                    0,
                    0
                ))
                Utilities.createCustomToast(
                    applicationContext,
                    getString(R.string.enable_automatic_time_option)
                )
                Utilities.navigateToMainActivity(applicationContext)
            }
        }
    }

    private fun showLocationDeviationDialog(deviationValue: Long) {
        messageTextStateFlow.value = getString(com.itgates.ultra.pulpo.cira.R.string.deviation_message_text, deviationValue)
        messageVisibleStateFlow.value = true
    }

    private fun saveVisit() {
        // duration calculation
        val visitDurationMillis = (currentValues.endDate!!.time - actualActivity_startDate!!.time)
        var visitDurationText = String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(visitDurationMillis),
            TimeUnit.MILLISECONDS.toMinutes(visitDurationMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(visitDurationMillis)),
            TimeUnit.MILLISECONDS.toSeconds(visitDurationMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(visitDurationMillis))
        )
        visitDurationText = Utilities.convertToEnglishDigits(visitDurationText);

        this.actualVisit = ActualVisit(
            divId = currentValues.divisionCurrentValue.id,
            accountTypeId = currentValues.accTypeCurrentValue.id.toInt(),
            itemId = currentValues.accountCurrentValue.id,
            itemDoctorId = currentValues.doctorCurrentValue.id,
            noOfDoctor = currentValues.noOfDoctorCurrentValue.id.toInt(),
            plannedVisitId =
                if (actualActivity_isPlannedVisit)
                    actualActivity_PlannedVisitObj?.plannedVisit?.id ?: -1L
                else
                    0L,
            multiplicity =
                when (currentValues.multiplicityCurrentValue.embedded.name) {
                    MultiplicityEnum.SINGLE_VISIT.text -> MultiplicityEnum.SINGLE_VISIT
                    MultiplicityEnum.DOUBLE_VISIT.text -> MultiplicityEnum.DOUBLE_VISIT
                    else -> { MultiplicityEnum.SINGLE_VISIT }
                },
            startDate = GlobalFormats.getDashedDate(Locale.getDefault(), actualActivity_startDate!!),
            startTime = GlobalFormats.getFullTime(Locale.getDefault(), actualActivity_startDate!!),
            endDate = GlobalFormats.getDashedDate(Locale.getDefault(), currentValues.endDate!!),
            endTime = GlobalFormats.getFullTime(Locale.getDefault(), currentValues.endDate!!),
            shift =
                when ((currentValues.accTypeCurrentValue as AccountType).catId) {
                    1 -> ShiftEnum.PM_SHIFT
                    2 -> ShiftEnum.AM_SHIFT
                    3 -> ShiftEnum.OTHER
                    else -> { ShiftEnum.OTHER }
                },
            comments = "", //"NEW_APP",
            insertionDate = "",
            insertionTime = "",
            userId = currentValues.userId,
            teamId = (currentValues.divisionCurrentValue as Division).teamId!!.toLong(),
            llStart = actualActivity_startLocation!!.latitude,
            lgStart = actualActivity_startLocation!!.longitude,
            llEnd = currentValues.endLocation!!.latitude,
            lgEnd = currentValues.endLocation!!.longitude,
            vDuration = visitDurationText,
            vDeviation = visitDeviation ?: 0L,
            isSynced = false,
            syncDate = "",
            syncTime = "",
            addedDate = GlobalFormats.getDashedDate(Locale.getDefault(), Date()),
            multipleListsInfo = RoomMultipleListsModule(
                currentValues.productsModuleList.stream().map {
                    RoomProductModule(it)
                }.toList(),
                currentValues.giveawaysModuleList.stream().map {
                    RoomGiveawayModule(it)
                }.toList(),
                currentValues.managersModuleList.stream().map {
                    RoomManagerModule(it)
                }.toList(),
            )
        )

        cacheViewModel.insertActualVisitWithValidation(this.actualVisit!!)
    }

    private fun setObservers() {
        cacheViewModel.settingData.observe(this@ActualActivity) {
            currentValues.settingMap = it.associate { setting ->
                setting.embedded.name to setting.value.toInt()
            }
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value

        }
        cacheViewModel.divisionData.observe(this@ActualActivity) {
            currentValues.divisionsList = it
            dataStateFlow.value = DataStatus.DONE
        }
        cacheViewModel.brickData.observe(this@ActualActivity) {
            currentValues.bricksList = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
        cacheViewModel.accountTypeData.observe(this@ActualActivity) {
            currentValues.accountTypesList = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
        cacheViewModel.accountData.observe(this@ActualActivity) {
            currentValues.accountsList = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
        cacheViewModel.doctorData.observe(this@ActualActivity) {
            currentValues.doctorsList = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
        cacheViewModel.idAndNameEntityData.observe(this@ActualActivity) {
            currentValues.multipleList = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
        cacheViewModel.presentationData.observe(this@ActualActivity) {
            currentValues.presentationList = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
        cacheViewModel.actualVisitStatus.observe(this@ActualActivity) {
            if (it > 0) {
                Globals.triggerActualEndEvent()
                if (isPlanned) {
                    // mark the planned visit as done
                    cacheViewModel.markPlannedVisitAsDone(this.actualVisit!!.plannedVisitId)
                }
                else {
                    Utilities.navigateToMainActivity(applicationContext)
                }
            }
            else {
                Utilities.createCustomToast(applicationContext, "this actual visit is done before")
            }
        }
        cacheViewModel.plannedVisitMarkedDone.observe(this@ActualActivity) {
            if (it) {
                PassedValues.plannedActivity_isToday = true
                val intent = Intent(this@ActualActivity, PlannedVisitActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            else {
                Utilities.createCustomToast(applicationContext, "Error when mark this Plan as done")
            }
        }
    }

    private fun loadMandatoryData() {
        loadSettingsData()
        loadIdAndNameEntityData()
        loadPresentationData()
        if (!isPlanned) {
            loadDivisionData()
        }
    }

    private fun loadSettingsData() {
        cacheViewModel.loadActualSettings()
    }

    private fun loadDivisionData() {
        cacheViewModel.loadActualUserDivisions()
    }

    private fun loadIdAndNameEntityData() {
        cacheViewModel.loadIdAndNameTablesByTAblesListForActualActivity()
    }

    fun loadBrickData(divId: Long) {
        cacheViewModel.loadActualBricks(listOf(divId))
    }

    fun loadAccTypeData(divId: Long, brickId: Long) {
        cacheViewModel.loadActualAccountTypes(listOf(divId), listOf(brickId))
    }

    fun loadAccountData(divId: Long, brickId: Long, table: String) {
        cacheViewModel.loadActualAccounts(divId, listOf(brickId), table)
    }

    fun loadDoctorData(accountId: Long, table: String) {
        cacheViewModel.loadActualDoctors(accountId, table)
    }

    fun loadPresentationData() {
        cacheViewModel.loadPresentations()
    }

    private fun prepareDataForPlanned() {
        if (isPlanned) {
            currentValues.divisionCurrentValue = Division(
                passedPlannedVisit!!.plannedVisit.divisionId, EmbeddedEntity(passedPlannedVisit.divName),
                passedPlannedVisit.teamId.toString(), "", "", "",
                "", "", ""
            )
            currentValues.brickCurrentValue = if (passedPlannedVisit.brickId > 0) {
                Brick(
                    passedPlannedVisit.brickId, EmbeddedEntity(passedPlannedVisit.brickName.toString()),
                    passedPlannedVisit.teamId.toString(), ""
                )
            }
            else {
                Brick(
                    passedPlannedVisit.brickId, EmbeddedEntity("All Bricks"),
                    passedPlannedVisit.teamId.toString(), ""
                )
            }
            currentValues.accTypeCurrentValue = AccountType(
                passedPlannedVisit.plannedVisit.accountTypeId, EmbeddedEntity(passedPlannedVisit.accTypeName),
                "", "", "", passedPlannedVisit.categoryId
            )
            currentValues.accountCurrentValue = Account(
                passedPlannedVisit.plannedVisit.itemId, EmbeddedEntity(passedPlannedVisit.accName),
                passedPlannedVisit.teamId, 0L, 0L,
                passedPlannedVisit.firstLL,passedPlannedVisit.firstLL,
                0L, 0L, "", "", "", ""
            )
            currentValues.doctorCurrentValue = Doctor(
                passedPlannedVisit.plannedVisit.itemDoctorId, EmbeddedEntity(passedPlannedVisit.docName),
                0L, 0L, "", "",0L,
                0L, 0L, "", "", 0, ""
            )
            dataStateFlow.value = DataStatus.DONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        CurrentValuesUtility.actualCurrentValues = currentValues
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentValues = CurrentValuesUtility.actualCurrentValues!!
    }
}