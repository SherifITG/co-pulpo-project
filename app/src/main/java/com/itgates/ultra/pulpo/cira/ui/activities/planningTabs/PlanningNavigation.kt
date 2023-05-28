package com.itgates.ultra.pulpo.cira.ui.activities.planningTabs

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.itgates.ultra.pulpo.cira.ui.activities.PlanningActivity
import com.itgates.ultra.pulpo.cira.ui.theme.*

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun PlanningNavigation(activity: PlanningActivity) {
    val navController = rememberNavController()
    activity.currentValues.tapNavigatingFun = { navController.navigate(PlanningBarScreen.SaveTap.route)}
    Scaffold { PlanningNavGraph(navController = navController, activity = activity) }
}