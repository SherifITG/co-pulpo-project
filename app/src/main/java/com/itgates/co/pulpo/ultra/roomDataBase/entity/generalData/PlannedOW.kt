package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.PlannedOWTable)
data class PlannedOW(
    @PrimaryKey val id: Long,
    @ColumnInfo("ow_type_id") val owTypeId: Long,
    @ColumnInfo("shift") val shift: Int,
    @ColumnInfo("ow_date") val visitDate: String,
    @ColumnInfo("ow_time") val visitTime: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo("is_done") val isDone: Boolean
)
