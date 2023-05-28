package com.itgates.ultra.pulpo.cira.ui.activities.plannedTabs

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.itgates.ultra.pulpo.cira.ui.activities.PlannedVisitActivity
import com.itgates.ultra.pulpo.cira.ui.composeUI.TextFactory
import com.itgates.ultra.pulpo.cira.ui.theme.*

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun PlannedNavigation(activity: PlannedVisitActivity) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { PlannedNavBar(navController = navController) },
    ) {
        PlannedNavGraph(
            navController = navController,
            activity = activity
        )
    }
}

@Composable
fun PlannedNavBar(navController: NavHostController) {
    val screens = listOf(
        PlannedBarScreen.AmTap,
        PlannedBarScreen.PmTap,
        PlannedBarScreen.OtherTap,
        PlannedBarScreen.OfficeWorkTap

    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Box(
        modifier = Modifier
            .padding(top = padding_4)
            .clip(ITGatesBarCornerShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
//                .clip(ITGatesBarCornerShape)
//                .background(ITGatesGreyColor)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = padding_16)
                    .fillMaxWidth()
                    .height(padding_55)
                    .clip(ITGatesBarCornerShape)
                    .background(ITGatesGreyColor)
            ) {
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .height(padding_55)) {
                val tabIndex = when(currentDestination?.route) {
                    PlannedBarScreen.AmTap.route -> 0
                    PlannedBarScreen.PmTap.route -> 1
                    PlannedBarScreen.OtherTap.route -> 2
                    PlannedBarScreen.OfficeWorkTap.route -> 3
                    else -> { 0 }
                }

                val screenWidth = LocalConfiguration.current.screenWidthDp
                val startWidth = 3
                val dividerWidth = 7
                val screensLength = screens.size
                val itemWidth = ((screenWidth - 2 * (16 + startWidth) - (screensLength-1) * dividerWidth).div(screensLength))
                val firstBoxWidth = (startWidth + 16) + tabIndex * (itemWidth + dividerWidth)

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = padding_3)
                        .clip(ITGatesBarCornerShape)
                        .animateContentSize(
                            animationSpec = SpringSpec(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .width(firstBoxWidth.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(itemWidth.dp)
                        .padding(vertical = padding_3)
                        .clip(ITGatesBarCornerShape)
                        .background(ITGatesPrimaryColor)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = padding_3)
                        .clip(ITGatesBarCornerShape)
                )
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = padding_16)
                    .height(padding_55)
            ) {
                screens.forEach { screen ->
                    AddItem(
                        screen = screen,
                        currentDestination = currentDestination,
                        navController = navController
                    )
                    if (screen != screens.last()) {
                        Spacer(
                            modifier = Modifier
                                .padding(vertical = padding_3)
                                .fillMaxHeight()
                                .width(padding_1)
                                .background(ITGatesWhiteColor)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: PlannedBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    BottomNavigationItem(
        modifier = Modifier
            .padding(padding_3)
            .clip(ITGatesBarCornerShape),
        selected = isSelected,
        icon = {
            TextFactory(
                text = screen.title,
                size = textSize_17,
                color = if (isSelected) ITGatesWhiteColor else ITGatesPrimaryColor,
            )
//            Icon(
//                modifier = Modifier.fillMaxSize(0.55F),
//                painter = painterResource(if (isSelected) screen.icon else screen.iconFocused),
//                contentDescription = "Nav Icon",
//                tint = if (isSelected) ITGatesWhiteColor else ITGatesPrimaryColor
//            )
        },
        onClick = {
            if (!isSelected) {
                navController.navigateAndClearBackStack(
                    screen.route
                )
            }
        }
    )
}

fun NavHostController.navigateAndClearBackStack(route: String) {
    if (this.backQueue.size == 3) {
        this.backQueue.removeLast()
        if (route == PlannedBarScreen.AmTap.route) {
            this.backQueue.removeLast()
        }
    }
    this.navigate(route)
}