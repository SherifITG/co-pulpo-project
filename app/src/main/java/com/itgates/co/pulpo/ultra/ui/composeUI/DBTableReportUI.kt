package com.itgates.co.pulpo.ultra.ui.composeUI

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.ui.activities.DBTableReportActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Utilities.getOfflineRecordMessage

@Composable
fun DBTableReportUI(activity: DBTableReportActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    when(isRoomDataFetchedToRefresh.value) {
        true, false -> {
            Column {
                DBUnitActivityScreen(activity)
                TableRowContentDialog(
                    activity.messageVisibleStateFlow,
                    activity.messageTextStateFlow,
                    activity.tableName.removeSuffix("table").replace("_", " ")
                ) {
                    activity.moreMessageVisibleStateFlow.value = true
                }
                TableRowContentDialog(
                    activity.moreMessageVisibleStateFlow,
                    activity.moreMessageTextStateFlow,
                    when (activity.tableName) {
                        TablesNames.ActualVisitTable -> "Lists Info"
                        TablesNames.OfflineLogTable -> "Message Info"
                        TablesNames.OfflineLocTable -> "Message Info"
                        TablesNames.VacationTable -> "Uris Info"
                        else -> "Info"
                    }
                )
            }
        }
    }
}

@Composable
fun DBUnitActivityScreen(activity: DBTableReportActivity) {
    Box(
        modifier = Modifier
            .padding(horizontal = padding_12)
            .padding(top = padding_4)
    ) {
        LazyColumn {
            item {
                DBRowItemUI(
                    activity.entitiesMap[activity.tableName] ?: listOf(),
                    activity.dimensMap[activity.tableName] ?: listOf()
                )
            }
            itemsIndexed(activity.data) {index, obj ->
                val background = if (index % 2 == 0) ITGatesGreyColor else ITGatesTransparentColor
                Box(modifier = Modifier.background(background)) {
                    Box(
                        modifier = Modifier
                            .clip(ITGatesCircularCornerShape)
                            .clickable {
                                activity.messageVisibleStateFlow.value = true
                                activity.messageTextStateFlow.value = obj.toString()
                                if (obj is ActualVisit)
                                    activity.moreMessageTextStateFlow.value =
                                        obj.showMultipleListsInfo()
                                if (obj is OfflineLog) {
                                    activity.moreMessageTextStateFlow.value =
                                        getOfflineRecordMessage(
                                            activity.applicationContext,
                                            obj.messageId
                                        )
                                }
                                if (obj is OfflineLoc) {
                                    activity.moreMessageTextStateFlow.value =
                                        getOfflineRecordMessage(
                                            activity.applicationContext,
                                            obj.messageId
                                        )
                                }
                                if (obj is Vacation) {
                                    activity.moreMessageTextStateFlow.value =
                                        obj.showUriListInfo()
                                }
                            }
                    ) {
                        when (obj) {
                            is IdAndNameEntity -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, obj.tableId.name),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is AccountType -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Line -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, obj.unplannedLimit.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Brick -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, obj.lineId),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Division -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, obj.lineId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Setting -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, obj.value),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is VacationType -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Account -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, "No Table", obj.divisionId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Doctor -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, "No Table", obj.accountId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is NewPlanEntity -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.accountTypeId.toString(), obj.accountId.toString(), obj.accountDoctorId.toString(), obj.onlineId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Vacation -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.vacationTypeId.toString(), obj.onlineId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is ActualVisit -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.accountTypeId.toString(), obj.accountId.toString(), obj.accountDoctorId.toString(), obj.onlineId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is PlannedVisit -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.accountTypeId.toString(), obj.accountId.toString(), obj.accountDoctorId.toString(), obj.isDone.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is OfflineLog -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.deviceName, obj.isSynced.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is OfflineLoc -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.deviceName, obj.isSynced.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Presentation -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.embedded.name, obj.productId.toString()),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            is Slide -> {
                                DBRowItemUI(
                                    listOf(obj.id.toString(), obj.presentationId.toString(), obj.slideType),
                                    activity.dimensMap[activity.tableName] ?: listOf()
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DBRowItemUI(features: List<String>, featuresWeight: List<Int>) {
    Row(
        modifier = Modifier.padding(horizontal = padding_16, vertical = padding_8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        println(features)
        println(featuresWeight)
        features.forEachIndexed { index, item ->
            Box(modifier = Modifier.weight(featuresWeight[index].toFloat())) {
                TextFactory(text = item)
            }
        }
    }
}