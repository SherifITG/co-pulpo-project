package com.itgates.co.pulpo.ultra.ui.activities.actualTabs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itgates.co.pulpo.ultra.ui.activities.ActualActivity
import com.itgates.co.pulpo.ultra.ui.composeUI.ActualVisitUI
import com.itgates.co.pulpo.ultra.ui.composeUI.GiveawaysScreen
import com.itgates.co.pulpo.ultra.ui.composeUI.ProductsScreen
import com.itgates.co.pulpo.ultra.ui.composeUI.VisitReportScreen

@Composable
fun ActualNavGraph(
    navController: NavHostController,
    activity: ActualActivity
) {
    NavHost(navController = navController, startDestination = ActualBarScreen.VisitDetails.route) {
        composable(route = ActualBarScreen.VisitDetails.route) {
            ActualVisitUI(activity = activity)
        }
        composable(route = ActualBarScreen.Giveaway.route) {
            GiveawaysScreen(activity = activity)
        }
        composable(route = ActualBarScreen.Product.route) {
            ProductsScreen(activity = activity)
        }
        composable(route = ActualBarScreen.VisitReport.route) {
            VisitReportScreen(activity = activity, navController = navController)
        }
    }
}