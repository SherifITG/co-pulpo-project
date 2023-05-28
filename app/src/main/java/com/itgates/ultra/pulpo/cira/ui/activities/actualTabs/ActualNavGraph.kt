package com.itgates.ultra.pulpo.cira.ui.activities.actualTabs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.itgates.ultra.pulpo.cira.ui.activities.ActualActivity
import com.itgates.ultra.pulpo.cira.ui.composeUI.ActualVisitUI
import com.itgates.ultra.pulpo.cira.ui.composeUI.GiveawaysScreen
import com.itgates.ultra.pulpo.cira.ui.composeUI.ProductsScreen
import com.itgates.ultra.pulpo.cira.ui.composeUI.VisitReportScreen

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