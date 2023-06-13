package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Doctor

class DoctorPlanningData (
    @Embedded val doctor: Doctor,
    val specialityName: String?,
    val divId: Long,
    val classId: Long,
    val brickId: Long,
    val divName: String,
    val accTypeId: Long,
    val accTypeName: String,
    val accName: String,
    val className: String?,
    val brickName: String?,
    val catId: Int,
)