package com.itgates.ultra.pulpo.cira.ui.activities

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
import com.itgates.ultra.pulpo.cira.ui.composeUI.PlanningUI
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.theme.PulpoUltraTheme
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.ui.activities.planningTabs.PlanningNavigation
import com.itgates.ultra.pulpo.cira.ui.utils.PlanningCurrentValues
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.streams.toList

@AndroidEntryPoint
class PlanningActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = PlanningCurrentValues(this@PlanningActivity)
    val isRoomDataFetchedToRefresh = MutableStateFlow(0)

    fun getDataStoreService() = cacheViewModel.getDataStoreService()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            println(it.size)
            currentValues.accountsDataList = it
            currentValues.accountsDataListToShow = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.doctorReportData.observe(this@PlanningActivity) {
            println(it.size)
            currentValues.doctorsDataList = it
            currentValues.doctorsDataListToShow = it
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
        }

        cacheViewModel.classesData.observe(this@PlanningActivity) {
            currentValues.classesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
    }

    private fun loadAccountAndDoctorData() {
        cacheViewModel.loadAllAccountReportData()
        cacheViewModel.loadAllDoctorReportData()

    }

    private fun loadDivisionData() {
        cacheViewModel.loadActualUserDivisions()
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

    fun loadClassesData(divIds: List<Long>, brickIds: List<Long>, accTypeTables: List<String>) {
        cacheViewModel.loadClassesData(divIds, brickIds, accTypeTables)
    }

    fun saveNewPlans(visitDate: String) {
        val newPlanList = currentValues.selectedDoctors.stream().map {
            val doctorAccount = currentValues.getDoctorAccount(it)
            val doctorAccountType = currentValues.getDoctorAccountType(it)
            currentValues.createNewPlanInstance(
                doctorAccount!!.account.divisionId, doctorAccountType!!.id, it.doctor.accountId,
                it.doctor.id, 0, visitDate, 0, doctorAccount.account.teamId
            )
        }.toList()
        cacheViewModel.saveNewPlans(newPlanList)
    }

    fun applyFilters(divId: Long, brickId: Long, classId: Long, accTypeTable: String) {
        println(" 99999999999999999999999999999999 $divId 999 $brickId 999 $classId 999 $accTypeTable")
        var accountsFiltered = currentValues.accountsDataList

        // division filter step
        if (divId != -1L) {
            accountsFiltered = accountsFiltered.stream().filter { it.account.divisionId == divId }.toList()
        }

        // brick filter step
        if (brickId != -1L) {
            accountsFiltered = accountsFiltered.stream().filter { it.account.brickId == brickId }.toList()
        }

        // class filter step
        if (classId != -1L) {
            accountsFiltered = accountsFiltered.stream().filter { it.account.classId == classId }.toList()
        }

        // account type filter step
        if (accTypeTable != "crm_All") {
            accountsFiltered = accountsFiltered.stream().filter { it.account.table == accTypeTable }.toList()
        }

        currentValues.accountsDataListToShow = accountsFiltered
    }
}