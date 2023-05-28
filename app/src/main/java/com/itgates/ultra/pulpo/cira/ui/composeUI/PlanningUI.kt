package com.itgates.ultra.pulpo.cira.ui.composeUI

import android.content.Intent
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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.AccountType
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Division
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import java.util.ArrayList
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Class
import com.itgates.ultra.pulpo.cira.ui.activities.MapActivity
import com.itgates.ultra.pulpo.cira.ui.activities.PlanningActivity
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                                            (activity.currentValues.accTypeCurrentValue as AccountType).table
                                        )
                                        isDataChangedToRefresh.value = !isFilterExpanded.value
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

                activity.currentValues.getDoctorListsMap().forEach { (crm_table, list) ->
                    if (list.isNotEmpty()) {
                        val accountType = activity.currentValues.allAccountTypesList.find {
                            it.table == crm_table
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
                                    val doctorAccount = activity.currentValues.getDoctorAccount(item)
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
                                                        "${doctorAccount?.account?.embedded?.name}: " ?: "null account: "
                                                    )
                                                }
                                                withStyle(
                                                    style = SpanStyle(
                                                        if (isSelected.value) ITGatesWhiteColor else ITGatesSecondaryColor
                                                    )
                                                ) {
                                                    append("(${accountType?.embedded?.name ?: "null account type"})")
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
            modifier = Modifier.fillMaxWidth().padding(bottom = padding_8),
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
        Box(modifier = Modifier.weight(2F)) {
            TextFactory(text = text)
        }
        Box(modifier = Modifier.weight(2F)) {
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
                                0, Division(-1L, EmbeddedEntity("All"), "", "",
                                "", "", "", "", "")
                            )
                            is Brick -> dataArrayList.add(
                                0, Brick(-1L, EmbeddedEntity("All"), "", "")
                            )
                            is AccountType -> dataArrayList.add(
                                0, AccountType(-1L, EmbeddedEntity("All"), "crm_All",
                                    "", "", 0)
                            )
                            is Class -> dataArrayList.add(
                                0, Class(-1L, EmbeddedEntity("All"), 0)
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
                items(activity.currentValues.selectedDoctors) { item ->
                    Card(
                        shape = ITGatesCardCornerShape,
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = padding_16
                    ) {
                        Box(
                            modifier = Modifier.padding(padding_8),
                            contentAlignment = Alignment.Center
                        ) {
                            TextFactory(
                                text = item.doctor.embedded.name,
                                color = ITGatesPrimaryColor
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(bottom = padding_8),
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
