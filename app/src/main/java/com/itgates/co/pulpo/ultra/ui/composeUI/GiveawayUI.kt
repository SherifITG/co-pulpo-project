package com.itgates.co.pulpo.ultra.ui.composeUI

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.GIVEAWAY
import com.itgates.co.pulpo.ultra.ui.activities.ActualActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.ui.utils.ActualCurrentValues
import com.itgates.co.pulpo.ultra.ui.utils.GiveawayModule
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun GiveawaysScreen(activity: ActualActivity) {

    val isDataChangedToRefresh = remember { mutableStateOf(false) }

    println("-------------------------------------------------- >> ${isDataChangedToRefresh.value}")

    if (activity.currentValues.isDivisionSelected() && !activity.currentValues.isGiveawayListEmpty()) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (!activity.currentValues.isAllGiveawayListIsPicked()) {
                    FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
                        ButtonFactory(text = "Add Giveaway") {
                            if (activity.currentValues.isAddingGiveawayIsAccepted()) {
                                activity.currentValues.giveawaysModuleList.add(GiveawayModule())
                                isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                            }
                            else {
                                Utilities.createCustomToast(activity.applicationContext, "Fill the last giveaway record first")
                            }
                        }
                    }
                }

            },
            content = {
                Box {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(top = padding_8),
                        verticalArrangement = Arrangement.spacedBy(padding_8)
                    ) {
                        itemsIndexed(activity.currentValues.giveawaysModuleList) { index, item ->
                            GiveawaysLazyColumnItem(activity, isDataChangedToRefresh, item, index)
                        }
                    }
                }
            }
        )
    }
    else if (!activity.currentValues.isDivisionSelected()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextFactory(text = "Select Division first")
        }
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextFactory(text = "There is no Giveaways for this Division")
        }
    }
}

@Composable
fun GiveawaysLazyColumnItem(
    activity: ActualActivity,
    isDataChangedToRefresh: MutableState<Boolean>,
    giveawayModule: GiveawayModule,
    index: Int
) {
    val data = activity.currentValues.getGiveawayList()

    val giveawayCurrentValue = remember { mutableStateOf(giveawayModule.giveawayCurrentValue) }
    val sampleCurrentValue = remember { mutableStateOf(giveawayModule.sampleCurrentValue) }
    giveawayCurrentValue.value = giveawayModule.giveawayCurrentValue
    sampleCurrentValue.value = giveawayModule.sampleCurrentValue

    Box(modifier = Modifier.padding(horizontal = padding_16)) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding_4)
        ) {
            Box(modifier = Modifier.weight(5F)) {
                SelectableDropDownMenu(
                    activity,
                    data,
                    giveawayCurrentValue,
                    isDataChangedToRefresh,
                    giveawayModule.giveawayErrorValue,
                    index
                )
            }

            if (giveawayModule.isGiveawaySelected()) {
                Box(modifier = Modifier.weight(2F)) {
                    SelectableDropDownMenu(
                        activity,
                        ActualCurrentValues.giveawaySamplesList,
                        sampleCurrentValue,
                        isDataChangedToRefresh,
                        false,
                        index
                    )
                }
            }
            else {
                Box(modifier = Modifier.weight(2F)) {
                    DisabledDropDownMenu(giveawayModule.sampleCurrentValue.embedded.name)
                }
            }
            Box(
                modifier = Modifier
                    .size(padding_40)
                    .clip(ITGatesCircularCornerShape)
                    .background(ITGatesGreyColor)
                    .clickable {
                        activity.currentValues.giveawaysModuleList.removeAt(index)
                        isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(padding_22),
                    painter = painterResource(R.drawable.trash_icon),
                    contentDescription = "Icon",
                    tint = ITGatesPrimaryColor
                )
            }
        }
    }
}