package com.itgates.co.pulpo.ultra.ui.composeUI

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Account
import com.itgates.co.pulpo.ultra.ui.activities.ActualActivity
import com.itgates.co.pulpo.ultra.ui.activities.actualTabs.navigateAndClearBackStack
import com.itgates.co.pulpo.ultra.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun VisitReportScreen(activity: ActualActivity, navController: NavHostController) {

    val isDataChangedToRefresh = remember { mutableStateOf(false) }

    println("-------------------------------------------------- >> ${isDataChangedToRefresh.value}")

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
                ButtonFactory(text = "End Visit") {
                    val errorRoute = activity.currentValues.isAllDataReady()
                    if (errorRoute == "NoRouteText") {
                        activity.endVisit()
                    }
                    else {
                        println("route بقى")
                        // route بقى
                        navController.navigateAndClearBackStack(errorRoute)
                    }
                }
            }
        },
        content = {
            Box (modifier = Modifier
                .padding(horizontal = padding_24)
                .padding(top = padding_8)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(padding_8)
                ) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(padding_8)
                        ) {
                            Column(
                                modifier = Modifier.weight(1F),
                                verticalArrangement = Arrangement.spacedBy(padding_8)
                            ) {
                                Box(modifier = Modifier) {
                                    TextFactory("Location Map", size = 18.sp, fontWeight = FontWeight.Bold)
                                }
                                Box {
                                    TextFactory(
                                        text = if (activity.currentValues.isAccountSelected()) {
                                            val account = activity.currentValues.accountCurrentValue as Account
                                            if (account.llFirst.isNotEmpty() && account.lgFirst.isNotEmpty()) {
                                                "account location ready"
                                            } else {
                                                "account has no location"
                                            }
                                        } else {
                                            "account not selected"
                                        },
                                        color = ITGatesOrangeColor
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(ITGatesCircularCornerShape)
                                    .clickable { activity.viewMap() }
                                    .padding(padding_8)
                            ) {
                                Icon(
                                    modifier = Modifier.size(padding_40),
                                    painter = painterResource(R.drawable.location_map_icon),
                                    contentDescription = "Location Icon",
                                    tint = ITGatesPrimaryColor
                                )
                            }
                        }
                    }
                    item {
                        Box(modifier = Modifier) {
                            TextFactory("Visit Details", size = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (activity.currentValues.isMainPageAnyItemSelected()) {
                        if (activity.currentValues.isDivisionSelected()) {
                            item {
                                ActualTabRowItemUI("Division", activity.currentValues.divisionCurrentValue.embedded.name)
                            }
                        }
                        if (activity.currentValues.isBrickSelected()) {
                            item {
                                ActualTabRowItemUI("Brick", activity.currentValues.brickCurrentValue.embedded.name)
                            }
                        }
                        if (activity.currentValues.isAccTypeSelected()) {
                            item {
                                ActualTabRowItemUI("Account Type", activity.currentValues.accTypeCurrentValue.embedded.name)
                            }
                        }
                        if (activity.currentValues.isAccountSelected()) {
                            item {
                                ActualTabRowItemUI("Account", activity.currentValues.accountCurrentValue.embedded.name)
                            }
                        }
                        if (activity.currentValues.isDoctorSelected()) {
                            item {
                                ActualTabRowItemUI("Doctor", activity.currentValues.doctorCurrentValue.embedded.name)
                            }
                        }
                        if (activity.currentValues.isNoOfDoctorsSelected()) {
                            item {
                                ActualTabRowItemUI("Num Of Doctors", activity.currentValues.noOfDoctorCurrentValue.embedded.name)
                            }
                        }
                        if (activity.currentValues.isMultiplicitySelected()) {
                            item {
                                ActualTabRowItemUI("Visit Type", activity.currentValues.multiplicityCurrentValue.embedded.name)
                            }
                        }
                    }
                    else {
                        item {
                            TextFactory(
                                "No item is selected",
                                modifier = Modifier.fillMaxWidth(),
                                color = ITGatesErrorColor
                            )
                        }
                    }

                    // managers
                    if (activity.currentValues.isMultiplicityDouble()) {
                        item {
                            Box(modifier = Modifier.padding(top = padding_12)) {
                                TextFactory("Managers Details", size = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (activity.currentValues.managersModuleList.isNotEmpty()) {
                            itemsIndexed(activity.currentValues.managersModuleList) { index, item ->
                                ActualTabRowItemUI("Manager ${index + 1}", item.managerCurrentValue.embedded.name)
                            }
                        }
                        else {
                            if (!activity.currentValues.isUserManager()) {
                                item {
                                    TextFactory(
                                        "You must add 1 manager at least",
                                        modifier = Modifier.fillMaxWidth(),
                                        color = ITGatesErrorColor
                                    )
                                }
                            }
                            else {
                                item {
                                    TextFactory(
                                        "No managers",
                                        modifier = Modifier.fillMaxWidth(),
                                        color = ITGatesErrorColor
                                    )
                                }
                            }
                        }
                    }

                    // giveaways
                    if (activity.currentValues.giveawaysModuleList.isNotEmpty()) {
                        item {
                            Box(modifier = Modifier.padding(top = padding_12)) {
                                TextFactory("Giveaways Details", size = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        itemsIndexed(activity.currentValues.giveawaysModuleList) { index, item ->
                            ActualTabRowItemUI("Giveaway ${index + 1}", item.giveawayCurrentValue.embedded.name)
                        }
                    }

                    // products
                    if (activity.currentValues.productsModuleList.isNotEmpty()) {
                        item {
                            Box(modifier = Modifier.padding(top = padding_12)) {
                                TextFactory("Products Details", size = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        itemsIndexed(activity.currentValues.productsModuleList) { index, item ->
                            if (item.isProductSelected()) {
                                if (NetworkConfiguration.configuration.name == "helal") {
                                    TextFactory("Product ${index + 1} : ", color = ITGatesSecondaryColor)
                                    ActualTabRowItemUI("Name", item.productCurrentValue.embedded.name)
                                    if (item.quotation.isNotEmpty()) ActualTabRowItemUI("Quotation", item.quotation)
                                    if (item.isDemo) ActualTabRowItemUI("Demo Date", item.demoDate)
                                }
                                else {
                                    ActualTabRowItemUI("Product ${index + 1}", item.productCurrentValue.embedded.name)
                                }
                            }
                            else {
                                if (NetworkConfiguration.configuration.name == "helal") {
                                    TextFactory("Product ${index + 1} : ", color = ITGatesSecondaryColor)
                                    ActualTabRowItemUI(
                                        "Name",
                                        item.productCurrentValue.embedded.name,
                                        ITGatesErrorColor
                                    )
                                }
                                else {
                                    ActualTabRowItemUI(
                                        "Product ${index + 1}",
                                        item.productCurrentValue.embedded.name,
                                        ITGatesErrorColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ActualTabRowItemUI(name: String, value: String, valueColor: Color = ITGatesPrimaryColor) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        Box {
            TextFactory(text = name, color = ITGatesOrangeColor)
        }
        Box(modifier = Modifier.weight(1F)) {
            TextFactory(
                text = value,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                color = valueColor
            )
        }
    }
}