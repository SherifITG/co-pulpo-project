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
import com.itgates.co.pulpo.ultra.ui.composeUI.AccountReportUI
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.ui.utils.AccountCurrentValues
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import com.itgates.co.pulpo.ultra.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.streams.toList

@AndroidEntryPoint
class AccountsReportActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var currentValues = AccountCurrentValues(this@AccountsReportActivity)
    val isRoomDataFetchedToRefresh = MutableStateFlow(0)

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
                        AccountReportUI(activity = this@AccountsReportActivity)
                    }
                }
            }
        }
        setObservers()
        loadAccountAndDoctorData()
        loadDivisionData()

    }
    private fun setObservers() {
        cacheViewModel.accountReportData.observe(this@AccountsReportActivity) {
            println(it.size)
            currentValues.accountsDataList = it
            currentValues.accountsDataListToShow = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.doctorReportData.observe(this@AccountsReportActivity) {
            println(it.size)
            currentValues.doctorsDataList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.divisionData.observe(this@AccountsReportActivity) {
            currentValues.divisionsList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.brickData.observe(this@AccountsReportActivity) {
            currentValues.bricksList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.accountTypeData.observe(this@AccountsReportActivity) {
            currentValues.accountTypesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }

        cacheViewModel.classesData.observe(this@AccountsReportActivity) {
            currentValues.classesList = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
    }

    private fun loadAccountAndDoctorData() {
        cacheViewModel.loadAllAccountReportData()
        cacheViewModel.loadAllDoctorReportData()

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

    fun loadClassesData(divIds: List<Long>, brickIds: List<Long>, accTypeIds: List<Int>) {
        cacheViewModel.loadClassesData(divIds, brickIds, accTypeIds)
    }

    fun applyFilters(divId: Long, brickId: Long, classId: Long, accTypeId: Long) {
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
        if (accTypeId != -1L) {
            // todo change it.account.id to it.account.accTypeId
//            accountsFiltered = accountsFiltered.stream().filter { it.account.table == accTypeTable }.toList()
            accountsFiltered = accountsFiltered.stream().filter { it.account.accountTypeId.toLong() == accTypeId }.toList()
//            accountsFiltered = accountsFiltered.stream().filter { it.account.id == accTypeId }.toList()
        }

        currentValues.accountsDataListToShow = accountsFiltered
    }
}