package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.NewPlanEntity

data class RelationalNewPlan(
    @Embedded val newPlan: NewPlanEntity,
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