package com.itgates.ultra.pulpo.cira.ui.composeUI

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.ultra.pulpo.cira.ui.activities.ActualActivity
import com.itgates.ultra.pulpo.cira.ui.composeUI.DoctorWithNumAndMultiplicity
import com.itgates.ultra.pulpo.cira.ui.composeUI.DoctorWithoutNumAndMultiplicity
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.ui.utils.ActualCurrentValues
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import java.util.*

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ActualVisitUI(activity: ActualActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    if (activity.isPlanned) {
                        PlannedVisitDetailsScreen(activity, isDataChangedToRefresh)
                    }else {
                        VisitDetailsScreen(activity, isDataChangedToRefresh)
                    }
                }
            }
        }
    }
}

@Composable
fun VisitDetailsScreen(
    activity: ActualActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val dateCurrentValue = remember {
        mutableStateOf(GlobalFormats.getDashedDate(Locale.getDefault(), PassedValues.actualActivity_startDate!!))
    }
    val timeCurrentValue = remember {
        mutableStateOf(GlobalFormats.getPartialTime(Locale.getDefault(), PassedValues.actualActivity_startDate!!))
    }

    val divisionCurrentValue = remember { mutableStateOf(activity.currentValues.divisionCurrentValue) }
    val brickCurrentValue = remember { mutableStateOf(activity.currentValues.brickCurrentValue) }
    val accTypeCurrentValue = remember { mutableStateOf(activity.currentValues.accTypeCurrentValue) }
    val accountCurrentValue = remember { mutableStateOf(activity.currentValues.accountCurrentValue) }
    val doctorCurrentValue = remember { mutableStateOf(activity.currentValues.doctorCurrentValue) }
    val noOfDoctorCurrentValue = remember { mutableStateOf(activity.currentValues.noOfDoctorCurrentValue) }
    val multiplicityCurrentValue = remember { mutableStateOf(activity.currentValues.multiplicityCurrentValue) }

    divisionCurrentValue.value = activity.currentValues.divisionCurrentValue
    brickCurrentValue.value = activity.currentValues.brickCurrentValue
    accTypeCurrentValue.value = activity.currentValues.accTypeCurrentValue
    accountCurrentValue.value = activity.currentValues.accountCurrentValue
    doctorCurrentValue.value = activity.currentValues.doctorCurrentValue
    noOfDoctorCurrentValue.value = activity.currentValues.noOfDoctorCurrentValue
    multiplicityCurrentValue.value = activity.currentValues.multiplicityCurrentValue

    Box(modifier = Modifier
        .padding(horizontal = padding_16)
        .padding(top = padding_8)
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(padding_4)
        ) {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    DateAndTimeComposeView(dateCurrentValue, R.drawable.calendar_icon)
                }
                Box(modifier = Modifier.weight(1F)) {
                    DateAndTimeComposeView(timeCurrentValue, R.drawable.clock_icon)
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    SelectableDropDownMenu(
                        activity,
                        activity.currentValues.divisionsList,
                        divisionCurrentValue,
                        isDataChangedToRefresh,
                        activity.currentValues.divisionErrorValue
                    )
                }

                if (activity.currentValues.isBrickVisible()) {
                    Box(modifier = Modifier.weight(1F)) {
                        SelectableDropDownMenu(
                            activity,
                            activity.currentValues.bricksList,
                            brickCurrentValue,
                            isDataChangedToRefresh,
                            activity.currentValues.brickErrorValue,
                            typeFlag = "BRICK"
                        )
                    }
                }
                else {
                    Box(modifier = Modifier.weight(1F)) {
                        DisabledDropDownMenu(ActualCurrentValues.brickStartValue.embedded.name)
                    }
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                if (activity.currentValues.isAccTypeVisible()) {
                    Box(modifier = Modifier.weight(1F)) {
                        SelectableDropDownMenu(
                            activity,
                            activity.currentValues.accountTypesList,
                            accTypeCurrentValue,
                            isDataChangedToRefresh,
                            activity.currentValues.accTypeErrorValue
                        )
                    }
                }
                else {
                    Box(modifier = Modifier.weight(1F)) {
                        DisabledDropDownMenu(ActualCurrentValues.accTypeStartValue.embedded.name)
                    }
                }

                if (activity.currentValues.isAccountVisible()) {
                    Box(modifier = Modifier.weight(1F)) {
                        SelectableDropDownMenu(
                            activity,
                            activity.currentValues.accountsList,
                            accountCurrentValue,
                            isDataChangedToRefresh,
                            activity.currentValues.accountErrorValue
                        )
                    }
                }
                else {
                    Box(modifier = Modifier.weight(1F)) {
                        DisabledDropDownMenu(ActualCurrentValues.accountStartValue.embedded.name)
                    }
                }
            }

            //move this code to CurrentValues Class later
            val noOfDoctorsSetting = activity.currentValues.settingMap.containsKey(SettingEnum.FIELD_NO_OF_DOCTORS.text)
                    && activity.currentValues.settingMap[SettingEnum.FIELD_NO_OF_DOCTORS.text] == 1
            if (noOfDoctorsSetting) {
                DoctorWithNumAndMultiplicity(
                    activity = activity,
                    isDataChangedToRefresh = isDataChangedToRefresh,
                    doctorCurrentValue = doctorCurrentValue,
                    noOfDoctorCurrentValue = noOfDoctorCurrentValue,
                    multiplicityCurrentValue = multiplicityCurrentValue
                )
            }
            else {
                DoctorWithoutNumAndMultiplicity(
                    activity = activity,
                    isDataChangedToRefresh = isDataChangedToRefresh,
                    doctorCurrentValue = doctorCurrentValue,
                    multiplicityCurrentValue = multiplicityCurrentValue
                )
            }

//            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
//                if (activity.currentValues.isDoctorVisible()) {
//                    Box(modifier = Modifier.weight(1F)) {
//                        SelectableDropDownMenu(
//                            activity,
//                            activity.currentValues.doctorsList,
//                            doctorCurrentValue,
//                            isDataChangedToRefresh,
//                            activity.currentValues.doctorErrorValue
//                        )
//                    }
//                }
//                else {
//                    Box(modifier = Modifier.weight(1F)) {
//                        DisabledDropDownMenu(ActualCurrentValues.doctorStartValue.embedded.name)
//                    }
//                }
//                Box(modifier = Modifier.weight(1F)) {
//                    SelectableDropDownMenu(
//                        activity,
//                        activity.currentValues.multiplicityList,
//                        multiplicityCurrentValue,
//                        isDataChangedToRefresh,
//                        activity.currentValues.multiplicityErrorValue
//                    )
//                }
//            }

            if (activity.currentValues.isMultiplicityDouble()) {
                ManagersScreen(activity = activity)
            }
        }
    }
}

@Composable
fun PlannedVisitDetailsScreen(
    activity: ActualActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {

    val dateCurrentValue = remember {
        mutableStateOf(GlobalFormats.getDashedDate(Locale.getDefault(), PassedValues.actualActivity_startDate!!))
    }
    val timeCurrentValue = remember {
        mutableStateOf(GlobalFormats.getPartialTime(Locale.getDefault(), PassedValues.actualActivity_startDate!!))
    }
    val noOfDoctorCurrentValue = remember { mutableStateOf(activity.currentValues.noOfDoctorCurrentValue) }

    val multiplicityCurrentValue = remember { mutableStateOf(activity.currentValues.multiplicityCurrentValue) }
    multiplicityCurrentValue.value = activity.currentValues.multiplicityCurrentValue
    noOfDoctorCurrentValue.value = activity.currentValues.noOfDoctorCurrentValue

    Box(

        modifier = Modifier
            .padding(horizontal = padding_16)
            .padding(top = padding_8)
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(padding_4)
        ) {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    DateAndTimeComposeView(dateCurrentValue, R.drawable.calendar_icon)
                }
                Box(modifier = Modifier.weight(1F)) {
                    DateAndTimeComposeView(timeCurrentValue, R.drawable.clock_icon)
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    PlannedSelectedDropDownMenu(
                        activity.currentValues.divisionCurrentValue.embedded.name
                    )
                }
                Box(modifier = Modifier.weight(1F)) {
                    PlannedSelectedDropDownMenu(
                        activity.currentValues.brickCurrentValue.embedded.name
                    )
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    PlannedSelectedDropDownMenu(
                        activity.currentValues.accTypeCurrentValue.embedded.name
                    )
                }
                Box(modifier = Modifier.weight(1F)) {
                    PlannedSelectedDropDownMenu(
                        activity.currentValues.accountCurrentValue.embedded.name
                    )
                }
            }

            //move this code to CurrentValues Class later
            val noOfDoctorsSetting = activity.currentValues.settingMap.containsKey(SettingEnum.FIELD_NO_OF_DOCTORS.text)
                    && activity.currentValues.settingMap[SettingEnum.FIELD_NO_OF_DOCTORS.text] == 1
            if (noOfDoctorsSetting) {
                PlannedDoctorWithNumAndMultiplicity(
                    activity = activity,
                    isDataChangedToRefresh = isDataChangedToRefresh,
                    noOfDoctorCurrentValue = noOfDoctorCurrentValue,
                    multiplicityCurrentValue = multiplicityCurrentValue
                )
            }
            else {
                PlannedDoctorWithoutNumAndMultiplicity(
                    activity = activity,
                    isDataChangedToRefresh = isDataChangedToRefresh,
                    multiplicityCurrentValue = multiplicityCurrentValue
                )
            }

//            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
//                Box(modifier = Modifier.weight(1F)) {
//                    PlannedSelectedDropDownMenu(
//                        activity.currentValues.doctorCurrentValue.embedded.name
//                    )
//                }
//                Box(modifier = Modifier.weight(1F)) {
//                    SelectableDropDownMenu(
//                        activity,
//                        activity.currentValues.multiplicityList,
//                        multiplicityCurrentValue,
//                        isDataChangedToRefresh,
//                        activity.currentValues.multiplicityErrorValue
//                    )
//                }
//            }

            if (activity.currentValues.isMultiplicityDouble()) {
                ManagersScreen(activity = activity)
            }
        }
    }
}

@Composable
fun DisabledDropDownMenu(text: String) {
    Box(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .border(padding_2, ITGatesSecondaryColor, ITGatesCircularCornerShape)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(horizontal = padding_12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_2)
            ) {
                Box(modifier = Modifier.weight(1F)) { SelectableDropDownMenuItem(text, ITGatesSecondaryColor) }
                Icon(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.arrow_drop_down),
                    contentDescription = "Dropdown Menu Icon",
                    tint = ITGatesSecondaryColor
                )
            }
        }
    }
}

@Composable
fun PlannedSelectedDropDownMenu(text: String) {
    Box(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .background(ITGatesPrimaryColor)
            .border(padding_2, ITGatesPrimaryColor, ITGatesCircularCornerShape)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = padding_12),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TextFactory(
                    text = text,
                    color = ITGatesWhiteColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = padding_12)
                )
            }
        }
    }
}

@Composable
fun DateAndTimeComposeView(currentValue: MutableState<String>, @DrawableRes resId: Int) {
    Box(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .border(padding_2, ITGatesPrimaryColor, ITGatesCircularCornerShape)
            // .clickable { /** handle click */ }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(horizontal = padding_12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_2)
            ) {
                Icon(
                    modifier = Modifier.size(padding_24),
                    painter = painterResource(resId),
                    contentDescription = "Dropdown Menu Icon",
                    tint = ITGatesPrimaryColor
                )
                Box(modifier = Modifier.weight(1F)) { SelectableDropDownMenuItem(currentValue.value) }
            }
        }
    }
}

@Composable
fun SelectableDropDownMenu(
    activity: ActualActivity,
    data: List<IdAndNameObj>,
    currentValue: MutableState<IdAndNameObj>,
    isDataChangedToRefresh: MutableState<Boolean>,
    isHasError: Boolean,
    listIndex: Int = -1,
    typeFlag: String = ""
) {
    val expanded = remember { mutableStateOf(false) }
    val searchValue = remember { mutableStateOf("") }
    val menuItemColor = if (isHasError) ITGatesErrorColor else ITGatesPrimaryColor

    Box(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .border(padding_2, menuItemColor, ITGatesCircularCornerShape)
            .clickable { expanded.value = true }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(horizontal = padding_12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_2)
            ) {
                Box(modifier = Modifier.weight(1F)) {
                    SelectableDropDownMenuItem(currentValue.value.embedded.name, menuItemColor)
                }
                Icon(
                    modifier = Modifier,
                    painter = if (expanded.value)
                        painterResource(R.drawable.arrow_drop_up)
                    else
                        painterResource(R.drawable.arrow_drop_down),
                    contentDescription = "Dropdown Menu Icon",
                    tint = menuItemColor
                )
            }
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = ITGatesMenuCornerShape)) {
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    modifier = Modifier
                        .fillMaxWidth(0.7F),
                ) {
                    val dataArrayList = ArrayList(data)
                    // All Bricks
                    if (data.isNotEmpty() && data[0] is Brick || typeFlag == "BRICK") {
                        dataArrayList.add(
                            0,
                            Brick(-1L, EmbeddedEntity("All Bricks"), "", "")
                        )
                    }
                    
                    // ----------------------------------------------------
//                    if (dataArrayList.size < 20) {
//
//                    }
//                    else {
//                        val configuration = LocalConfiguration.current
//
//                        val menuHeight = configuration.screenHeightDp.times(0.7F).dp
//                        val menuWidth = configuration.screenWidthDp.times(0.7F).dp
//                        LazyColumn(
//                            modifier = Modifier
//                                .width(menuWidth)
//                                .height(menuHeight)
//                        ) {
//                            itemsIndexed(dataArrayList) {index, item ->
//
//                            }
//                        }
//                    }
                    dataArrayList.forEachIndexed { index, item ->
                        if (index == 0) {
                            Box(modifier = Modifier.padding(horizontal = padding_16, vertical = padding_8)) {
                                val requester = FocusRequester()
                                CustomOutlinedTextField(
                                    myValue = searchValue,
                                    myHint = "Search \uD83D\uDD0E",
                                    hasError = remember { mutableStateOf(false) },
                                    modifier = Modifier.focusRequester(requester)
                                )
                                LaunchedEffect(Unit) {
                                    requester.requestFocus()
                                }
                            }
                        }
                        if (searchValue.value.isEmpty() || item.embedded.name.contains(searchValue.value, true)) {
                            if (listIndex == -1 || !isObjectSelectedBefore(activity, item as IdAndNameEntity)) {
                                DropdownMenuItem(
                                    modifier = Modifier,
                                    onClick = {
                                        if (currentValue.value.id != item.id) {
                                            currentValue.value = item

                                            // notify the current data object
                                            if (listIndex == -1) {
                                                activity.currentValues.notifyChanges(item)
                                            }
                                            else {
                                                activity.currentValues.notifyListsChanges(item, listIndex)
                                            }
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
    }
}

@Composable
fun SelectableDropDownMenuItem(text: String, color: Color = ITGatesPrimaryColor) {
    Box(modifier = Modifier) {
        TextFactory(
            text = text,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = padding_12)
        )
    }
}