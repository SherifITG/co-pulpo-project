package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.PlannedVisit

data class RelationalPlannedVisit(
    @Embedded val plannedVisit: PlannedVisit,
    val firstLL: String,
    val firstLG: String,
    val divName: String,
    val teamId: Long,
    val brickName: String?,
    val brickId: Long,
    val accTypeName: String,
    val categoryId: Int,
    val accName: String,
    val docName: String
)