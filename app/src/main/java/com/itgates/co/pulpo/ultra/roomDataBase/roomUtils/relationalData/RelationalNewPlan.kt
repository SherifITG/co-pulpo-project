package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.NewPlanEntity

data class RelationalNewPlan(
    @Embedded val newPlan: NewPlanEntity,
    val firstLL: String,
    val firstLG: String,
    val divName: String,
    val lineId: Long,
    val brickName: String?,
    val brickId: Long,
    val accTypeName: String,
    val shiftId: Int,
    val accName: String,
    val docName: String
)