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
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.MANAGER
import com.itgates.co.pulpo.ultra.ui.activities.ActualActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.ui.utils.ManagerModule
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ManagersScreen(activity: ActualActivity) {

    val isDataChangedToRefresh = remember { mutableStateOf(false) }

    println("-------------------------------------------------- >> ${isDataChangedToRefresh.value}")
    if (activity.currentValues.isDivisionSelected() && !activity.currentValues.isManagerListEmpty()) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (!activity.currentValues.isAllManagerListIsPicked()) {
                    FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
                        ButtonFactory(text = "Add Manager") {
                            if (activity.currentValues.isAddingManagerIsAccepted()) {
                                activity.currentValues.managersModuleList.add(ManagerModule())
                                isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                            }
                            else {
                                Utilities.createCustomToast(activity.applicationContext, "Fill the last manager record first")
                            }
                        }
                    }
                }

            },
            content = {
                Box {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(padding_8)
                    ) {
                        itemsIndexed(activity.currentValues.managersModuleList) { index, item ->
                            ManagersLazyColumnItem(activity, isDataChangedToRefresh, item, index)
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
            TextFactory(text = "There is no Managers for this Division")
        }
    }
}

@Composable
fun ManagersLazyColumnItem(
    activity: ActualActivity,
    isDataChangedToRefresh: MutableState<Boolean>,
    managerModule: ManagerModule,
    index: Int
) {
    var data = activity.currentValues.getManagerList()
    // Remove System Administrator
    data = data.filter { it.embedded.name != "System Administrator" }

    val currentValue = remember { mutableStateOf(managerModule.managerCurrentValue) }
    currentValue.value = managerModule.managerCurrentValue

    Box(modifier = Modifier) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(padding_4)
        ) {
            Box(modifier = Modifier.weight(1F)) {
                SelectableDropDownMenu(
                    activity,
                    data,
                    currentValue,
                    isDataChangedToRefresh,
                    managerModule.managerErrorValue,
                    index
                )
            }
            Box(
                modifier = Modifier
                    .size(padding_40)
                    .clip(ITGatesCircularCornerShape)
                    .background(ITGatesGreyColor)
                    .clickable {
                        activity.currentValues.managersModuleList.removeAt(index)
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