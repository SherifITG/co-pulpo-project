package com.itgates.co.pulpo.ultra.ui.activities.plannedTabs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itgates.co.pulpo.ultra.ui.activities.PlannedVisitActivity
import com.itgates.co.pulpo.ultra.ui.composeUI.*

@Composable
fun PlannedNavGraph(
    navController: NavHostController,
    activity: PlannedVisitActivity
) {
    NavHost(navController = navController, startDestination = PlannedBarScreen.AmTap.route) {
        composable(route = PlannedBarScreen.AmTap.route) {
            PlannedVisitUI(activity = activity, PlannedBarScreen.AmTap.shiftIndex)
        }
        composable(route = PlannedBarScreen.PmTap.route) {
            PlannedVisitUI(activity = activity, PlannedBarScreen.PmTap.shiftIndex)
        }
        composable(route = PlannedBarScreen.OtherTap.route) {
            PlannedVisitUI(activity = activity, PlannedBarScreen.OtherTap.shiftIndex)
        }
        composable(route = PlannedBarScreen.OfficeWorkTap.route) {
            PlannedVisitUI(activity = activity, PlannedBarScreen.OfficeWorkTap.shiftIndex)
        }
    }
}