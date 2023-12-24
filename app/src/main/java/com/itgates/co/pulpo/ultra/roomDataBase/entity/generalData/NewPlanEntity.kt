package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import java.util.*

@Entity(tableName = TablesNames.NewPlanTable)
data class NewPlanEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo("div_id") val divisionId: Long,// change and the relational query
    @ColumnInfo("acc_type_id") val accountTypeId: Long,
    @ColumnInfo("account_id") val accountId: Long,
    @ColumnInfo("account_doctor_id") val accountDoctorId: Long,
    @ColumnInfo("members") val members: Long,
    @ColumnInfo("visit_date") val visitDate: String,
    @ColumnInfo("visit_time") val visitTime: String,
    @ColumnInfo("shift") val shift: Int,
    @ColumnInfo("insertion_date") val insertionDate: String,
    @ColumnInfo("user_id") val userId: Long,
    @ColumnInfo("line_id") val lineId: Long,
    @ColumnInfo("is_approved") val isApproved: Boolean,
    @ColumnInfo("related_id") val relatedId: Long,
    @ColumnInfo("is_synced") var isSynced: Boolean,
    @ColumnInfo("sync_date") var syncDate: String,
    @ColumnInfo("sync_time") var syncTime: String,
) {
    constructor(
        divisionId: Long, accountTypeId: Long, accountId: Long, accountDoctorId: Long, members: Long,
        visitDate: String, shift: Int, userId: Long, lineId: Long, relatedId: Long
    ) : this(
        0L, 0L, divisionId, accountTypeId, accountId, accountDoctorId, members, visitDate,
        "", shift, GlobalFormats.getDashedDate(Locale.getDefault(), Date()), userId, lineId,
        false, relatedId, false, "", ""
    )

    override fun toString(): String {
        return "   id: $id\n" +
                "   onlineId: $onlineId\n" +
                "   divisionId: $divisionId\n" +
                "   accountTypeId: $accountTypeId\n" +
                "   accountId: $accountId\n" +
                "   accountDoctorId: $accountDoctorId\n" +
                "   members: $members\n" +
                "   visitDate: $visitDate\n" +
                "   visitTime: $visitTime\n" +
                "   shift: $shift\n" +
                "   insertionDate: $insertionDate\n" +
                "   userId: $userId\n" +
                "   lineId: $lineId\n" +
                "   isApproved: $isApproved\n" +
                "   relatedId: $relatedId\n" +
                "   isSynced: $isSynced\n" +
                "   syncDate: $syncDate\n" +
                "   syncTime: $syncTime"
    }
}
