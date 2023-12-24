package com.itgates.co.pulpo.ultra.ui.composeUI

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.ui.activities.VacationActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.uiComponents.MyOutlineBrowserTextField
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VacationUI(activity: VacationActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    println("=====================================================================================")
    when(isRoomDataFetchedToRefresh.value) {
        true, false -> {
            when (isDataChangedToRefresh.value) {
                true, false -> {
                    Scaffold(
                        floatingActionButtonPosition = FabPosition.Center,
                        floatingActionButton = {
//                            if (activity.currentValues.isAllDataCompleted()) {
//                                FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
//                                    ButtonFactory(text = "Save") {
//                                        activity.saveVacation()
//                                    }
//                                }
//                            }
                        },
                        content = {
                            VacationScreen(activity, isDataChangedToRefresh)
                        }
                    )
                }
            }
        }
    }

}
@SuppressLint("MutableCollectionMutableState")
@Composable
fun VacationScreen(
    activity: VacationActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val attachmentWidgetHasError = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            activity.currentValues.attachmentsUris.add(uri)
            isDataChangedToRefresh.value = !isDataChangedToRefresh.value
        }
        println("---------------------------------------------------------------------------------")
        println(activity.currentValues.attachmentsUris.size)
//        doFun(uri)
    }

    val vacationTypeCurrentValue = remember { mutableStateOf(activity.currentValues.vacationTypeCurrentValue) }
    val durationCurrentValue = remember { mutableStateOf(activity.currentValues.durationCurrentValue) }
    val shiftCurrentValue = remember { mutableStateOf(activity.currentValues.shiftCurrentValue) }
    val noteCurrentText = remember { mutableStateOf(activity.currentValues.noteTextCurrentValue) }
    val noteCurrentError = remember { mutableStateOf(activity.currentValues.textNoteError) }

    vacationTypeCurrentValue.value = activity.currentValues.vacationTypeCurrentValue
    durationCurrentValue.value = activity.currentValues.durationCurrentValue
    shiftCurrentValue.value = activity.currentValues.shiftCurrentValue
    noteCurrentText.value = activity.currentValues.noteTextCurrentValue
    noteCurrentError.value = activity.currentValues.textNoteError

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var pickedDateFrom by remember {
        mutableStateOf(LocalDate.parse(activity.currentValues.dateFrom, formatter))
    }
    var pickedDateTo by remember {
        mutableStateOf(LocalDate.parse(activity.currentValues.dateTo, formatter))
    }
    pickedDateFrom = LocalDate.parse(activity.currentValues.dateFrom, formatter)
    pickedDateTo = LocalDate.parse(activity.currentValues.dateTo, formatter)

    val dateFromDialogState = rememberMaterialDialogState()
    val dateToDialogState = rememberMaterialDialogState()

    val vacationDateFrom = remember {
        mutableStateOf(DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateFrom) )
    }
    val vacationDateTo = remember {
        mutableStateOf(DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo) )
    }
    vacationDateFrom.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateFrom)
    vacationDateTo.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo)

    Column(
        modifier = Modifier
            .padding(top = padding_4)
            .padding(horizontal = padding_16),
        verticalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
            FilterDateComposeView("Date From", vacationDateFrom, Modifier.weight(1F)) {
                dateFromDialogState.show()
            }
            FilterDateComposeView("Date To", vacationDateTo, Modifier.weight(1F)) {
                dateToDialogState.show()
            }
        }
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(padding_4)) {
            Box(modifier = Modifier.weight(1F)) {
                SelectableDropDownMenu(
                    activity,
                    activity.currentValues.vacationTypeList,
                    vacationTypeCurrentValue,
                    isDataChangedToRefresh,
                    activity.currentValues.vacationTypeErrorValue
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            val screenWidth = LocalConfiguration.current.screenWidthDp
            val fullDayMaxWidth = (screenWidth - 32).div(1.0)
            val fullDayMinWidth = (screenWidth - 36).div(2.0)
            val isFullDayDuration = activity.currentValues.isFullDayDuration()
            Box(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = SpringSpec(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .width(if (isFullDayDuration) fullDayMaxWidth.dp else fullDayMinWidth.dp)
            ) {
                SelectableDropDownMenu(
                    activity,
                    activity.currentValues.durationList,
                    durationCurrentValue,
                    isDataChangedToRefresh,
                    false
                )
            }
            Spacer(
                modifier = Modifier.width(padding_4)
            )
            Box(
                modifier = Modifier
                    .width(fullDayMinWidth.dp)
            ) {
                SelectableDropDownMenu(
                    activity,
                    activity.currentValues.shiftList,
                    shiftCurrentValue,
                    isDataChangedToRefresh,
                    false
                )
            }
        }
        // ----------------------------------------------
        MyOutlineBrowserTextField(
            activity = activity, names = activity.currentValues.attachmentsUris,
            myHint = "Attachments", hasError = attachmentWidgetHasError,
            isDataChangedToRefresh = isDataChangedToRefresh
        ) {
//            launcher.launch("file/*")
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        Box(modifier = Modifier.weight(1F)) {
            CustomOutlinedTextField(
                noteCurrentText,
                "note",
                noteCurrentError,
            ) {
                activity.currentValues.notifyTextChanges(it)
                activity.currentValues.textNoteError = false
                isDataChangedToRefresh.value = !isDataChangedToRefresh.value
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
                    vacationDateFrom.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateFrom)
                    activity.currentValues.dateFrom = vacationDateFrom.value
                    if (pickedDateFrom.isAfter(pickedDateTo)) {
                        pickedDateTo = it
                        vacationDateTo.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo)
                        activity.currentValues.dateTo = vacationDateTo.value
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
                    vacationDateTo.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDateTo)
                    activity.currentValues.dateTo = vacationDateTo.value
                }
            }
        }
    }
}

@Composable
fun SelectableDropDownMenu(
    activity: VacationActivity,
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
                                        println("--------------------------------------------------------------------")
                                        println(activity.currentValues.dateFrom)
                                        println(activity.currentValues.dateTo)

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


