package com.itgates.ultra.pulpo.cira.ui.composeUI

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
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
import com.itgates.ultra.pulpo.cira.ui.activities.AccountsReportActivity
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import java.util.ArrayList
import kotlin.streams.toList
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Class
import com.itgates.ultra.pulpo.cira.ui.activities.PlanningActivity

@Composable
fun AccountReportUI(activity: AccountsReportActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    when(isRoomDataFetchedToRefresh.value) {
        in 0..5 -> {
            when(isDataChangedToRefresh.value) {
                true, false -> {
                    AccountReportScreen(activity, isDataChangedToRefresh)
                }
            }
        }
    }
}

@Composable
fun AccountReportScreen(
    activity: AccountsReportActivity,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val isExpanded = remember { mutableStateOf(-1) }
    val isFilterExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(top = padding_4).padding(horizontal = padding_16),
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
                            AccountFilterItemComposeView(
                                "Division",
                                activity,
                                activity.currentValues.divisionsList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Division"))) },
                                isDataChangedToRefresh
                            )
                            AccountFilterItemComposeView(
                                "Brick",
                                activity,
                                activity.currentValues.bricksList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Brick"))) },
                                isDataChangedToRefresh
                            )
                            AccountFilterItemComposeView(
                                "Account Type",
                                activity,
                                activity.currentValues.accountTypesList,
                                remember{ mutableStateOf(IdAndNameObj(0L, EmbeddedEntity("Select Acc Type"))) },
                                isDataChangedToRefresh
                            )
                            AccountFilterItemComposeView(
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
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(padding_8)
            ) {
                itemsIndexed(activity.currentValues.accountsDataListToShow) { index, item ->
                    Card(
                        shape = ITGatesCardCornerShape,
                        modifier = Modifier
                            .fillMaxWidth(),
                        elevation = padding_16
                    ) {
                        Row(
                            modifier = Modifier.clickable {
                                if (isExpanded.value == index) {
                                    isExpanded.value = -1
                                } else {
                                    isExpanded.value = index
                                }
                            }.padding(padding_16),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column() {
                                TextFactory(
                                    text = "id: (${item.account.id})",
                                    color = ITGatesPrimaryColor
                                )
                                TextFactory(
                                    buildAnnotatedString {
                                        withStyle(style = SpanStyle(ITGatesPrimaryColor)) {
                                            append("${item.account.embedded.name}: ")
                                        }
                                        withStyle(style = SpanStyle(ITGatesSecondaryColor)) {
                                            append("(${item.accTypeName})")
                                        }
                                    },
                                    size = 17.sp
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
                                    val doctors = activity.currentValues.doctorsDataList.stream().filter {
                                        it.doctor.accountId == item.account.id && it.doctor.table == item.account.table // TODO
                                    }.toList()
                                    Column() {
                                        TextFactory(text = "Division: ${item.divName}")
                                        TextFactory(text = "Brick: ${item.brickName}")
                                        TextFactory(text = "Class: ${item.className}")
                                        for (docIndex in 0..doctors.lastIndex) {
                                            TextFactory(
                                                text = "doctor ${docIndex + 1}: ${doctors[docIndex].doctor.embedded.name}" +
                                                        " (${doctors[docIndex].specialityName})"
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
    }
}

@Composable
fun AccountFilterItemComposeView(
    text: String,
    activity: AccountsReportActivity,
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
    activity: AccountsReportActivity,
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
