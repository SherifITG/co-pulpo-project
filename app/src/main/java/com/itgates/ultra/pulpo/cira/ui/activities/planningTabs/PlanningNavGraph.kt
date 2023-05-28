package com.itgates.ultra.pulpo.cira.ui.activities.planningTabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itgates.ultra.pulpo.cira.ui.activities.PlanningActivity
import com.itgates.ultra.pulpo.cira.ui.composeUI.*
import com.itgates.ultra.pulpo.cira.ui.theme.ITGatesErrorColor
import com.itgates.ultra.pulpo.cira.ui.theme.ITGatesWhiteColor

@Composable
fun PlanningNavGraph(
    navController: NavHostController,
    activity: PlanningActivity
) {
    NavHost(navController = navController, startDestination = PlanningBarScreen.SelectTap.route) {
        composable(route = PlanningBarScreen.SelectTap.route) {
            PlanningUI(activity = activity)
        }
        composable(route = PlanningBarScreen.SaveTap.route) {
            PlanningSaveScreen(activity = activity)
        }
    }
}