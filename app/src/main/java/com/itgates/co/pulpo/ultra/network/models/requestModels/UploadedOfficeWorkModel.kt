package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.OfficeWork
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum

data class UploadedOfficeWorkModel(
    val id: Long,
    val offline_id: Long,
    val date: String,
    val time: String,
    val ow_type_id: Int,
    val shift_id: Int,
    val ow_plan_id: Long,
    val notes: String
) {
    constructor(ow: OfficeWork): this (
        ow.onlineId, ow.id, ow.startDate, ow.startTime, ow.owTypeId,
        when (ow.shift) {
            ShiftEnum.AM_SHIFT -> ShiftEnum.AM_SHIFT.index.toInt()
            ShiftEnum.PM_SHIFT -> ShiftEnum.PM_SHIFT.index.toInt()
            ShiftEnum.OTHER -> ShiftEnum.OTHER.index.toInt()
            else -> ShiftEnum.OTHER.index.toInt()
        },
        ow.plannedOwId, ow.notes
    )
}