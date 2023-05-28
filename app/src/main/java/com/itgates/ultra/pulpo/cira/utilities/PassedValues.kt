package com.itgates.ultra.pulpo.cira.utilities

import android.location.Location
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalPlannedOfficeWork
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.RelationalPlannedVisit
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

    // main activity data
    var detailingActivity_presentationId: Long = 1
}