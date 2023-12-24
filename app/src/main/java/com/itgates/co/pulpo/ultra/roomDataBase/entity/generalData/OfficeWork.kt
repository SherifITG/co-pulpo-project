package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum

@Entity(tableName = TablesNames.OfficeWorkTable)
data class OfficeWork(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "online_id") val onlineId: Long,
    @ColumnInfo(name = "ow_type_id") val owTypeId: Int,
    @ColumnInfo(name = "shift") val shift: ShiftEnum,
    @ColumnInfo(name = "planned_ow_id") val plannedOwId: Long,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "start_time") val startTime: String,
    @ColumnInfo(name = "end_date") val endDate: String,
    @ColumnInfo(name = "end_time") val endTime: String,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "is_synced") val isSynced: Boolean,
    @ColumnInfo(name = "sync_date") val syncDate: String,
    @ColumnInfo(name = "sync_time") val syncTime: String
) {
    constructor(
        owTypeId: Int, shift: ShiftEnum, plannedOwId: Long, notes: String, startDate: String,
        startTime: String, endDate: String, endTime: String, userId: Long, isSynced: Boolean,
        syncDate: String, syncTime: String,
    ) : this(
        0L, 0L, owTypeId, shift, plannedOwId, notes, startDate, startTime, endDate,
        endTime, userId, isSynced, syncDate, syncTime,
    )
}