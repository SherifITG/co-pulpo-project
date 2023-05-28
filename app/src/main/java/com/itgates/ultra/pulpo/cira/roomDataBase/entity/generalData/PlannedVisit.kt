package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.PlannedVisitTable)
data class PlannedVisit(
    @PrimaryKey val id: Long,
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
    @ColumnInfo("is_done") val isDone: Boolean,
    @ColumnInfo("related_id") val relatedId: Long
)
