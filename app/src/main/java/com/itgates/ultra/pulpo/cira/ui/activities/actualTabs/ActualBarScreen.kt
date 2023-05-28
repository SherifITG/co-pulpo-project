package com.itgates.ultra.pulpo.cira.ui.activities.actualTabs

import com.itgates.ultra.pulpo.cira.R

sealed class ActualBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val iconFocused: Int
) {
    object VisitDetails: ActualBarScreen(
        route = "visit_details",
        title = "Visit Details",
        icon = R.drawable.acual_visit_tab_icon,
        iconFocused = R.drawable.acual_visit_focus_icon
    )
    object Giveaway: ActualBarScreen(
        route = "giveaway",
        title = "Giveaway",
        icon = R.drawable.actual_giveaway_tab_icon,
        iconFocused = R.drawable.actual_giveaway_focus_icon
    )
    object Product: ActualBarScreen(
        route = "product",
        title = "Product",
        icon = R.drawable.actual_product_tab_icon,
        iconFocused = R.drawable.actual_product_focus_icon
    )
    object VisitReport: ActualBarScreen(
        route = "visit_report",
        title = "Visit Report",
        icon = R.drawable.actual_visit_details_tab_icon,
        iconFocused = R.drawable.actual_visit_details_focus_icon
    )
}