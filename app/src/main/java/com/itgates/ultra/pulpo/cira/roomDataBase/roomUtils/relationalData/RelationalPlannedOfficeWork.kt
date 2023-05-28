package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.PlannedVisit
import com.itgates.ultra.pulpo.cira.ui.utils.OfficeWorkCurrentValues

data class RelationalPlannedOfficeWork(
    @Embedded val plannedVisit: PlannedVisit,
    val officeWorkName: String?
)