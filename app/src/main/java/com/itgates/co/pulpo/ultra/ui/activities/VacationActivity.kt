package com.itgates.co.pulpo.ultra.ui.activities

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
import com.itgates.co.pulpo.ultra.enumerations.DataStatus
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.ui.composeUI.*
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomPathListModule
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Vacation
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.DurationEnum
import com.itgates.co.pulpo.ultra.ui.utils.VacationCurrentValues
import com.itgates.co.pulpo.ultra.utilities.*

@AndroidEntryPoint
class VacationActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = VacationCurrentValues(this@VacationActivity)
    private val dataStateFlow = MutableStateFlow(DataStatus.LOADING)
    val isRoomDataFetchedToRefresh = MutableStateFlow(false)
    private var vacation: Vacation? = null

    fun getDataStoreService() = cacheViewModel.getDataStoreService()

    @SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            DataStatus.DONE -> { VacationUI(this@VacationActivity) }
            DataStatus.REFRESH -> { VacationUI(this@VacationActivity) }
            DataStatus.ERROR -> {}
            DataStatus.NO_DATA -> {}
        }
    }

    fun saveVacation() {
        this.vacation = Vacation(
            vacationTypeId = currentValues.vacationTypeCurrentValue.id,
            durationType =
            when (currentValues.durationCurrentValue.embedded.name) {
                DurationEnum.FULL_DAY.text -> DurationEnum.FULL_DAY
                DurationEnum.HALF_DAY.text -> DurationEnum.HALF_DAY
                else -> { DurationEnum.FULL_DAY }
            },
            shift =
            when (currentValues.shiftCurrentValue.embedded.name) {
                ShiftEnum.PM_SHIFT.text -> ShiftEnum.PM_SHIFT
                ShiftEnum.AM_SHIFT.text -> ShiftEnum.AM_SHIFT
                ShiftEnum.OTHER.text -> ShiftEnum.OTHER
                else -> { ShiftEnum.OTHER }
            },
            dateFrom = currentValues.dateFrom,
            dateTo = currentValues.dateTo,
            note = currentValues.noteTextCurrentValue,
            userId = currentValues.userId,
            uriListsInfo = RoomPathListModule(
                paths = currentValues.attachmentsUris.map {
//                    it.path!!
                    PathUtil.getPath(applicationContext, it)!!
                }
            )
        )

        cacheViewModel.insertVacationValidation(applicationContext, this.vacation!!)
    }

    private fun setObservers() {
        cacheViewModel.vacationTypeData.observe(this@VacationActivity) {
            currentValues.vacationTypeList = it
            dataStateFlow.value = DataStatus.DONE
        }
        cacheViewModel.vacationStatus.observe(this@VacationActivity) {
            if (it > 0) {
                Globals.triggerVacationEndEvent()
                Utilities.navigateToMainActivity(applicationContext)
            }
            else {
                Utilities.createCustomToast(applicationContext, "You can't make this Vacation because it is overlapping with another existing one")
            }
        }
    }

    private fun loadMandatoryData() {
//        loadSettingsData()
        loadVacationTypesData()
    }

//    private fun loadSettingsData() {
//        cacheViewModel.loadActualSettings()
//    }

    private fun loadVacationTypesData() {
        cacheViewModel.loadVacationTypesData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        CurrentValuesUtility.vacationCurrentValues = currentValues
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentValues = CurrentValuesUtility.vacationCurrentValues!!
    }
}