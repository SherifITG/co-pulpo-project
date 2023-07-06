package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
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
import com.itgates.ultra.pulpo.cira.enumerations.DataStatus
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.ultra.pulpo.cira.ui.composeUI.*
import com.itgates.ultra.pulpo.cira.ui.theme.PulpoUltraTheme
import com.itgates.ultra.pulpo.cira.ui.utils.OfficeWorkCurrentValues
import com.itgates.ultra.pulpo.cira.utilities.CurrentValuesUtility
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.Globals
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.officeWorkActivity_startDate
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.officeWorkActivity_startLocation
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_PlannedVisitObj
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_isPlannedVisit
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.officeWorkActivity_PlannedOfficeWorkObj
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.officeWorkActivity_isPlannedOfficeWork
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import java.util.concurrent.TimeUnit
import com.itgates.ultra.pulpo.cira.R

@AndroidEntryPoint
class OfficeWorkActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = OfficeWorkCurrentValues(this@OfficeWorkActivity)
    private val dataStateFlow = MutableStateFlow(DataStatus.LOADING)
    val isRoomDataFetchedToRefresh = MutableStateFlow(false)
    private var actualVisit: ActualVisit? = null
    val isPlanned = officeWorkActivity_isPlannedOfficeWork
    val passedPlannedOfficeWork = officeWorkActivity_PlannedOfficeWorkObj

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
            DataStatus.DONE -> { OfficeWorkUI(this@OfficeWorkActivity) }
            DataStatus.REFRESH -> { OfficeWorkUI(this@OfficeWorkActivity) }
            DataStatus.ERROR -> {}
            DataStatus.NO_DATA -> {}
        }
    }

    fun endVisit() {
        checkEndVisitDateAndLocation()
        saveVisit()
    }

    private fun checkEndVisitDateAndLocation() {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
            Utilities.navigateToMainActivity(applicationContext)
        }
        else {
            if (Utilities.isAutomaticTimeEnabled(applicationContext)) {
                currentValues.endDate = Date()
                currentValues.endLocation = locationInfo.location
            }
            else {
                Utilities.createCustomToast(
                    applicationContext,
                    "Enable the automatic time option"
                )
                Utilities.navigateToMainActivity(applicationContext)
            }
        }
    }

    private fun saveVisit() {
        // duration calculation
        val visitDurationMillis = (currentValues.endDate!!.time - officeWorkActivity_startDate!!.time)
        var visitDurationText = String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(visitDurationMillis),
            TimeUnit.MILLISECONDS.toMinutes(visitDurationMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(visitDurationMillis)),
            TimeUnit.MILLISECONDS.toSeconds(visitDurationMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(visitDurationMillis))
        )
        visitDurationText = Utilities.convertToEnglishDigits(visitDurationText);

        this.actualVisit = ActualVisit(
            divId = -1L, /** divId = -1L : this means that the visit is office Work Visit Type */
            accountTypeId = currentValues.officeWorkCurrentValue.id.toInt(),
            itemId = 0L,
            itemDoctorId = 0L,
            noOfDoctor = 0,
            plannedVisitId =
                if (actualActivity_isPlannedVisit)
                    actualActivity_PlannedVisitObj?.plannedVisit?.id ?: -1L
                else
                    0L,
            multiplicity = MultiplicityEnum.SINGLE_VISIT, // must be null but this field is nullable
            startDate = GlobalFormats.getDashedDate(Locale.getDefault(), officeWorkActivity_startDate!!),
            startTime = GlobalFormats.getFullTime(Locale.getDefault(), officeWorkActivity_startDate!!),
            endDate = GlobalFormats.getDashedDate(Locale.getDefault(), currentValues.endDate!!),
            endTime = GlobalFormats.getFullTime(Locale.getDefault(), currentValues.endDate!!),
            shift =
                when (currentValues.shiftCurrentValue.embedded.name) {
                    ShiftEnum.PM_SHIFT.text -> ShiftEnum.PM_SHIFT
                    ShiftEnum.AM_SHIFT.text -> ShiftEnum.AM_SHIFT
                    ShiftEnum.OTHER.text -> ShiftEnum.OTHER
                    else -> { ShiftEnum.OTHER }
                },
            comments = currentValues.commentTextCurrentValue, //"NEW_APP",
            insertionDate = "",
            insertionTime = "",
            userId = currentValues.userId,
            teamId = 0L,
            llStart = officeWorkActivity_startLocation!!.latitude,
            lgStart = officeWorkActivity_startLocation!!.longitude,
            llEnd = currentValues.endLocation!!.latitude,
            lgEnd = currentValues.endLocation!!.longitude,
            vDuration = visitDurationText,
            vDeviation = officeWorkActivity_startLocation!!.distanceTo(currentValues.endLocation!!).toLong(),
            isSynced = false,
            syncDate = "",
            syncTime = "",
            addedDate = GlobalFormats.getDashedDate(Locale.getDefault(), Date()),
            multipleListsInfo = RoomMultipleListsModule(listOf(), listOf(), listOf())
        )

        cacheViewModel.insertActualVisitWithValidation(this.actualVisit!!)
    }

    private fun setObservers() {
//        cacheViewModel.settingData.observe(this@OfficeWorkActivity) {
//            currentValues.settingMap = it.associate { setting ->
//                setting.embedded.name to setting.value.toInt()
//            }
//        }
        cacheViewModel.idAndNameEntityData.observe(this@OfficeWorkActivity) {
            currentValues.officeWorkList = it
            dataStateFlow.value = DataStatus.DONE
        }
        cacheViewModel.actualVisitStatus.observe(this@OfficeWorkActivity) {
            if (it > 0) {
                Globals.triggerActualEndEvent()
//                if (isPlanned) {
//                    // mark the planned visit as done
//                    cacheViewModel.markPlannedVisitAsDone(this.actualVisit!!.plannedVisitId)
//                }
//                else {
//                    Utilities.navigateToMainActivity(applicationContext)
//                }
                Utilities.navigateToMainActivity(applicationContext)
            }
            else {
                Utilities.createCustomToast(applicationContext, "You can only make 1 Office work or activity per shift")
            }
        }
//        cacheViewModel.plannedVisitMarkedDone.observe(this@OfficeWorkActivity) {
//            if (it) {
//                PassedValues.plannedActivity_isToday = true
//                val intent = Intent(this@OfficeWorkActivity, PlannedVisitActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//                finish()
//            }
//            else {
//                Utilities.createCustomToast(applicationContext, "Error when mark this Plan as done")
//            }
//        }
    }

    private fun loadMandatoryData() {
//        loadSettingsData()
        if (!isPlanned) {
            loadOfficeWorkTypesData()
        }
    }

//    private fun loadSettingsData() {
//        cacheViewModel.loadActualSettings()
//    }

    private fun loadOfficeWorkTypesData() {
        cacheViewModel.loadOfficeWorkTypes()
    }

    private fun prepareDataForPlanned() {
        if (isPlanned) {
            currentValues.shiftCurrentValue = IdAndNameEntity(
                passedPlannedOfficeWork!!.plannedVisit.shift.toLong(),
                IdAndNameTablesNamesEnum.SHIFT,
                EmbeddedEntity(
                    when(passedPlannedOfficeWork.plannedVisit.shift) {
                        2 -> "AM"
                        1 -> "PM"
                        else -> "Unknown"
                    }
                ),
            )
            if (passedPlannedOfficeWork.officeWorkName != null) {
                currentValues.officeWorkCurrentValue = IdAndNameEntity(
                    passedPlannedOfficeWork.plannedVisit.accountTypeId,
                    IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE,
                    EmbeddedEntity(passedPlannedOfficeWork.officeWorkName),
                )
                dataStateFlow.value = DataStatus.DONE
            }
            else {
                loadOfficeWorkTypesData()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        CurrentValuesUtility.officeWorkCurrentValues = currentValues
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentValues = CurrentValuesUtility.officeWorkCurrentValues!!
    }
}