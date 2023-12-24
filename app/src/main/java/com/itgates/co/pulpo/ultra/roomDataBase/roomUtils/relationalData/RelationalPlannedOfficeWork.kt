package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.PlannedOW

data class RelationalPlannedOfficeWork(
    @Embedded val plannedOW: PlannedOW,
    val officeWorkName: String?
)