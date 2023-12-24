package com.itgates.co.pulpo.ultra.ui.composeUI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.AccountType
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Brick
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Division
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.DoctorPlanningData
import com.itgates.co.pulpo.ultra.ui.activities.PlanningActivity
import com.itgates.co.pulpo.ultra.ui.utils.PlanningCurrentValues
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

@Composable
fun PlanningUI(activity: PlanningActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    PlanningScreen(activity, isDataChangedToRefresh)
                }
            }
        }
    }
}

@Composable
fun PlanningScreen(
    activity: PlanningActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val isFilterExpanded = remember { mutableStateOf(false) }

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
                            .padding(horizontal = padding_12, vertical = padding_8),
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
                            } else {
                                painterResource(R.drawable.arrow_drop_down)
                            },
                            contentDescription = "Filter Icon",
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = padding_8),
                            verticalArrangement = Arrangement.spacedBy(padding_8)
                        ) {
                            PlanningFilterItemComposeView(
                                "Division",
                                activity,
                                activity.currentValues.divisionsList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Division"))) },
                                isDataChangedToRefresh
                            )
                            PlanningFilterItemComposeView(
                                "Brick",
                                activity,
                                activity.currentValues.bricksList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Brick"))) },
                                isDataChangedToRefresh
                            )
                            PlanningFilterItemComposeView(
                                "Account Type",
                                activity,
                                activity.currentValues.accountTypesList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Acc Type"))) },
                                isDataChangedToRefresh
                            )
                            PlanningFilterItemComposeView(
                                "class",
                                activity,
                                activity.currentValues.classesList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Class"))) },
                                isDataChangedToRefresh
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding_16),
                                contentAlignment = Alignment.Center
                            ) {
                                ButtonFactory(text = "apply", withPercentage = 0.5F) {
                                    if (activity.currentValues.isAllDataReady()) {
                                        activity.applyFilters(
                                            activity.currentValues.divisionCurrentValue.id,
                                            activity.currentValues.brickCurrentValue.id,
                                            activity.currentValues.classCurrentValue.id,
                                            activity.currentValues.accTypeCurrentValue.id.toInt(),
                                            0 // for new apply filters
                                        )
                                        isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                                    }
                                    else {
                                        Utilities.createCustomToast(activity.applicationContext, "Choose All filter first")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Box(modifier = Modifier.weight(1F)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding_8)
            ) {

                activity.currentValues.getDoctorListsMap(activity.currentValues.doctorsDataList).forEach { (accTypeName, list) ->
                    if (list.isNotEmpty()) {
                        val accountType = activity.currentValues.allAccountTypesList.find {
                            it.embedded.name == accTypeName
                        }
                        item {
                            DividerHeader(headerText = "(${accountType?.embedded?.name ?: "null account type"})")
                        }
                        items(list) { item ->
                            Card(
                                shape = ITGatesCardCornerShape,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                elevation = padding_16
                            ) {
                                val isSelected = remember {
                                    mutableStateOf(activity.currentValues.selectedDoctors.contains(item))
                                }
                                isSelected.value = activity.currentValues.selectedDoctors.contains(item)

                                Row(
                                    modifier = Modifier
                                        .clickable {
                                            if (isSelected.value) {
                                                activity.currentValues.selectedDoctors.remove(item)
                                            } else {
                                                activity.currentValues.selectedDoctors.add(item)
                                            }
                                            isSelected.value = !isSelected.value
                                        }
                                        .background(
                                            if (isSelected.value) ITGatesIconGreyColor else ITGatesWhiteColor
                                        )
                                        .padding(padding_16),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1F)) {
                                        TextFactory(
                                            text = "id: (${item.doctor.id})",
                                            color = if (isSelected.value) ITGatesSecondaryColor else ITGatesPrimaryColor
                                        )
                                        TextFactory(
                                            text = item.doctor.embedded.name,
                                            color = if (isSelected.value) ITGatesSecondaryColor else ITGatesPrimaryColor
                                        )
                                        TextFactory(
                                            buildAnnotatedString {
                                                withStyle(
                                                    style = SpanStyle(
                                                        if (isSelected.value) ITGatesSecondaryColor else ITGatesPrimaryColor
                                                    )
                                                ) {
                                                    append(
                                                        "${item.accName}: "
                                                    )
                                                }
                                                withStyle(
                                                    style = SpanStyle(
                                                        if (isSelected.value) ITGatesWhiteColor else ITGatesSecondaryColor
                                                    )
                                                ) {
                                                    append("(${item.accTypeName})")
                                                }
                                            },
                                            size = 17.sp
                                        )
                                    }
                                    if (isSelected.value) {
                                        Box(
                                            modifier = Modifier
                                                .clip(ITGatesCircularCornerShape)
                                                .padding(padding_8)
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(padding_40),
                                                painter = painterResource(R.drawable.check_circle_icon),
                                                contentDescription = "Location Icon",
                                                tint = ITGatesIconGreenColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = padding_8),
            contentAlignment = Alignment.Center
        ) {
            ButtonFactory(text = "   next   ") {
                activity.currentValues.tapNavigatingFun()
            }
        }
    }
}

@Composable
fun PlanningUI(activity: PlanningActivity, shiftIndex: Int) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    Column {
                        PlanningScreen(activity, isDataChangedToRefresh, shiftIndex)
                        TableRowContentDialog(
                            activity.messageVisibleStateFlow,
                            activity.messageTextStateFlow,
                            "Doctor Coverage Info"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlanningScreen(
    activity: PlanningActivity,
    isDataChangedToRefresh: MutableState<Boolean>,
    shiftIndex: Int
) {
    val isFilterExpanded = remember { mutableStateOf(false) }

    val coverageCurrentValue = remember { mutableStateOf(activity.currentValues.coverageCurrentValue) }
    coverageCurrentValue.value = activity.currentValues.coverageCurrentValue

    Column(
        modifier = Modifier
            .padding(top = padding_4)
            .padding(horizontal = padding_16),
        verticalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        val searchValue = remember { mutableStateOf("") }
        CustomOutlinedTextField(
            myValue = searchValue,
            myHint = "Search \uD83D\uDD0E",
            hasError = remember { mutableStateOf(false) }
        ) {
            isDataChangedToRefresh.value = !isDataChangedToRefresh.value
        }
        Card(
            shape = ITGatesCardCornerShape,
            modifier = Modifier,
            elevation = padding_16
        ) {
            Box(modifier = Modifier) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isFilterExpanded.value = !isFilterExpanded.value }
                            .padding(horizontal = padding_12, vertical = padding_8),
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
                            } else {
                                painterResource(R.drawable.arrow_drop_down)
                            },
                            contentDescription = "Filter Icon",
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = padding_8),
                            verticalArrangement = Arrangement.spacedBy(padding_8)
                        ) {
                            PlanningFilterItemComposeView(
                                "Division",
                                activity,
                                activity.currentValues.divisionsList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Division"))) },
                                isDataChangedToRefresh
                            )
                            PlanningFilterItemComposeView(
                                "Brick",
                                activity,
                                activity.currentValues.bricksList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Brick"))) },
                                isDataChangedToRefresh
                            )
                            PlanningFilterItemComposeView(
                                "Account Type",
                                activity,
                                activity.currentValues.accountTypesList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Acc Type"))) },
                                isDataChangedToRefresh
                            )
                            PlanningFilterItemComposeView(
                                "class",
                                activity,
                                activity.currentValues.classesList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Class"))) },
                                isDataChangedToRefresh
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = padding_16),
                                contentAlignment = Alignment.Center
                            ) {
                                ButtonFactory(text = "apply", withPercentage = 0.5F) {
                                    if (activity.currentValues.isAllDataReady()) {
                                        activity.applyFilters(
                                            activity.currentValues.divisionCurrentValue.id,
                                            activity.currentValues.brickCurrentValue.id,
                                            activity.currentValues.classCurrentValue.id,
                                            activity.currentValues.accTypeCurrentValue.id.toInt(),
                                            shiftIndex
                                        )
                                        println("--------------------------------------------- ${isDataChangedToRefresh.value}")
                                        isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                                        println("--------------------------------------------- ${isDataChangedToRefresh.value}")
                                    }
                                    else {
                                        Utilities.createCustomToast(activity.applicationContext, "Choose All filter first")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Box {
            PlanningFilterItemComposeView(
                "Coverage Filter",
                activity,
                PlanningCurrentValues.coverageList,
                remember{ mutableStateOf(PlanningCurrentValues.coverageList[0]) },
                isDataChangedToRefresh
            )
        }
        Box(modifier = Modifier.weight(1F)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding_8)
            ) {
                val list = when(shiftIndex) {
                    1 -> activity.currentValues.getDoctorListsMap(activity.currentValues.pmDoctorsDataListToShow)
                    2 -> activity.currentValues.getDoctorListsMap(activity.currentValues.amDoctorsDataListToShow)
                    3 -> activity.currentValues.getDoctorListsMap(activity.currentValues.otherDoctorsDataListToShow)
                    else -> activity.currentValues.getDoctorListsMap(activity.currentValues.otherDoctorsDataListToShow)
                }

                var isThereIsAnyDataPresent = false
                list.forEach { (accTypeName, list) ->
                    // search filter ...
                    var filteredList = if (searchValue.value.isNotEmpty()) {
                        list.filter {
                            it.accName.contains(searchValue.value) || it.doctor.embedded.name.contains(searchValue.value)
                        }
                    }
                    else {
                        list.toList()
                    }

                    // coverage filter ...
                    if (coverageCurrentValue.value.id != 0L) {
                        filteredList = filteredList.filter {
                            it.getDoctorCoverageLeve() == coverageCurrentValue.value.id
                        }
                    }

                    if (filteredList.isNotEmpty()) {
                        isThereIsAnyDataPresent = true
                        val accountType = activity.currentValues.allAccountTypesList.find {
                            it.embedded.name == accTypeName
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(padding_8)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(padding_2)
                                        .background(ITGatesPrimaryColor)
                                )
                                TextFactory(text = "(${accountType?.embedded?.name ?: "null account type"})")
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(padding_2)
                                        .background(ITGatesPrimaryColor)
                                )
                            }
                        }
                        items(filteredList) { item ->
//                            val radius = if (item.doctor.target >= 100) 36 else if (item.doctor.target >= 10) 27 else 18
//                            val badgeSize = if (item.doctor.target >= 100) 90.0 else if (item.doctor.target >= 10) 70.0 else 50.0
                            val radius = 18
                            val badgeSize = 50.5
                            Box {
                                Box(modifier = Modifier.padding(top = (badgeSize / 2).dp)) {
                                    Card(
                                        shape = ITGatesCardCornerShape,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        elevation = padding_16
                                    ) {
                                        val isSelected = remember {
                                            mutableStateOf(activity.currentValues.selectedDoctors.contains(item))
                                        }
                                        isSelected.value = activity.currentValues.selectedDoctors.contains(item)

                                        Row(
                                            modifier = Modifier
                                                .clickable {
                                                    if (isSelected.value) {
                                                        activity.currentValues.selectedDoctors.remove(
                                                            item
                                                        )
                                                    } else {
                                                        activity.currentValues.selectedDoctors.add(
                                                            item
                                                        )
                                                    }
                                                    isSelected.value = !isSelected.value
                                                }
                                                .background(
                                                    if (isSelected.value) ITGatesIconGreyColor else ITGatesWhiteColor
                                                )
                                                .padding(padding_16),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1F)) {
                                                TextFactory(
                                                    text = "id: (${item.doctor.id})",
                                                    color = if (isSelected.value) ITGatesSecondaryColor else ITGatesPrimaryColor
                                                )
                                                TextFactory(
                                                    text = item.doctor.embedded.name,
                                                    color = if (isSelected.value) ITGatesSecondaryColor else ITGatesPrimaryColor
                                                )
                                                TextFactory(
                                                    buildAnnotatedString {
                                                        withStyle(
                                                            style = SpanStyle(
                                                                if (isSelected.value) ITGatesSecondaryColor else ITGatesPrimaryColor
                                                            )
                                                        ) {
                                                            append("${item.accName} ")
                                                        }
                                                        withStyle(
                                                            style = SpanStyle(
                                                                if (isSelected.value) ITGatesWhiteColor else ITGatesSecondaryColor
                                                            )
                                                        ) {
                                                            append("(${item.accTypeName})")
                                                        }
                                                    },
                                                    size = 17.sp
                                                )
                                            }
                                            if (isSelected.value) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(ITGatesCircularCornerShape)
                                                        .padding(padding_8)
                                                ) {
                                                    Icon(
                                                        modifier = Modifier.size(padding_40),
                                                        painter = painterResource(R.drawable.check_circle_icon),
                                                        contentDescription = "Location Icon",
                                                        tint = ITGatesIconGreenColor
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Card(
                                        shape = ITGatesCircularCornerShape,
                                        modifier = Modifier.size(badgeSize.dp),
                                        elevation = padding_16
                                    ) {
                                        val percentage = if (item.doctor.target == 0)
                                            item.coverage.toFloat()
                                        else
                                            item.coverage.toFloat() / item.doctor.target.toFloat()

                                        Box(
                                            modifier = Modifier
                                                .clip(ITGatesCircularCornerShape)
                                                .clickable {
                                                    activity.messageVisibleStateFlow.value = true
                                                    activity.messageTextStateFlow.value =
                                                        formatDoctorCoverageInfo(item)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            StatisticsCircularProgressBar(
                                                percentage = percentage,
                                                targetNumber = item.doctor.target,
                                                progressNumber = item.coverage,
                                                fontSize = 16.sp,
                                                radius = radius.dp,
                                                color = when(item.getDoctorCoverageLeve()) {
                                                    1L -> ITGatesGreenColor
                                                    2L -> ITGatesOrangeColor
                                                    3L -> ITGatesErrorColor
                                                    4L -> ITGatesDarkGreyColor
                                                    else -> ITGatesOrangeColor
                                                },
                                                secondaryColor = when(item.getDoctorCoverageLeve()) {
                                                    1L -> ITGatesVeryLightGreenColor
                                                    2L -> ITGatesVeryLightOrangeColor
                                                    3L -> ITGatesVeryLightErrorColor
                                                    4L -> ITGatesVeryLightGreyColor
                                                    else -> ITGatesVeryLightOrangeColor
                                                },
                                                strokeWidth = 5.dp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!isThereIsAnyDataPresent) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TextFactory(text = "No data to preview")
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = padding_8),
            contentAlignment = Alignment.Center
        ) {
            ButtonFactory(text = "   next   ") {
                activity.currentValues.tapNavigatingFun()
            }
        }
    }
}

@Composable
fun PlanningFilterItemComposeView(
    text: String,
    activity: PlanningActivity,
    dataList: List<IdAndNameObj>,
    currentValue: MutableState<IdAndNameObj>,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding_12),
        horizontalArrangement = Arrangement.spacedBy(padding_8),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1F)) {
            TextFactory(text = text)
        }
        Box(modifier = Modifier.weight(1F)) {
            SelectableDropDownMenu(
                activity,
                dataList,
                currentValue,
                isDataChangedToRefresh
            )
        }
    }
}

@Composable
fun SelectableDropDownMenu(
    activity: PlanningActivity,
    data: List<IdAndNameObj>,
    currentValue: MutableState<IdAndNameObj>,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val expanded = remember { mutableStateOf(false) }
    val searchValue = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .border(padding_2, ITGatesPrimaryColor, ITGatesCircularCornerShape)
            .clickable { expanded.value = true }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(horizontal = padding_20),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_2)
            ) {
                Box(modifier = Modifier.weight(1F)) { SelectableDropDownMenuItem(currentValue.value.embedded.name) }
                Icon(
                    modifier = Modifier,
                    painter = if (expanded.value)
                        painterResource(R.drawable.arrow_drop_up)
                    else
                        painterResource(R.drawable.arrow_drop_down),
                    contentDescription = "Dropdown Menu Icon",
                    tint = ITGatesPrimaryColor
                )
            }
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = ITGatesMenuCornerShape)) {
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier
                        .fillMaxWidth(0.7F),
                ) {
                    CustomOutlinedTextField(
                        myValue = searchValue,
                        myHint = "Search \uD83D\uDD0E",
                        hasError = remember { mutableStateOf(false) }
                    )
                    val dataArrayList = ArrayList(data)
                    if (data.isNotEmpty()) {
                        when(data[0]) {
                            is Division -> dataArrayList.add(
                                0, Division(-1L, EmbeddedEntity("All"), 0, 0)
                            )
                            is Brick -> dataArrayList.add(
                                0, Brick(-1L, EmbeddedEntity("All"), "", "")
                            )
                            is AccountType -> dataArrayList.add(
                                0, AccountType(-1L, EmbeddedEntity("All"), 0,0, -1)
                            )
                            is IdAndNameEntity -> dataArrayList.add(
                                0,
                                IdAndNameEntity(
                                    -1L,
                                    IdAndNameTablesNamesEnum.CLASS,
                                    EmbeddedEntity("All"),
                                    0
                                )
                            )
                        }
                    }
                    dataArrayList.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier,
                            onClick = {
                                if (currentValue.value.id != item.id) {
                                    currentValue.value = item

                                    activity.currentValues.notifyChanges(item)

                                    isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                                }
                                expanded.value = false
                            }
                        ) {
                            SelectableDropDownMenuItem(item.embedded.name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanningSaveScreenUI(activity: PlanningActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            PlanningSaveScreen(activity)
        }
    }
}

@Composable
fun PlanningSaveScreen(activity: PlanningActivity) {
    var pickedDateFrom by remember { mutableStateOf(LocalDate.now()) }
    val dateFromDialogState = rememberMaterialDialogState()

    val filterDateFrom = remember {
        mutableStateOf(DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateFrom) )
    }
    Column(
        modifier = Modifier
            .padding(horizontal = padding_16)
            .padding(top = padding_12),
        verticalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        FilterDateComposeView("Plan Date", filterDateFrom, Modifier.fillMaxWidth()) {
            dateFromDialogState.show()
        }

        Box(modifier = Modifier.weight(1F)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding_8)
            ) {
                item {
                    Spacer(modifier = Modifier.height(padding_4))
                }
                itemsIndexed(activity.currentValues.selectedDoctors) { index, item ->
                    Card(
                        shape = ITGatesCardCornerShape,
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = padding_16
                    ) {
                        val status = activity.currentValues.selectedDoctorsStatus[index]
//                        println("888888888888888888888888888888888 $status")
                        Row(
                            modifier = Modifier.padding(end = padding_8),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(padding_8)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1F)
                                    .padding(padding_8),
                                contentAlignment = Alignment.Center
                            ) {
                                TextFactory(
                                    text = "${item.accName} - ${item.doctor.embedded.name}",
                                    color = ITGatesPrimaryColor
                                )
                            }

                            if (status != null) {
                                var modifier = Modifier.clip(ITGatesCircularCornerShape)
                                modifier = if (status > 0) modifier.clickable {
                                    Utilities.createCustomToast(
                                        activity.applicationContext,
                                        "This plan is saved correctly",
                                        R.drawable.polpo5
                                    )
                                }
                                else modifier.clickable {
                                    Utilities.createCustomToast(
                                        activity.applicationContext,
                                        "This account is already planned to be visited before",
                                        R.drawable.polpo5
                                    )
                                }
                                modifier = modifier.padding(padding_4)

                                Box(modifier = modifier) {
                                    Icon(
                                        modifier = Modifier.size(padding_20),
                                        painter = painterResource(
                                            if (status > 0) R.drawable.circle_confirmed_icon
                                            else R.drawable.circle_canceled_icon

                                        ),
                                        contentDescription = "Location Icon",
                                        tint = if (status > 0) ITGatesGreenColor
                                        else ITGatesErrorColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = padding_8),
            contentAlignment = Alignment.Center
        ) {
            ButtonFactory(text = "   save plans   ") {
                activity.saveNewPlans(filterDateFrom.value)
//                activity.finish()
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
                }
            }
        }
    }
}

fun formatDoctorCoverageInfo(item: DoctorPlanningData): String {
    return "    Doctor : ${item.doctor.embedded.name}\n" +
            "    Target : ${item.doctor.target}\n" +
            "    Coverage : ${item.coverage}\n" +
            "    Coverage Status : ${
                when (item.getDoctorCoverageLeve()) {
                    1L -> "Covered"
                    2L -> "Partially Covered"
                    3L -> "Uncovered"
                    4L -> "No Coverage"
                    else -> "No Coverage"
                }
            }"
}
