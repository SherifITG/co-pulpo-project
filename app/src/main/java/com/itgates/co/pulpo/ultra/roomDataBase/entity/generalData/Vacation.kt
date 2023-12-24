package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomPathListModule
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.DurationEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import java.util.*

@Entity(tableName = TablesNames.VacationTable)
data class Vacation(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo("vacation_type_id") val vacationTypeId: Long,
    @ColumnInfo("duration_type") val durationType: DurationEnum,
    @ColumnInfo("shift") val shift: ShiftEnum,
    @ColumnInfo("date_from") val dateFrom: String,
    @ColumnInfo("date_to") val dateTo: String,
    @ColumnInfo("note") val note: String,
    @ColumnInfo("user_id") val userId: Long,
    @ColumnInfo("is_approved") val isApproved: Boolean,
    @ColumnInfo("is_synced") var isSynced: Boolean,
    @ColumnInfo("sync_date") var syncDate: String,
    @ColumnInfo("sync_time") var syncTime: String,
    @ColumnInfo("uri_list_info") val uriListsInfo: RoomPathListModule
) {
    constructor(
        vacationTypeId: Long, durationType: DurationEnum, shift: ShiftEnum, dateFrom: String,
        dateTo: String, note: String, userId: Long, uriListsInfo: RoomPathListModule
    ) : this(
        0L, 0L, vacationTypeId, durationType, shift, dateFrom, dateTo,
        note, userId, false, false, "", "",
        uriListsInfo
    )

    override fun toString(): String {
        return "   id: $id\n" +
                "   onlineId: $onlineId\n" +
                "   vacationTypeId: $vacationTypeId\n" +
                "   durationTypeId: $durationType\n" +
                "   shift: $shift\n" +
                "   dateFrom: $dateFrom\n" +
                "   dateTo: $dateTo\n" +
                "   note: $note\n" +
                "   userId: $userId\n" +
                "   isApproved: $isApproved\n" +
                "   isSynced: $isSynced\n" +
                "   syncDate: $syncDate\n" +
                "   syncTime: $syncTime" +
                " ...END_OF_VACATION..."
    }

    fun showUriListInfo(): String {
        return "uriListsInfo: $uriListsInfo"
    }
}
