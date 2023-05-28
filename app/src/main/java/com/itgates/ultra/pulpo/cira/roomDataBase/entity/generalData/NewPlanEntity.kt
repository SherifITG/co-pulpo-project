package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import java.util.*

@Entity(tableName = TablesNames.NewPlanTable)
data class NewPlanEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo("div_id") val divisionId: Long,// change and the relational query
    @ColumnInfo("acc_type_id") val accountTypeId: Long,
    @ColumnInfo("item_id") val itemId: Long,
    @ColumnInfo("item_doctor_id") val itemDoctorId: Long,
    @ColumnInfo("members") val members: Long,
    @ColumnInfo("visit_date") val visitDate: String,
    @ColumnInfo("visit_time") val visitTime: String,
    @ColumnInfo("shift") val shift: Int,
    @ColumnInfo("insertion_date") val insertionDate: String,
    @ColumnInfo("user_id") val userId: Long,
    @ColumnInfo("team_id") val teamId: Long,
    @ColumnInfo("is_approved") val isApproved: Boolean,
    @ColumnInfo("related_id") val relatedId: Long,
    @ColumnInfo("is_synced") var isSynced: Boolean,
    @ColumnInfo("sync_date") var syncDate: String,
    @ColumnInfo("sync_time") var syncTime: String,
) {
    constructor(
        divisionId: Long, accountTypeId: Long, itemId: Long, itemDoctorId: Long, members: Long,
        visitDate: String, shift: Int, userId: Long, teamId: Long, relatedId: Long
    ) : this(
        0L, 0L, divisionId, accountTypeId, itemId, itemDoctorId, members, visitDate,
        "", shift, GlobalFormats.getDashedDate(Locale.getDefault(), Date()), userId, teamId,
        false, relatedId, false, "", ""
    )
}
