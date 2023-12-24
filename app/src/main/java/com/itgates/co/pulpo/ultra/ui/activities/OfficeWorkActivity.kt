package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
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
import com.itgates.co.pulpo.ultra.enumerations.DataStatus
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.ui.composeUI.*
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.ui.utils.OfficeWorkCurrentValues
import com.itgates.co.pulpo.ultra.utilities.PassedValues.officeWorkActivity_startDate
import com.itgates.co.pulpo.ultra.utilities.PassedValues.officeWorkActivity_PlannedOfficeWorkObj
import com.itgates.co.pulpo.ultra.utilities.PassedValues.officeWorkActivity_isPlannedOfficeWork
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.OfficeWork
import com.itgates.co.pulpo.ultra.utilities.*

@AndroidEntryPoint
class OfficeWorkActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = OfficeWorkCurrentValues(this@OfficeWorkActivity)
    private val dataStateFlow = MutableStateFlow(DataStatus.LOADING)
    val isRoomDataFetchedToRefresh = MutableStateFlow(false)
    private var officeWork: OfficeWork? = null
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
        saveOW()
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
            if (Utilities.isValidTimeProvided(applicationContext)) {
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

    private fun saveOW() {
        this.officeWork = OfficeWork(
            owTypeId = currentValues.officeWorkTypeCurrentValue.id.toInt(),
            shift =
            when (currentValues.shiftCurrentValue.embedded.name) {
                ShiftEnum.PM_SHIFT.text -> ShiftEnum.PM_SHIFT
                ShiftEnum.AM_SHIFT.text -> ShiftEnum.AM_SHIFT
                ShiftEnum.OTHER.text -> ShiftEnum.OTHER
                else -> { ShiftEnum.OTHER }
            },
            plannedOwId =
            if (isPlanned)
                passedPlannedOfficeWork?.plannedOW?.id ?: -1L
            else
                0L,
            notes = "notes",
            startDate = GlobalFormats.getDashedDate(Locale.getDefault(), officeWorkActivity_startDate!!),
            startTime = GlobalFormats.getFullTime(Locale.getDefault(), officeWorkActivity_startDate!!),
            endDate = GlobalFormats.getDashedDate(Locale.getDefault(), currentValues.endDate!!),
            endTime = GlobalFormats.getFullTime(Locale.getDefault(), currentValues.endDate!!),
            userId = currentValues.userId,
            isSynced = false,
            syncDate = "",
            syncTime = "",
        )

        cacheViewModel.insertOfficeWorkWithValidation(this.officeWork!!)
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
        cacheViewModel.officeWorkStatus.observe(this@OfficeWorkActivity) {
            if (it > 0) {
                Globals.triggerOWEndEvent()
                if (isPlanned) {
                    // mark the planned visit as done
                    cacheViewModel.markPlannedOfficeWorkAsDone(this.officeWork!!.plannedOwId)
                }
                else {
                    Utilities.navigateToMainActivity(applicationContext)
                }
            }
            else {
                if (officeWork != null && officeWork!!.shift == ShiftEnum.FULL_DAY)
                    Utilities.createCustomToast(applicationContext, "You can't make a full day Office work in this day when you made a shift office work elso in this day")
                else
                    Utilities.createCustomToast(applicationContext, "You can only make 1 Office work or activity per shift")
            }
        }
        cacheViewModel.plannedOWMarkedDone.observe(this@OfficeWorkActivity) {
            if (it) {
                PassedValues.plannedActivity_isToday = true
                val intent = Intent(this@OfficeWorkActivity, PlannedVisitActivity::class.java)
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
                passedPlannedOfficeWork!!.plannedOW.shift.toLong(),
                IdAndNameTablesNamesEnum.SHIFT,
                EmbeddedEntity(
                    when(passedPlannedOfficeWork.plannedOW.shift) {
                        ShiftEnum.AM_SHIFT.index.toInt() -> ShiftEnum.AM_SHIFT.text
                        ShiftEnum.PM_SHIFT.index.toInt()  -> ShiftEnum.PM_SHIFT.text
                        else -> "Unknown"
                    }
                ),
                -2
            )
            if (passedPlannedOfficeWork.officeWorkName != null) {
                currentValues.officeWorkTypeCurrentValue = IdAndNameEntity(
                    passedPlannedOfficeWork.plannedOW.owTypeId,
                    IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE,
                    EmbeddedEntity(passedPlannedOfficeWork.officeWorkName),
                    -2
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