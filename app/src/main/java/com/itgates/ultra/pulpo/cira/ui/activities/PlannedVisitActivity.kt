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
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.ultra.pulpo.cira.ui.activities.ActualActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.streams.toList

@AndroidEntryPoint
class PlannedVisitActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    // ###########################################################################################
    var relationalPlannedVisit: List<RelationalPlannedVisit> = listOf()
    var relationalPlannedVisitToShow: List<RelationalPlannedVisit> = listOf()
    var relationalPlannedOfficeWork: List<RelationalPlannedOfficeWork> = listOf()
    var relationalPlannedOfficeWorkToShow: List<RelationalPlannedOfficeWork> = listOf()
    // ###########################################################################################
    val isRoomDataFetchedToRefresh = MutableStateFlow(0)
    val isTodayList = PassedValues.plannedActivity_isToday

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
                        if (isTodayList) {
                            PlannedNavigation(activity = this@PlannedVisitActivity)
                        }
                        else {
                            PlannedVisitUI(activity = this@PlannedVisitActivity)
                        }
                    }
                }
            }
        }
        setObservers()
        loadRelationalPlannedVisitData()
        loadRelationalPlannedOfficeWorkData()

    }

    private fun setObservers() {
        cacheViewModel.relationalPlannedVisitData.observe(this@PlannedVisitActivity) {
            println(it)
            val filteredList = it.filter { relationalPlannedVisit ->
                relationalPlannedVisit.accName != null && relationalPlannedVisit.docName != null
            }
            relationalPlannedVisit = filteredList
            relationalPlannedVisitToShow = filteredList
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.relationalPlannedOfficeWorkData.observe(this@PlannedVisitActivity) {
            println(it)
            relationalPlannedOfficeWork = it
            relationalPlannedOfficeWorkToShow = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
    }

    private fun loadRelationalPlannedVisitData() {
        cacheViewModel.loadRelationalPlannedVisits(isTodayList)

    }

    private fun loadRelationalPlannedOfficeWorkData() {
        cacheViewModel.loadRelationalPlannedOfficeWorks(isTodayList)

    }

    fun convertToActualActivity(plannedVisit: RelationalPlannedVisit) {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
        }
        else {

            if (Utilities.isAutomaticTimeEnabled(applicationContext)) {
                PassedValues.actualActivity_isPlannedVisit = true
                PassedValues.actualActivity_PlannedVisitObj = plannedVisit
                PassedValues.actualActivity_startDate = Date()
                PassedValues.actualActivity_startLocation = locationInfo.location
                startActivity(Intent(this@PlannedVisitActivity, ActualActivity::class.java))
            }
            else {
                Utilities.createCustomToast(
                    applicationContext,
                    "Enable the automatic time option"
                )
            }
        }
    }

    fun convertToActualActivity(plannedOfficeWork: RelationalPlannedOfficeWork) {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
        }
        else {
            if (Utilities.isAutomaticTimeEnabled(applicationContext)) {
                PassedValues.officeWorkActivity_isPlannedOfficeWork = true
                PassedValues.officeWorkActivity_PlannedOfficeWorkObj = plannedOfficeWork
                PassedValues.officeWorkActivity_startDate = Date()
                PassedValues.officeWorkActivity_startLocation = locationInfo.location
                startActivity(Intent(this@PlannedVisitActivity, OfficeWorkActivity::class.java))
            }
            else {
                Utilities.createCustomToast(
                    applicationContext,
                    "Enable the automatic time option"
                )
            }
        }
    }

    fun applyFilters(dateFrom: LocalDate, dateTo: LocalDate) {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        relationalPlannedVisitToShow = relationalPlannedVisit.stream().filter {
            val visitDate = LocalDate.parse(it.plannedVisit.visitDate, dateFormatter)
            !visitDate.isBefore(dateFrom) && !visitDate.isAfter(dateTo)
        }.toList()

        relationalPlannedOfficeWorkToShow = relationalPlannedOfficeWork.stream().filter {
            val visitDate = LocalDate.parse(it.plannedVisit.visitDate, dateFormatter)
            !visitDate.isBefore(dateFrom) && !visitDate.isAfter(dateTo)
        }.toList()
    }
}