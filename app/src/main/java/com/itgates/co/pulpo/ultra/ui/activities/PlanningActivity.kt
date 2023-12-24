package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.activities.planningTabs.PlanningNavigation
import com.itgates.co.pulpo.ultra.ui.utils.PlanningCurrentValues
import com.itgates.co.pulpo.ultra.utilities.Globals
import com.itgates.co.pulpo.ultra.utilities.Utilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.streams.toList

@AndroidEntryPoint
class PlanningActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = PlanningCurrentValues(this@PlanningActivity)
    val isRoomDataFetchedToRefresh = MutableStateFlow(0)

    val messageVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    val messageTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    fun getDataStoreService() = cacheViewModel.getDataStoreService()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                        PlanningNavigation(activity = this@PlanningActivity)
                    }
                }
            }
        }
        setObservers()
        loadAccountAndDoctorData()
        loadDivisionData()
        loadAllAccTypeData()

    }
    private fun setObservers() {
        cacheViewModel.accountReportData.observe(this@PlanningActivity) {
            println("io io io ${it.size}")
            currentValues.accountsDataList = it
            currentValues.accountsDataListToShow = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.doctorPlanningData.observe(this@PlanningActivity) {
            println("io io io ${it.size}")
            it.forEach { doc ->
                println("io io io ${doc.doctor.id}-${doc.coverage}")
            }
            currentValues.doctorsDataList = it
            currentValues.distributeDoctorsList()
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.divisionData.observe(this@PlanningActivity) {
            currentValues.divisionsList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.brickData.observe(this@PlanningActivity) {
            currentValues.bricksList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.accountTypeData.observe(this@PlanningActivity) {
            currentValues.accountTypesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.allAccountTypeData.observe(this@PlanningActivity) {
            currentValues.allAccountTypesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5

            currentValues.distributeDoctorsList()
        }

        cacheViewModel.classesData.observe(this@PlanningActivity) {
            currentValues.classesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.planningMapStatus.observe(this@PlanningActivity) {
            if (it.size == currentValues.selectedDoctors.size) {
                var isPassed = true
                it.forEach { entry -> isPassed = isPassed && entry.value > 0 }
                Globals.triggerNewPlanEndEvent()
                if (isPassed) {
                    Utilities.navigateToMainActivity(applicationContext)
                    return@observe
                }
            }

            println("------------ $it")
            currentValues.selectedDoctorsStatus = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
    }

    private fun loadAccountAndDoctorData() {
        cacheViewModel.loadAllAccountReportData()
        cacheViewModel.loadAllDoctorPlanningData()

    }

    private fun loadDivisionData() {
        cacheViewModel.loadUserDivisions()
    }

    fun loadBrickData(divIds: List<Long>) {
        cacheViewModel.loadActualBricks(divIds)
    }

    fun loadAccTypeData(divIds: List<Long>, brickIds: List<Long>) {
        cacheViewModel.loadActualAccountTypes(divIds, brickIds)
    }

    private fun loadAllAccTypeData() {
        cacheViewModel.loadAllAccountTypes()
    }

    fun loadClassesData(divIds: List<Long>, brickIds: List<Long>, accTypeIds: List<Int>) {
        cacheViewModel.loadClassesData(divIds, brickIds, accTypeIds)
    }

    fun saveNewPlans(visitDate: String) {
        val newPlanList = currentValues.selectedDoctors.stream().map {
            currentValues.createNewPlanInstance(
                it.divId, it.accTypeId, it.doctor.accountId,
                it.doctor.id, 0, visitDate, 0, it.doctor.lineId
            )
        }.toList()
        cacheViewModel.saveNewPlans(newPlanList)
    }

    fun applyFilters(divId: Long, brickId: Long, classId: Long, accTypeId: Int, shift: Int) {
        var doctorsFiltered = when(shift) {
            1 -> currentValues.pmDoctorsDataList
            2 -> currentValues.amDoctorsDataList
            3 -> currentValues.otherDoctorsDataList
            else -> currentValues.otherDoctorsDataList
        }

        // division filter step
        if (divId != -1L) {
            doctorsFiltered = ArrayList(
                doctorsFiltered.stream().filter {
                    it.divId == divId
                }.toList()
            )
        }

        // brick filter step
        if (brickId != -1L) {
            doctorsFiltered = ArrayList(
                doctorsFiltered.stream().filter {
                    it.brickId == brickId
                }.toList()
            )
        }

        // class filter step
        if (classId != -1L) {
            doctorsFiltered = ArrayList(
                doctorsFiltered.stream().filter {
                    it.classId == classId
                }.toList()
            )
        }

        // account type filter step
        if (accTypeId != -1) {
            doctorsFiltered = ArrayList(
                doctorsFiltered.stream().filter {
                    it.doctor.accountTypeId == accTypeId
                }.toList()
            )
        }

        when(shift) {
            1 -> currentValues.pmDoctorsDataListToShow = doctorsFiltered
            2 -> currentValues.amDoctorsDataListToShow = doctorsFiltered
            3 -> currentValues.otherDoctorsDataListToShow = doctorsFiltered
            else -> currentValues.otherDoctorsDataListToShow = doctorsFiltered
        }
    }
}