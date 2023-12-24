package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.PlannedVisit

data class RelationalPlannedVisit(
    @Embedded val plannedVisit: PlannedVisit,
    val divName: String,
    val lineId: Long,
    val brickName: String?,
    val brickId: Long,
    val accTypeName: String,
    val accTypeDistance: Int,
    val shiftId: Int,
    val accName: String?,
    val firstLL: String?,
    val firstLG: String?,
    val docName: String?
)