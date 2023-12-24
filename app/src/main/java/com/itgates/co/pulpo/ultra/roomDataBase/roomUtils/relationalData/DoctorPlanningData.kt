package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData

import androidx.room.Embedded
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Doctor

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
    val shiftId: Int,
    val coverage: Int,
) {
    fun getDoctorCoverageLeve(): Long {
        return if (doctor.target == 0) 4L
        else if (coverage >= doctor.target) 1L
        else if (coverage > 0) 2L
        else 3L
    }
}