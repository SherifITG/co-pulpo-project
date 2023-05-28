package com.itgates.ultra.pulpo.cira.network.models.requestModels

import com.itgates.ultra.pulpo.cira.network.models.requestModels.GiveawayInfoModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.ProductInfoModel
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ActualVisit
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import kotlin.streams.toList

data class UploadedActualVisitModel(
    val id: Long,
    val offline_id: Long,
    val div_id: Long,
    val type_id: Int,
    val item_id: Long, //--clinic
    val item_doc_id: Long,
    val no_of_doctors: Int,
    val members: String,
    val vdate: String,
    val vtime: String,
    val ampm: Int,
    val comments: String,
    var vplanned_id: Long,
    val insertion_date: String,
    val insertion_time: String,
    val user_id: Long,
    val team_id: Long,
    val visit_duration: String,// !--between time
    val visit_deviation: Long,// !--between time
    val ll: String,//-end
    val lg: String,
    val ll_start: String,// !
    val lg_start: String,// !
    val visit_address: String,// !--address from google geocode
    val is_sync: Int,
    val sync_date: String,
    val sync_time: String,
    val date_added: String,// !
    var product_info: List<ProductInfoModel>,
    var member_info: List<MemberInfoModel>,
    var giveaway_info: List<GiveawayInfoModel>
) {
    constructor(actualVisit: ActualVisit): this (
        actualVisit.onlineId,actualVisit.id, actualVisit.divisionId, actualVisit.accountTypeId,
        actualVisit.itemId, actualVisit.itemDoctorId, actualVisit.noOfDoctors,
        if (actualVisit.multiplicity == MultiplicityEnum.DOUBLE_VISIT) "1" else "0",
        actualVisit.startDate, actualVisit.startTime,
        when (actualVisit.shift) {
            ShiftEnum.AM_SHIFT -> 2
            ShiftEnum.PM_SHIFT -> 1
            ShiftEnum.OTHER -> 3
            else -> 3
        },
        actualVisit.comments, actualVisit.plannedVisitId, actualVisit.insertionDate,
        actualVisit.insertionTime, actualVisit.userId, actualVisit.teamId,
        actualVisit.visitDuration, actualVisit.visitDeviation, actualVisit.llEnd.toString(),
         actualVisit.lgEnd.toString(), actualVisit.llStart.toString(),
        actualVisit.lgStart.toString(), "",
        if (actualVisit.isSynced) 1 else 0,
        actualVisit.syncDate, actualVisit.syncTime, actualVisit.addedDate,
        actualVisit.multipleListsInfo.products.stream().map {
            ProductInfoModel(it)
        }.toList(),
        actualVisit.multipleListsInfo.managers.stream().map {
            MemberInfoModel(it)
        }.toList(),
        actualVisit.multipleListsInfo.giveaways.stream().map {
            GiveawayInfoModel(it)
        }.toList()
    )
}