package com.itgates.co.pulpo.ultra.ui.activities

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
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.enumerations.DataStatus
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomGiveawayModule
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomManagerModule
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomProductModule
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Account
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.ActualVisit
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Doctor
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.AccountType
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Brick
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Division
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import com.itgates.co.pulpo.ultra.ui.activities.actualTabs.ActualNavigation
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.composeUI.DeviationMessageWithoutSaveDialog
import com.itgates.co.pulpo.ultra.ui.composeUI.DeviationOrInfoMessageDialog
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.ui.utils.ActualCurrentValues
import com.itgates.co.pulpo.ultra.utilities.*
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_PlannedVisitObj
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_isPlannedVisit
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_startDate
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_startLocation
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
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
    val isRoomDataFetchedToRefresh = MutableStateFlow(0)
    private var actualVisit: ActualVisit? = null
    private var visitDeviation: Long? = null
    val isPlanned = actualActivity_isPlannedVisit
    val passedPlannedVisit = actualActivity_PlannedVisitObj

    private val messageVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    private val messageTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    private val messageWithoutSaveVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    private val messageWithoutSaveTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    private val infoVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    private val infoTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    fun getDataStoreService() = cacheViewModel.getDataStoreService()

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareDataForPlanned()
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name), goToHomeContext = this) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Box {
                            SwitchUI()
                            DeviationOrInfoMessageDialog(
                                messageVisibleStateFlow,
                                messageTextStateFlow,
                                "Deviation Dialog",
                                isInfo = false,
                                onSaveAction = { saveVisit() },
                                onViewMapAction = { viewMap() }
                            )
                            DeviationOrInfoMessageDialog(
                                infoVisibleStateFlow,
                                infoTextStateFlow,
                                "Disabled Info Dialog",
                                isInfo = true,
                                onSaveAction = {},
                                onViewMapAction = {}
                            )
                            DeviationMessageWithoutSaveDialog(
                                messageWithoutSaveVisibleStateFlow,
                                messageWithoutSaveTextStateFlow,
                                "Deviation Dialog"
                            )
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

            val distanceSettingIsOn = currentValues.settingMap.containsKey(
                SettingEnum.ALLOW_ACTUAL_WITH_DEVIATION.text
            ) && currentValues.settingMap[SettingEnum.ALLOW_ACTUAL_WITH_DEVIATION.text] == 1

            val acceptedDistance = (currentValues.accTypeCurrentValue as AccountType).acceptedDistance

            if (
                distanceSettingIsOn && visitDeviation != null
                && visitDeviation!! > acceptedDistance && acceptedDistance == -1
            ) {
                showLocationDeviationDialog(visitDeviation!!.toLong())
            }
            else if (
                distanceSettingIsOn && visitDeviation != null
                && visitDeviation!! > acceptedDistance && acceptedDistance != -1
            ) {
                showLocationDeviationDialogWithoutSave(visitDeviation!!.toLong())
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
                    currentValues.accountCurrentValue.id,
                    (currentValues.accountCurrentValue as Account).accountTypeId,
                    (currentValues.accountCurrentValue as Account).lineId,
                    (currentValues.accountCurrentValue as Account).divisionId,
                    (currentValues.accountCurrentValue as Account).brickId,
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
                0
            ))
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: getString(R.string.some_error_with_location)
            )
            Utilities.navigateToMainActivity(applicationContext)
        }
        else {
            if (Utilities.isValidTimeProvided(applicationContext)) {
                currentValues.endDate = Date()
                currentValues.endLocation = locationInfo.location
            }
            else {
                // Take an offline log *
                cacheViewModel.saveOfflineLog(Utilities.createOfflineLog(
                    applicationContext,
                    getString(R.string.enable_automatic_time_option),
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
        messageTextStateFlow.value = getString(R.string.deviation_message_text, deviationValue)
        messageVisibleStateFlow.value = true
    }

    private fun showLocationDeviationDialogWithoutSave(deviationValue: Long) {
        messageWithoutSaveTextStateFlow.value = getString(R.string.deviation_message_text_without_save, deviationValue)
        messageWithoutSaveVisibleStateFlow.value = true
    }

    fun showInfoDialog(divName: String, lineName: String, lineUnplannedLimit: Int) {
        infoTextStateFlow.value = getString(R.string.info_message_text, divName, lineName, lineUnplannedLimit)
        infoVisibleStateFlow.value = true
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
            brickId = currentValues.brickCurrentValue.id,
            accountTypeId = currentValues.accTypeCurrentValue.id.toInt(),
            accountId = currentValues.accountCurrentValue.id,
            accountDoctorId = currentValues.doctorCurrentValue.id,
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
                when ((currentValues.accTypeCurrentValue as AccountType).shiftId) {
                    ShiftEnum.AM_SHIFT.index.toInt() -> ShiftEnum.AM_SHIFT
                    ShiftEnum.PM_SHIFT.index.toInt() -> ShiftEnum.PM_SHIFT
                    ShiftEnum.OTHER.index.toInt() -> ShiftEnum.OTHER
                    else -> { ShiftEnum.OTHER }
                },
            userId = currentValues.userId,
            lineId = (currentValues.divisionCurrentValue as Division).lineId,
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
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.divisionData.observe(this@ActualActivity) {
            currentValues.divisionsList = it.sortedBy { division -> division.embedded.name }
            dataStateFlow.value = DataStatus.DONE
        }
        cacheViewModel.brickData.observe(this@ActualActivity) {
            currentValues.bricksList = it.sortedBy { brick -> brick.embedded.name }
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.accountTypeData.observe(this@ActualActivity) {
            currentValues.accountTypesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.accountData.observe(this@ActualActivity) {
            currentValues.accountsList = it.sortedBy { account -> account.embedded.name }
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.doctorData.observe(this@ActualActivity) {
            currentValues.doctorsList = it.sortedBy { doctor -> doctor.embedded.name }
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.idAndNameEntityData.observe(this@ActualActivity) { list ->
            currentValues.multipleList = list
            currentValues.managersList = list.filter {
                it.tableId == IdAndNameTablesNamesEnum.MANAGER
            }.filter {
                it.embedded.name != "System Administrator"
            }
            currentValues.giveawaysList = list.filter {
                it.tableId == IdAndNameTablesNamesEnum.GIVEAWAY
            }
            currentValues.productsList = list.filter {
                it.tableId == IdAndNameTablesNamesEnum.PRODUCT
            }
            currentValues.feedbacksList = list.filter {
                it.tableId == IdAndNameTablesNamesEnum.FEEDBACK
            }
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.presentationData.observe(this@ActualActivity) {
            currentValues.presentationList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.actualVisitStatus.observe(this@ActualActivity) {
            if (it > 0) {
                Utilities.createCustomToast(
                    applicationContext,
                    "Visit is saved successfully",
                    R.drawable.polpo5
                )
                Globals.triggerActualEndEvent()
                if (isPlanned) {
                    // mark the planned visit as done
                    cacheViewModel.markPlannedVisitAsDone(this.actualVisit!!.plannedVisitId)
                }
                else {
                    Utilities.navigateToMainActivity(applicationContext)
                }
            }
            else if (it == -1L) {
                Utilities.createCustomToast(applicationContext, "This actual visit is done before")
            }
            else {
                Utilities.createCustomToast(applicationContext, "Error when saving this actual visit")
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
        loadPresentationData()
        if (!isPlanned) {
            loadDivisionData()
        }
    }

    private fun loadSettingsData() {
        cacheViewModel.loadActualSettings()
    }

    private fun loadDivisionData() {
        cacheViewModel.loadUserDivisions()
    }

    fun loadIdAndNameEntityData(lineId: Long) {
        cacheViewModel.loadIdAndNameTablesByTAblesListForActualActivity(lineId)
    }

    fun loadBrickData(divId: Long) {
        cacheViewModel.loadActualBricks(listOf(divId))
    }

    fun loadAccTypeData(divId: Long, brickId: Long) {
        cacheViewModel.loadActualAccountTypes(listOf(divId), listOf(brickId))
    }

    fun loadAccountData(lineId: Long, divId: Long, brickId: Long, accTypeId: Int) {
        cacheViewModel.loadActualAccounts(lineId, divId, brickId, accTypeId)
    }

    fun loadDoctorData(lineId: Long, accountId: Long, accTypeId: Int) {
        cacheViewModel.loadActualDoctors(lineId, accountId, accTypeId)
    }

    fun loadPresentationData() {
        cacheViewModel.loadPresentations()
    }

    private fun prepareDataForPlanned() {
        if (isPlanned) {
            currentValues.divisionsList = listOf(
                Division(
                    passedPlannedVisit!!.plannedVisit.divisionId, EmbeddedEntity(passedPlannedVisit.divName),
                    passedPlannedVisit.lineId, passedPlannedVisit.lineId
                )
            )
            currentValues.divisionCurrentValue = currentValues.divisionsList.first()
            currentValues.brickCurrentValue = if (passedPlannedVisit.brickId > 0) {
                Brick(
                    passedPlannedVisit.brickId, EmbeddedEntity(passedPlannedVisit.brickName.toString()),
                    passedPlannedVisit.lineId.toString(), ""
                )
            }
            else {
                Brick(
                    passedPlannedVisit.brickId, EmbeddedEntity("All Bricks"),
                    passedPlannedVisit.lineId.toString(), ""
                )
            }
            currentValues.accTypeCurrentValue = AccountType(
                passedPlannedVisit.plannedVisit.accountTypeId, EmbeddedEntity(passedPlannedVisit.accTypeName),
                0, passedPlannedVisit.shiftId, passedPlannedVisit.accTypeDistance
            )
            currentValues.accountCurrentValue = Account(
                passedPlannedVisit.plannedVisit.accountId, EmbeddedEntity(passedPlannedVisit.accName.toString()),
                passedPlannedVisit.lineId, 0L, 0L,
                0L, 0, "", "", "", "",
                passedPlannedVisit.firstLL ?: "", passedPlannedVisit.firstLG ?: ""
            )
            if (passedPlannedVisit.plannedVisit.accountDoctorId != null && passedPlannedVisit.docName != null) {
                currentValues.doctorCurrentValue = Doctor(
                    passedPlannedVisit.plannedVisit.accountDoctorId, EmbeddedEntity(passedPlannedVisit.docName),
                    passedPlannedVisit.lineId, 0L, 0, "","",
                    0L, 0L, "", 0
                )
            }
            else {
                loadDoctorData(
                    (currentValues.divisionCurrentValue as Division).lineId,
                    currentValues.accountCurrentValue.id,
                    currentValues.accTypeCurrentValue.id.toInt()
                )
            }
            dataStateFlow.value = DataStatus.DONE
            loadIdAndNameEntityData(passedPlannedVisit.lineId)
        }
    }

    fun viewMap() {
        if (currentValues.isAccountSelected()) {
            val account =
                currentValues.accountCurrentValue as Account
            val doctor = currentValues.doctorCurrentValue
            if (account.llFirst.isNotEmpty() && account.lgFirst.isNotEmpty()) {
                PassedValues.mapActivity_ll =
                    account.llFirst.toDouble()
                PassedValues.mapActivity_lg =
                    account.lgFirst.toDouble()
                PassedValues.mapActivity_accName =
                    account.embedded.name
                if (doctor.embedded.name != "Select Doctor")
                    PassedValues.mapActivity_docName =
                        doctor.embedded.name
                else
                    PassedValues.mapActivity_docName =
                        "NO_DOCTOR_SELECTED"

            } else {
                PassedValues.mapActivity_ll = 0.0
                PassedValues.mapActivity_lg = 0.0
                PassedValues.mapActivity_accName =
                    "NO_ACCOUNT_SELECTED"
                PassedValues.mapActivity_docName =
                    "NO_DOCTOR_SELECTED"
            }
        } else {
            PassedValues.mapActivity_ll = 0.0
            PassedValues.mapActivity_lg = 0.0
            PassedValues.mapActivity_accName = "NO_ACCOUNT_SELECTED"
            PassedValues.mapActivity_docName = "NO_DOCTOR_SELECTED"
        }

        startActivity(Intent(this@ActualActivity, MyLocationCustomMapActivity::class.java))
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