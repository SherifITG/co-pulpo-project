package com.itgates.co.pulpo.ultra.ui.composeUI

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.ui.activities.OfficeWorkActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun OfficeWorkUI(activity: OfficeWorkActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        true, false -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    Scaffold(
                        floatingActionButtonPosition = FabPosition.Center,
                        floatingActionButton = {
                            if (activity.currentValues.isAllDataCompleted()) {
                                FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
                                    ButtonFactory(text = "Save") {
                                        activity.endVisit()
                                    }
                                }
                            }
                        },
                        content = {
                            if (activity.isPlanned) {
                                PlannedOfficeWorkDetailsScreen(activity, isDataChangedToRefresh)
                            }else {
                                OfficeWorkScreen(activity, isDataChangedToRefresh)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OfficeWorkScreen(
    activity: OfficeWorkActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val dateCurrentValue = remember {
        mutableStateOf(GlobalFormats.getDashedDate(Locale.getDefault(), PassedValues.officeWorkActivity_startDate!!))
    }

    val officeWorkCurrentValue = remember { mutableStateOf(activity.currentValues.officeWorkTypeCurrentValue) }
    val shiftCurrentValue = remember { mutableStateOf(activity.currentValues.shiftCurrentValue) }
    val commentCurrentText = remember { mutableStateOf(activity.currentValues.commentTextCurrentValue) }
    val commentCurrentError = remember { mutableStateOf(activity.currentValues.textCommentError) }

    officeWorkCurrentValue.value = activity.currentValues.officeWorkTypeCurrentValue
    shiftCurrentValue.value = activity.currentValues.shiftCurrentValue
    commentCurrentText.value = activity.currentValues.commentTextCurrentValue
    commentCurrentError.value = activity.currentValues.textCommentError

    Box(modifier = Modifier
        .padding(horizontal = padding_16)
        .padding(top = padding_8)) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(padding_4)
        ) {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    DateAndTimeComposeView(dateCurrentValue, R.drawable.calendar_icon)
                }
                Box(modifier = Modifier.weight(1F)) {
                    SelectableDropDownMenu(
                        activity,
                        activity.currentValues.shiftList,
                        shiftCurrentValue,
                        isDataChangedToRefresh,
                        false
                    )
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    SelectableDropDownMenu(
                        activity,
                        activity.currentValues.officeWorkList,
                        officeWorkCurrentValue,
                        isDataChangedToRefresh,
                        activity.currentValues.officeWorkErrorValue
                    )
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    CustomOutlinedTextField(commentCurrentText, "comments", commentCurrentError) {
                        activity.currentValues.notifyTextChanges(it)
                        activity.currentValues.textCommentError = false
                        isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                    }
                }
            }
        }
    }
}

@Composable
fun PlannedOfficeWorkDetailsScreen(
    activity: OfficeWorkActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val dateCurrentValue = remember {
        mutableStateOf(GlobalFormats.getDashedDate(Locale.getDefault(), PassedValues.officeWorkActivity_startDate!!))
    }

    val officeWorkCurrentValue = remember { mutableStateOf(activity.currentValues.officeWorkTypeCurrentValue) }
    val shiftCurrentValue = remember { mutableStateOf(activity.currentValues.shiftCurrentValue) }
    val commentCurrentText = remember { mutableStateOf(activity.currentValues.commentTextCurrentValue) }
    val commentCurrentError = remember { mutableStateOf(activity.currentValues.textCommentError) }

    officeWorkCurrentValue.value = activity.currentValues.officeWorkTypeCurrentValue
    shiftCurrentValue.value = activity.currentValues.shiftCurrentValue
    commentCurrentText.value = activity.currentValues.commentTextCurrentValue

    Box(modifier = Modifier
        .padding(horizontal = padding_16)
        .padding(top = padding_8)) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(padding_4)
        ) {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    DateAndTimeComposeView(dateCurrentValue, R.drawable.calendar_icon)
                }
                Box(modifier = Modifier.weight(1F)) {
                    PlannedSelectedDropDownMenu(activity.currentValues.shiftCurrentValue.embedded.name)
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    if (activity.passedPlannedOfficeWork!!.officeWorkName != null) {
                        PlannedSelectedDropDownMenu(activity.currentValues.officeWorkTypeCurrentValue.embedded.name)
                    }
                    else {
                        SelectableDropDownMenu(
                            activity,
                            activity.currentValues.officeWorkList,
                            officeWorkCurrentValue,
                            isDataChangedToRefresh,
                            activity.currentValues.officeWorkErrorValue
                        )
                    }
                }
            }
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                Box(modifier = Modifier.weight(1F)) {
                    CustomOutlinedTextField(commentCurrentText, "comments", commentCurrentError) {
                        activity.currentValues.notifyTextChanges(it)
                        activity.currentValues.textCommentError = false
                        isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                    }
                }
            }
        }
    }
}

@Composable
fun SelectableDropDownMenu(
    activity: OfficeWorkActivity,
    data: List<IdAndNameObj>,
    currentValue: MutableState<IdAndNameObj>,
    isDataChangedToRefresh: MutableState<Boolean>,
    isHasError: Boolean
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
                    data.forEachIndexed { index, item ->
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
                            DropdownMenuItem(
                                modifier = Modifier,
                                onClick = {
                                    if (currentValue.value.id != item.id) {
                                        currentValue.value = item

                                        // notify the current data object
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
}