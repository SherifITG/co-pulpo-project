package com.itgates.co.pulpo.ultra.ui.activities.plannedTabs

import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum

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
        shiftIndex = ShiftEnum.AM_SHIFT.index.toInt()
    )
    object PmTap: PlannedBarScreen(
        route = "pm_tap",
        title = "PM",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = ShiftEnum.PM_SHIFT.index.toInt()
    )
    object OtherTap: PlannedBarScreen(
        route = "other_tap",
        title = "Other",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = ShiftEnum.OTHER.index.toInt()
    )
    object OfficeWorkTap: PlannedBarScreen(
        route = "Office_Work_tap",
        title = "Office Work",
        icon = R.drawable.acual_visit_focus_icon,
        iconFocused = R.drawable.acual_visit_focus_icon,
        shiftIndex = 4
    )
}