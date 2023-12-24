package com.itgates.co.pulpo.ultra.ui.activities.planningTabs

import com.itgates.co.pulpo.ultra.R

sealed class PlanningBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val iconFocused: Int,
) {
    object SelectTap: PlanningBarScreen(
        route = "select_tap",
        title = "Select",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
    )
    object SaveTap: PlanningBarScreen(
        route = "save_tap",
        title = "Save",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
    )
}
//
//data class PlanningInnerBarScreen(
//    val route: String,
//    val title: String,
//    val icon: Int,
//    val iconFocused: Int,
//)