package com.itgates.co.pulpo.ultra.ui.utils

import android.content.Intent
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.activities.*
import com.itgates.co.pulpo.ultra.utilities.PassedValues

data class MainGridController (val activity: MainActivity, val screenWidth: Int) {
    val columnsNum = 3
    private val iconWeight = 5F
    private val spaceWeight = 1F
    private val screenSegments = (columnsNum * iconWeight) + ((columnsNum - 1) * spaceWeight)
    val featureWidth = (iconWeight * this.screenWidth) / screenSegments
    val spaceWidth = (spaceWeight * this.screenWidth) / screenSegments
    val features = listOf(
        MainActivityFeature(R.drawable.new_planning_visits_icon, "Planning") {
            activity.startActivity(Intent(activity, PlanningActivity::class.java))
        },
        MainActivityFeature(R.drawable.report_planned_visits_icon, "Planned\nVisit") {
            PassedValues.plannedActivity_isToday = true
            activity.startActivity(Intent(activity, PlannedVisitActivity::class.java))
        },
        MainActivityFeature(R.drawable.report_actual_visits_icon, "Unplanned\nVisit") {
            activity.startActualActivity()
        },
        MainActivityFeature(R.drawable.main_officework_icon, "Office\nWork") {
            activity.startOfficeWorkActivity()
        },
        MainActivityFeature(R.drawable.main_officework_icon, "Vacation") {
            activity.startActivity(Intent(activity, VacationActivity::class.java))
        },
        MainActivityFeature(R.drawable.my_location_icon, "Your\nLocation") {
            activity.startActivity(Intent(activity, MyLocationMapActivity::class.java))
        },
        MainActivityFeature(R.drawable.main_report_icon, "Reports") {
            activity.startActivity(Intent(activity, ReportsActivity::class.java))
        },
        MainActivityFeature(R.drawable.main_data_icon, "Data\nCenter") {
            activity.startActivity(Intent(activity, DataCenterActivity::class.java))
        },
        MainActivityFeature(R.drawable.main_data_icon, "Online\nDebugging") {
            activity.startActivity(Intent(activity, OnlineDebuggingActivity::class.java))
        },
//        MainActivityFeature(R.drawable.circle_icon, "") {},
    )
}