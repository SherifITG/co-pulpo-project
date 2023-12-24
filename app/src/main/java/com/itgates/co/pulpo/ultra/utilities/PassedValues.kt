package com.itgates.co.pulpo.ultra.utilities

import android.location.Location
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalPlannedVisit
import java.util.*

object PassedValues {
    // actual activity data
    var actualActivity_isPlannedVisit: Boolean = false
    var actualActivity_PlannedVisitObj: RelationalPlannedVisit? = null
    var actualActivity_startDate: Date? = null
    var actualActivity_startLocation: Location? = null

    // office work activity data
    var officeWorkActivity_isPlannedOfficeWork: Boolean = false
    var officeWorkActivity_PlannedOfficeWorkObj: RelationalPlannedOfficeWork? = null
    var officeWorkActivity_startDate: Date? = null
    var officeWorkActivity_startLocation: Location? = null

    // planned activity data
    var plannedActivity_isToday: Boolean = false

    // main activity data
    var mainActivity_isNewUser: Boolean = false

    // map activity data
    var mapActivity_ll: Double = 0.0
    var mapActivity_lg: Double = 0.0
    var mapActivity_accName: String = "Error"
    var mapActivity_docName: String = "No Doctor"

    // detailing activity data
    var detailingActivity_presentationId: Long = 1

    // DB Table Report activity data
    var dbTableReportActivity_tableName: String = ""
}