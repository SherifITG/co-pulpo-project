package com.itgates.ultra.pulpo.cira.ui.activities

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
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalPlannedVisit
import com.itgates.ultra.pulpo.cira.ui.activities.plannedTabs.PlannedNavigation
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.composeUI.PlannedVisitUI
import com.itgates.ultra.pulpo.cira.ui.theme.PulpoUltraTheme
import com.itgates.ultra.pulpo.cira.utilities.Globals
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalNewPlan
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.ultra.pulpo.cira.ui.composeUI.NewPlanUI
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
                    topBar = { AppBarComposeView(text = getString(R.string.app_name)) }
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