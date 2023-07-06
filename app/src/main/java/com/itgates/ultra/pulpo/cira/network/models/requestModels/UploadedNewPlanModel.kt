package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.NewPlanEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import kotlin.streams.toList

data class UploadedNewPlanModel(
    val id: Long,
    val offline_id: Long,
    val div_id: Long,
    val type_id: Int,
    val item_id: Long, //--clinic
    val item_doc_id: Long,
    val vdate: String,
    val vtime: String,
    val user_id: Long,
    val team_id: Long,
    val insertion_date: String
) {
    constructor(newPlan: NewPlanEntity): this (
        newPlan.onlineId, newPlan.id, newPlan.divisionId, newPlan.accountTypeId.toInt(),
        newPlan.itemId, newPlan.itemDoctorId, newPlan.visitDate, newPlan.visitTime, newPlan.userId,
        newPlan.teamId, newPlan.insertionDate
    )
}