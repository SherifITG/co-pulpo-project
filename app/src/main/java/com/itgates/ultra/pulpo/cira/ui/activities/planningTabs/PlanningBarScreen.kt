package com.itgates.ultra.pulpo.cira.ui.activities.planningTabs

import com.itgates.ultra.pulpo.cira.R

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