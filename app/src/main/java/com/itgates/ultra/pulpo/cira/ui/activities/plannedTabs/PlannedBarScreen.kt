package com.itgates.ultra.pulpo.cira.ui.activities.plannedTabs

import com.itgates.ultra.pulpo.cira.R

sealed class PlannedBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val iconFocused: Int,
    val shiftIndex: Int,
) {
    object AmTap: PlannedBarScreen(
        route = "am_tap",
        title = "AM",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = 2
    )
    object PmTap: PlannedBarScreen(
        route = "pm_tap",
        title = "PM",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = 1
    )
    object OtherTap: PlannedBarScreen(
        route = "other_tap",
        title = "Other",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = 3
    )
    object OfficeWorkTap: PlannedBarScreen(
        route = "Office_Work_tap",
        title = "Office Work",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = 4
    )
}