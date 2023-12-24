package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.AccountTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.AccountTypeTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.ActualVisitTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.BrickTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.DivisionTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.DoctorTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.IdAndNameTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.LineTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.NewPlanTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.OfflineLocTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.OfflineLogTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.PlannedVisitTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.PresentationTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.SettingTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.SlideTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.VacationTable
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames.VacationTypeTable
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.composeUI.DBTableReportUI
import com.itgates.co.pulpo.ultra.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.streams.toList

@AndroidEntryPoint
class DBTableReportActivity : ComponentActivity() {
    private val cacheViewModel: CacheViewModel by viewModels()

    val messageVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    val messageTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    val moreMessageVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    val moreMessageTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    val isRoomDataFetchedToRefresh = MutableStateFlow(false)
    var tableName = ""
    var data = listOf<Any>()

    val entitiesMap = mapOf(
        Pair(IdAndNameTable, listOf("id", "name", "tableId")),
        Pair(AccountTypeTable, listOf("id", "name")),
        Pair(LineTable, listOf("id", "name", "unplannedLimit")),
        Pair(BrickTable, listOf("id", "name", "lineId")),
        Pair(DivisionTable, listOf("id", "name", "lineId")),
        Pair(SettingTable, listOf("id", "name", "value")),
        Pair(VacationTypeTable, listOf("id", "name")),
        Pair(AccountTable, listOf("id", "name", "table", "divisionId")),
        Pair(DoctorTable, listOf("id", "name", "table", "accountId")),
        Pair(NewPlanTable, listOf("id", "accountTypeId", "accountId", "accountDoctorId", "onlineId")),
        Pair(VacationTable, listOf("id", "vacationTypeId", "onlineId")),
        Pair(ActualVisitTable, listOf("id", "accountTypeId", "accountId", "accountDoctorId", "onlineId")),
        Pair(PlannedVisitTable, listOf("id", "accountTypeId", "accountId", "accountDoctorId", "isDone")),
        Pair(PresentationTable, listOf("id", "name", "productId")),
        Pair(SlideTable, listOf("id", "presentationId", "slideType")),
        Pair(OfflineLogTable, listOf("id", "deviceName", "isSynced")),
        Pair(OfflineLocTable, listOf("id", "deviceName", "isSynced"))
    )

    val dimensMap = mapOf(
        Pair(IdAndNameTable, listOf(2, 6, 2)),
        Pair(AccountTypeTable, listOf(2, 8)),
        Pair(LineTable, listOf(2, 6, 2)),
        Pair(BrickTable, listOf(2, 6, 2)),
        Pair(DivisionTable, listOf(2, 6, 2)),
        Pair(SettingTable, listOf(2, 4, 4)),
        Pair(VacationTypeTable, listOf(2, 8)),
        Pair(AccountTable, listOf(2, 3, 3, 2)),
        Pair(DoctorTable, listOf(2, 3, 3, 2)),
        Pair(NewPlanTable, listOf(2, 2, 2, 2, 2)),
        Pair(VacationTable, listOf(1, 1, 1)),
        Pair(ActualVisitTable, listOf(2, 2, 2, 2, 2)),
        Pair(PlannedVisitTable, listOf(2, 2, 2, 2, 2)),
        Pair(PresentationTable, listOf(2, 6, 2)),
        Pair(SlideTable, listOf(2, 4, 4)),
        Pair(OfflineLogTable, listOf(2, 6, 2)),
        Pair(OfflineLocTable, listOf(2, 6, 2))
    )

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadAll()
        setObservers()
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
                        DBTableReportUI(activity = this@DBTableReportActivity)
                    }
                }
            }
        }
    }

    private fun loadAll() {
        tableName = PassedValues.dbTableReportActivity_tableName
        cacheViewModel.genericLoadAll(tableName)
    }

    private fun setObservers() {
        cacheViewModel.genericData.observe(this) { stringList ->
            data = stringList
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
    }
}