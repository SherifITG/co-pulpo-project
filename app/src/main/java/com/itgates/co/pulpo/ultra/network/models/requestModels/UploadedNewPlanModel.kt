package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.NewPlanEntity

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
    val line_id: Long,
    val insertion_date: String
) {
    constructor(newPlan: NewPlanEntity): this (
        newPlan.onlineId, newPlan.id, newPlan.divisionId, newPlan.accountTypeId.toInt(),
        newPlan.accountId, newPlan.accountDoctorId, newPlan.visitDate, newPlan.visitTime, newPlan.userId,
        newPlan.lineId, newPlan.insertionDate
    )
}