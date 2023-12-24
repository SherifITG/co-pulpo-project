package com.itgates.co.pulpo.ultra.ui.activities.planningTabs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itgates.co.pulpo.ultra.ui.activities.PlanningActivity
import com.itgates.co.pulpo.ultra.ui.activities.plannedTabs.PlannedBarScreen
import com.itgates.co.pulpo.ultra.ui.composeUI.*

@Composable
fun PlanningNavGraph(
    navController: NavHostController,
    activity: PlanningActivity
) {
    NavHost(navController = navController, startDestination = PlanningBarScreen.SelectTap.route) {
        composable(route = PlanningBarScreen.SelectTap.route) {
            PlanningInnerNavigation(activity = activity)
        }
        composable(route = PlanningBarScreen.SaveTap.route) {
            PlanningSaveScreenUI(activity = activity)
        }
    }
}

@Composable
fun PlanningInnerNavGraph(
    navController: NavHostController,
    activity: PlanningActivity
) {
    NavHost(navController = navController, startDestination = PlannedBarScreen.AmTap.route) {
        composable(route = PlannedBarScreen.AmTap.route) { _ ->
            PlanningUI(activity = activity, PlannedBarScreen.AmTap.shiftIndex)
        }
        composable(route = PlannedBarScreen.PmTap.route) {
            PlanningUI(activity = activity, PlannedBarScreen.PmTap.shiftIndex)
        }
        composable(route = PlannedBarScreen.OtherTap.route) {
            PlanningUI(activity = activity, PlannedBarScreen.OtherTap.shiftIndex)
        }
    }
}