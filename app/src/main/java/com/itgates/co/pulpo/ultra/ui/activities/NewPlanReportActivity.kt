package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalPlannedVisit
import com.itgates.co.pulpo.ultra.ui.activities.plannedTabs.PlannedNavigation
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.composeUI.PlannedVisitUI
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.utilities.Globals
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalNewPlan
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.co.pulpo.ultra.ui.composeUI.NewPlanUI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.streams.toList

@AndroidEntryPoint
class NewPlanReportActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    // ###########################################################################################
    var relationalNewPlans: List<RelationalNewPlan> = listOf()
//    var relationalPlannedOfficeWorks: List<RelationalPlannedOfficeWork> = listOf()
    // ###########################################################################################
    val isRoomDataFetchedToRefresh = MutableStateFlow(0)

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
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        NewPlanUI(activity = this@NewPlanReportActivity)
                    }
                }
            }
        }
        setObservers()
        loadRelationalPlannedVisitData()
        loadRelationalPlannedOfficeWorkData()

    }

    private fun setObservers() {
        cacheViewModel.relationalNewPlanData.observe(this@NewPlanReportActivity) {
            println(it)
            relationalNewPlans = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
//        cacheViewModel.relationalPlannedOfficeWorkData.observe(this@NewPlanReportActivity) {
//            println(it)
//            relationalPlannedOfficeWork = it
//            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
//        }
    }

    private fun loadRelationalPlannedVisitData() {
        cacheViewModel.loadRelationalNewPlans()

    }

    private fun loadRelationalPlannedOfficeWorkData() {
//        cacheViewModel.loadRelationalPlannedOfficeWorks(isTodayList)
    }
}