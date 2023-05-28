package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.DoctorColumns

@Entity(tableName = TablesNames.DoctorTable, primaryKeys = [DoctorColumns.ID, DoctorColumns.TBL])
data class Doctor(
    @ColumnInfo("id") override val id: Long,
    @Embedded(prefix = "embedded_doctor_") override val embedded: EmbeddedEntity,
    @ColumnInfo("doctor_account_id") val doctorAccountId: Long,
    @ColumnInfo("account_id") val accountId: Long,
    @ColumnInfo("d_active_from") val dActiveFrom: String,
    @ColumnInfo("d_inactive_from") val dInactiveFrom: String,
    @ColumnInfo("team_id") val teamId: Long,
    @ColumnInfo("specialization_id") val specializationId: Long,
    @ColumnInfo("class_id") val classId: Long,
    @ColumnInfo("active_from") val activeFrom: String,
    @ColumnInfo("inactive_from") val inactiveFrom: String,
    @ColumnInfo("active") val active: Int,
    @ColumnInfo("tbl") val table: String
): IdAndNameObj(id, embedded)
