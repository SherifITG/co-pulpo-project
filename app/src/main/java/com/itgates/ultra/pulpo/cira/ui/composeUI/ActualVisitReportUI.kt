package com.itgates.ultra.pulpo.cira.ui.composeUI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalActualVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalOfficeWorkReport
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.ultra.pulpo.cira.ui.activities.ActualVisitReportActivity
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ActualVisitReportUI(activity: ActualVisitReportActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    ActualVisitScreen(activity,
                        activity.relationalActualVisitToShow,
                        activity.relationalOfficeWorkToShow,
                        isDataChangedToRefresh
                    )
                }
            }
        }
    }
}

@Composable
fun ActualVisitScreen(
    activity: ActualVisitReportActivity,
    ActualDataList: List<RelationalActualVisit>,
    officeWorkDataList: List<RelationalOfficeWorkReport>,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val isExpanded = remember { mutableStateOf(-1) }
    val isFilterExpanded = remember { mutableStateOf(false) }

    var pickedDateFrom by remember { mutableStateOf(LocalDate.now()) }
    var pickedDateTo by remember { mutableStateOf(LocalDate.now()) }
    val dateFromDialogState = rememberMaterialDialogState()
    val dateToDialogState = rememberMaterialDialogState()

    val filterDateFrom = remember {
        mutableStateOf(DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateFrom) )
    }
    val filterDateTo = remember {
        mutableStateOf(DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo) )
    }

    Column(
        modifier = Modifier
            .padding(top = padding_4)
            .padding(horizontal = padding_16),
        verticalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        Card(
            shape = ITGatesCardCornerShape,
            modifier = Modifier,
            elevation = padding_16
        ) {
            Box(modifier = Modifier) {
                Column() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isFilterExpanded.value = !isFilterExpanded.value }
                            .padding(horizontal = padding_16, vertical = padding_8),
                        horizontalArrangement = Arrangement.spacedBy(padding_8)
                    ) {
                        Box(modifier = Modifier.weight(1F)) {
                            TextFactory(text = "Filter")
                        }
                        Icon(
                            modifier = Modifier.size(padding_24),
                            painter =
                                if (isFilterExpanded.value) {
                                    painterResource(R.drawable.arrow_drop_up)
                                }
                                else {
                                    painterResource(R.drawable.arrow_drop_down)
                                },
                            contentDescription = "Nav Icon",
                            tint = ITGatesPrimaryColor
                        )
                    }
                    AnimatedVisibility(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = SpringSpec(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                        visible = isFilterExpanded.value
                    ) {
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = padding_8),
                            verticalArrangement = Arrangement.spacedBy(padding_8)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding_12),
                                horizontalArrangement = Arrangement.spacedBy(padding_8)
                            ) {
                                FilterDateComposeView("Date From", filterDateFrom, Modifier.weight(1F)) {
                                    dateFromDialogState.show()
                                }
                                FilterDateComposeView("Date To", filterDateTo, Modifier.weight(1F)) {
                                    dateToDialogState.show()
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding_16),
                                contentAlignment = Alignment.Center
                            ) {
                                ButtonFactory(text = "apply", withPercentage = 0.5F) {
                                    activity.applyFilters(pickedDateFrom, pickedDateTo)
                                    isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                                }
                            }
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(padding_8)
        ) {
            if (ActualDataList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextFactory(
                            if (activity.relationalActualVisit.isNotEmpty())
                                "There is no data for these filters"
                            else
                                "There is no data"
                        )
                    }
                }
            }
            else {
                itemsIndexed(ActualDataList) { index, item ->
                    Card(
                        shape = ITGatesCardCornerShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isExpanded.value == index) {
                                    isExpanded.value = -1
                                } else {
                                    isExpanded.value = index
                                }
                            },
                        elevation = padding_16
                    ) {
                        Box(modifier = Modifier.padding(padding_12)) {
                            Column() {
                                // AM & PM calculations
                                val visitTime = item.actualVisit.startTime
//                                val visitTime = "00:00:00"
                                val shownTime =
                                    when(val hours = visitTime.subSequence(0, 2).toString().toInt()) {
                                        in 1..11 -> {
                                            "$visitTime AM"
                                        }
                                        in 13..21 -> {
                                            "0${hours-12}${visitTime.subSequence(2, visitTime.length)} PM"
                                        }
                                        in 22..23 -> {
                                            "${hours-12}${visitTime.subSequence(2, visitTime.length)} PM"
                                        }
                                        12 -> {
                                            "12${visitTime.subSequence(2, visitTime.length)} PM"
                                        }
                                        24 -> {
                                            "12${visitTime.subSequence(2, visitTime.length)} AM"
                                        }
                                        0 -> {
                                            "12${visitTime.subSequence(2, visitTime.length)} AM"
                                        }
                                        else -> {
                                            visitTime
                                        }
                                    }
//                                println("is synced ${item.actualVisit.isSynced} ------------------------------")
//                                println("is id ${item.actualVisit.id} ------------------------------")
                                if (item.actualVisit.isSynced) {
                                    TextFactory(
                                        text = "online id: (${item.actualVisit.onlineId}) ✅",
                                        color = ITGatesOrangeColor
                                    )
                                }
                                TextFactory(
                                    text = "id: (${item.actualVisit.id}) - date: (${item.actualVisit.startDate} " +
                                            "$shownTime)",
                                    color = ITGatesOrangeColor
                                )
                                TextFactory(buildAnnotatedString {
                                    withStyle(style = SpanStyle(ITGatesPrimaryColor)) {
                                        append("${item.accTypeName}: ")
                                    }
                                    withStyle(style = SpanStyle(ITGatesLightOrangeColor)) {
                                        append("(${item.accName})")
                                    }
                                })
                                TextFactory(
                                    text = "doctor: (${item.docName}), ${item.actualVisit.noOfDoctors}," +
                                            " ${item.actualVisit.onlineId}",
                                    modifier = Modifier.alpha(0.65F),
                                    color = ITGatesOrangeColor
                                )
                                AnimatedVisibility(
                                    modifier = Modifier
                                        .animateContentSize(
                                            animationSpec = SpringSpec(
                                                dampingRatio = Spring.DampingRatioHighBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        ),
                                    visible = isExpanded.value == index
                                ) {
                                    Column() {
                                        TextFactory(
                                            text = "visit type: (${
                                                if (item.actualVisit.plannedVisitId == 0L) {
                                                    "Actual Visit"
                                                }
                                                else {
                                                    "Planned Visit"
                                                }
                                            })",
                                            color = ITGatesOrangeColor
                                        )
                                        TextFactory(
                                            text = "visit multiplicity: (${item.actualVisit.multiplicity.text})",
                                            color = ITGatesOrangeColor
                                        )

                                        val managers = item.actualVisit.multipleListsInfo.managers
                                        for (managerIndex in 0..managers.lastIndex) {
                                            TextFactory(
                                                text = "manager ${managerIndex + 1}: ${managers[managerIndex].managerName}",
                                                color = ITGatesOrangeColor
                                            )
                                        }

                                        val products = item.actualVisit.multipleListsInfo.products
                                        for (productIndex in 0..products.lastIndex) {
                                            TextFactory(
                                                text = "product ${productIndex + 1}: ${products[productIndex].productName}",
                                                color = ITGatesOrangeColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (officeWorkDataList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextFactory(
                            if (activity.relationalOfficeWork.isNotEmpty())
                                "There is no data for these filters"
                            else
                                "There is no data"
                        )
                    }
                }
            }
            else {
                itemsIndexed(officeWorkDataList) { index, item ->
                    Card(
                        shape = ITGatesCardCornerShape,
                        modifier = Modifier
                            .fillMaxWidth(),
                            //.clickable {},
                        elevation = padding_16
                    ) {
                        Row(
                            modifier = Modifier
                                .background(ITGatesGreyColor)
                                .padding(padding_16),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1F)) {
                                TextFactory(
                                    text = "id: (${item.actualVisit.id}) - date: (${item.actualVisit.startDate})",
                                    color = ITGatesOrangeColor
                                )
                                TextFactory(buildAnnotatedString {
                                    withStyle(style = SpanStyle(ITGatesPrimaryColor)) {
                                        append("${item.officeWorkName}: ")
                                    }
                                    withStyle(style = SpanStyle(ITGatesLightOrangeColor)) {
                                        append("(${item.actualVisit.shift.text})")
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }

        MaterialDialog(
            dialogState = dateFromDialogState,
            shape = ITGatesMenuCornerShape,
            buttons = {
                positiveButton(text = "Ok") {}
                negativeButton(text = "Cancel")
            }
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ITGatesPrimaryColor)
                        .padding(top = padding_30),
                    contentAlignment = Alignment.Center
                ) {
                    WhiteTextFactory(text = "Select Date", size = 18.sp)
                }
                datepicker(
                    initialDate = pickedDateFrom,
                    title = "",
//                    allowedDateValidator = {
//                        !it.isBefore(LocalDate.now())
//                    },
                    colors = DatePickerDefaults.colors(
                        calendarHeaderTextColor = ITGatesPrimaryColor,
                        dateActiveBackgroundColor = ITGatesPrimaryColor,
                        dateActiveTextColor = ITGatesWhiteColor,
                        dateInactiveBackgroundColor = ITGatesGreyColor,
                        dateInactiveTextColor = ITGatesPrimaryColor,
                        headerBackgroundColor = ITGatesPrimaryColor,
                        headerTextColor = ITGatesWhiteColor
                    )
                ) {
                    pickedDateFrom = it
                    filterDateFrom.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateFrom)
                    if (pickedDateFrom.isAfter(pickedDateTo)) {
                        pickedDateTo = it
                        filterDateTo.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo)
                    }
                }
            }
        }

        MaterialDialog(
            dialogState = dateToDialogState,
            shape = ITGatesMenuCornerShape,
            buttons = {
                positiveButton(text = "Ok") {}
                negativeButton(text = "Cancel")
            }
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ITGatesPrimaryColor)
                        .padding(top = padding_30),
                    contentAlignment = Alignment.Center
                ) {
                    WhiteTextFactory(text = "Select Date", size = 18.sp)
                }
                datepicker(
                    initialDate = pickedDateTo,
                    allowedDateValidator = {
                        !it.isBefore(pickedDateFrom)
                    },
                    colors = DatePickerDefaults.colors(
                        calendarHeaderTextColor = ITGatesPrimaryColor,
                        dateActiveBackgroundColor = ITGatesPrimaryColor,
                        dateActiveTextColor = ITGatesWhiteColor,
                        dateInactiveBackgroundColor = ITGatesGreyColor,
                        dateInactiveTextColor = ITGatesPrimaryColor,
                        headerBackgroundColor = ITGatesPrimaryColor,
                        headerTextColor = ITGatesWhiteColor
                    )
                ) {
                    pickedDateTo = it
                    filterDateTo.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo)
                }
            }
        }
    }
}

@Composable
fun FilterDateComposeView(
    promptText: String,
    currentValue: MutableState<String>,
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(ITGatesCircularCornerShape)
            .border(padding_2, ITGatesPrimaryColor, ITGatesCircularCornerShape)
            .clickable { onClickAction() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = padding_8, vertical = padding_4),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(padding_2)
        ) {
            Box(modifier = Modifier) {
                TextFactory(
                    text = promptText, size = 13.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Box(modifier = Modifier) { TextFactory(text = currentValue.value) }
        }
    }
}