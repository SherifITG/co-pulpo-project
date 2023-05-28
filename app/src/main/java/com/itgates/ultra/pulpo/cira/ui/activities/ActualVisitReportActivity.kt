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
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalActualVisit
import com.itgates.ultra.pulpo.cira.ui.composeUI.ActualVisitReportUI
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.theme.PulpoUltraTheme
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalOfficeWorkReport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.streams.toList
import com.itgates.ultra.pulpo.cira.R

@AndroidEntryPoint
class ActualVisitReportActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    // ###########################################################################################
    var relationalActualVisit: List<RelationalActualVisit> = listOf()
    var relationalActualVisitToShow: List<RelationalActualVisit> = listOf()
    var relationalOfficeWork: List<RelationalOfficeWorkReport> = listOf()
    var relationalOfficeWorkToShow: List<RelationalOfficeWorkReport> = listOf()
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
                        ActualVisitReportUI(activity = this@ActualVisitReportActivity)
                    }
                }
            }
        }
        setObservers()
        loadRelationalActualVisitData()
        loadRelationalOfficeWorkReportData()

    }
    private fun setObservers() {
        cacheViewModel.relationalActualVisitData.observe(this@ActualVisitReportActivity) {
            println(it)
            relationalActualVisit = it
            relationalActualVisitToShow = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
        cacheViewModel.relationalOfficeWorkData.observe(this@ActualVisitReportActivity) {
            println(it)
            relationalOfficeWork = it
            relationalOfficeWorkToShow = it
            isRoomDataFetchedToRefresh.value = (isRoomDataFetchedToRefresh.value + 1) % 5
        }
    }

    private fun loadRelationalActualVisitData() {
        cacheViewModel.loadRelationalActualVisits()

    }

    private fun loadRelationalOfficeWorkReportData() {
        cacheViewModel.loadRelationalOfficeWorkReportsData()

    }

    fun applyFilters(dateFrom: LocalDate, dateTo: LocalDate) {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        relationalActualVisitToShow = relationalActualVisit.stream().filter {
            val visitDate = LocalDate.parse(it.actualVisit.startDate, dateFormatter)
            !visitDate.isBefore(dateFrom) && !visitDate.isAfter(dateTo)
        }.toList()
        relationalOfficeWorkToShow = relationalOfficeWork.stream().filter {
            val visitDate = LocalDate.parse(it.actualVisit.startDate, dateFormatter)
            !visitDate.isBefore(dateFrom) && !visitDate.isAfter(dateTo)
        }.toList()
    }
}