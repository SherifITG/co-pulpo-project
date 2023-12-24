package com.itgates.co.pulpo.ultra.ui.composeUI

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Icon
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
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalPlannedVisit
import com.itgates.co.pulpo.ultra.ui.activities.MapActivity
import com.itgates.co.pulpo.ultra.ui.activities.PlannedVisitActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PlannedVisitUI(activity: PlannedVisitActivity, tabIndex: Int = -1) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    when (tabIndex) {
                        -1 -> {
                            PlannedVisitScreen(
                                activity,
                                activity.relationalPlannedVisitToShow,
                                activity.relationalPlannedOfficeWorkToShow,
                                isDataChangedToRefresh
                            )
                        }
                        in 1..3 -> {
                            val tabDataList = activity.relationalPlannedVisit.filter {
                                it.shiftId == tabIndex
                            }.toList()

                            PlannedVisitScreen(
                                activity,
                                tabDataList,
                                listOf(),
                                isDataChangedToRefresh,
                                true
                            )
                        }
                        else -> {
                            PlannedOfficeWorkScreen(
                                activity,
                                activity.relationalPlannedOfficeWorkToShow,
                                isDataChangedToRefresh
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlannedVisitScreen(
    activity: PlannedVisitActivity,
    plannedDataList: List<RelationalPlannedVisit>,
    officeWorkDataList: List<RelationalPlannedOfficeWork>,
    isDataChangedToRefresh: MutableState<Boolean>,
    isTodayActivity: Boolean = false
) {
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
            .padding(horizontal = padding_16)
            .padding(top = padding_12),
        verticalArrangement = Arrangement.spacedBy(padding_8)
    ) {

        if (!activity.isTodayList) {
            // filter card
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
        }

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding_8)
            ) {
                if (!isTodayActivity) {
                    item {
                        DividerHeader(headerText = "Planned Visits")
                    }
                }

                // filter done planned visits
                plannedDataList.filter { !it.plannedVisit.isDone }

                if (plannedDataList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextFactory(
                                if (activity.relationalPlannedVisit.isNotEmpty())
                                    "There is no data for these filters"
                                else
                                    "There is no data"
                            )
                        }
                    }
                }
                else {
                    itemsIndexed(plannedDataList) { index, item ->
                        val isDone = item.plannedVisit.isDone
                        val modifier = if (isDone) Modifier.background(isDoneBrush) else Modifier

                        Card(
                            shape = ITGatesCardCornerShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isTodayActivity) {
                                        if (isDone) {
                                            Utilities.createCustomToast(
                                                activity.applicationContext,
                                                "Done Before"
                                            )
                                        } else {
                                            activity.convertToActualActivity(plannedDataList[index])
                                        }
                                    } else {
                                        Utilities.createCustomToast(
                                            activity.applicationContext,
                                            "Report Only"
                                        )
                                    }
                                },
                            elevation = padding_16
                        ) {
                            Row(
                                modifier = modifier.padding(padding_16),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
//                                val hasNoLocation = item.firstLL.isEmpty() || item.firstLG.isEmpty()
                                val hasNoLocation = item.firstLL?.isEmpty() ?: true || item.firstLG?.isEmpty() ?: true

                                Column(modifier = Modifier.weight(1F)) {
                                    TextFactory(
                                        text = "id: (${item.plannedVisit.id}) - date: (${item.plannedVisit.visitDate})",
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
                                        text = "doctor: (${item.docName})",
                                        modifier = Modifier.alpha(0.65F),
                                        color = ITGatesOrangeColor
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(ITGatesCircularCornerShape)
                                        .clickable {
                                            if (hasNoLocation) {
                                                Utilities.createCustomToast(
                                                    activity.applicationContext,
                                                    "This plan has no location"
                                                )
                                            }
                                            else {
//                                                PassedValues.mapActivity_ll = item.firstLL.toDouble()
//                                                PassedValues.mapActivity_lg = item.firstLG.toDouble()
//                                                PassedValues.mapActivity_accName = item.accName
//                                                PassedValues.mapActivity_docName = item.docName
                                                // TODO 0.0 index
                                                PassedValues.mapActivity_ll = item.firstLL?.toDouble() ?: 0.0
                                                PassedValues.mapActivity_lg = item.firstLG?.toDouble() ?: 0.0
                                                PassedValues.mapActivity_accName = item.accName.toString()
                                                PassedValues.mapActivity_docName = item.docName.toString()
                                                activity.startActivity(Intent(activity, MapActivity::class.java))
                                            }
                                        }
                                        .padding(padding_8)
                                ) {
                                    if (hasNoLocation) {
                                        Icon(
                                            modifier = Modifier.size(padding_40),
                                            painter = painterResource(R.drawable.location_map_icon),
                                            contentDescription = "Location Icon",
                                            tint = ITGatesGreyColor
                                        )
                                    }
                                    else {
                                        Icon(
                                            modifier = Modifier.size(padding_40),
                                            painter = painterResource(R.drawable.location_map_icon),
                                            contentDescription = "Location Icon",
                                            tint = ITGatesPrimaryColor
                                        )
                                    }
                                }

                            }
                        }
                    }
                }
                if (!isTodayActivity) {
                    item {
                        DividerHeader(headerText = "Planned Office Work")
                    }
                    if (officeWorkDataList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                TextFactory(
                                    if (activity.relationalPlannedOfficeWork.isNotEmpty())
                                        "There is no data for these filters"
                                    else
                                        "There is no data"
                                )
                            }
                        }
                    }
                    else {
                        itemsIndexed(officeWorkDataList) { index, item ->
                            val isDone = item.plannedOW.isDone
                            val modifier = if (isDone) {
                                Modifier.background(isDoneOfficeWorkBrush)
                            }
                            else {
                                Modifier.background(ITGatesGreyColor)
                            }

                            Card(
                                shape = ITGatesCardCornerShape,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        Utilities.createCustomToast(
                                            activity.applicationContext,
                                            "Report Only"
                                        )
                                    },
                                elevation = padding_16
                            ) {
                                Row(
                                    modifier = modifier
                                        .padding(padding_16),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1F)) {
                                        TextFactory(
                                            text = "id: (${item.plannedOW.id}) - date: (${item.plannedOW.visitDate})",
                                            color = ITGatesOrangeColor
                                        )
                                        TextFactory(buildAnnotatedString {
                                            withStyle(style = SpanStyle(ITGatesPrimaryColor)) {
                                                append("${item.officeWorkName?: "Office Work Type Not Selected"}: ")
                                            }
                                            withStyle(style = SpanStyle(ITGatesLightOrangeColor)) {
                                                append("(${
                                                    when(item.plannedOW.shift) {
                                                        2 -> "AM"
                                                        1 -> "PM"
                                                        else -> "Unknown"
                                                    }
                                                })")
                                            }
                                        })
                                    }
                                }
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
fun PlannedOfficeWorkScreen(
    activity: PlannedVisitActivity,
    dataList: List<RelationalPlannedOfficeWork>,
    isDataChangedToRefresh: MutableState<Boolean>
) {
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
            .padding(horizontal = padding_16)
            .padding(top = padding_12),
        verticalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        /* Filter View
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
        * */

        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding_8)
            ) {
                if (dataList.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextFactory(
                                if (activity.relationalPlannedOfficeWork.isNotEmpty())
                                    "There is no data for these filters"
                                else
                                    "There is no data"
                            )
                        }
                    }
                }
                else {
                    itemsIndexed(dataList) { index, item ->
                        val isDone = item.plannedOW.isDone
                        val modifier = if (isDone) {
                            Modifier.background(isDoneOfficeWorkBrush)
                        }
                        else {
                            Modifier.background(ITGatesGreyColor)
                        }

                        Card(
                            shape = ITGatesCardCornerShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isDone) {
                                        Utilities.createCustomToast(
                                            activity.applicationContext,
                                            "Done Before"
                                        )
                                    } else {
                                        activity.convertToActualActivity(dataList[index])
                                    }
                                },
                            elevation = padding_16
                        ) {
                            Row(
                                modifier = modifier.padding(padding_16),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1F)) {
                                    TextFactory(
                                        text = "id: (${item.plannedOW.id}) - date: (${item.plannedOW.visitDate})",
                                        color = ITGatesOrangeColor
                                    )
                                    TextFactory(buildAnnotatedString {
                                        withStyle(style = SpanStyle(ITGatesPrimaryColor)) {
                                            append("${item.officeWorkName?: "Office Work Type Not Selected"}: ")
                                        }
                                        withStyle(style = SpanStyle(ITGatesLightOrangeColor)) {
                                            append("(${
                                                when(item.plannedOW.shift) {
                                                    2 -> "AM"
                                                    1 -> "PM"
                                                    else -> "Unknown"
                                                }
                                            })")
                                        }
                                    })
                                }
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
                    allowedDateValidator = {
                        !it.isBefore(LocalDate.now())
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