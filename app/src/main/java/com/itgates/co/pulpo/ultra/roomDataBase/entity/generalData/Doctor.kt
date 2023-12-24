package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.DoctorColumns

@Entity(
    tableName = TablesNames.DoctorTable,
    primaryKeys = [DoctorColumns.ID, DoctorColumns.ACCOUNT_TYPE_ID, DoctorColumns.ACCOUNT_ID]
)
data class Doctor(
    @ColumnInfo(DoctorColumns.ID) override val id: Long,
    @Embedded(prefix = "embedded_doctor_") override val embedded: EmbeddedEntity,
    @ColumnInfo(DoctorColumns.LINE_ID) val lineId: Long,
    @ColumnInfo(DoctorColumns.ACCOUNT_ID) val accountId: Long,
    @ColumnInfo(DoctorColumns.ACCOUNT_TYPE_ID) val accountTypeId: Int,
    @ColumnInfo(DoctorColumns.ACTIVE_DATE) val activeDate: String,
    @ColumnInfo(DoctorColumns.INACTIVE_DATE) val inactiveDate: String,
    @ColumnInfo(DoctorColumns.SPECIALIZATION_ID) val specializationId: Long,
    @ColumnInfo(DoctorColumns.CLASS_ID) val classId: Long,
    @ColumnInfo(DoctorColumns.GENDER) val gender: String,
    @ColumnInfo(DoctorColumns.TARGET) val target: Int

): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   lineId: $lineId\n" +
                "   accountId: $accountId\n" +
                "   accountTypeId: $accountTypeId\n" +
                "   activeDate: $activeDate\n" +
                "   inactiveDate: $inactiveDate\n" +
                "   specializationId: $specializationId\n" +
                "   classId: $classId\n" +
                "   gender: $gender\n" +
                "   target: $target"
    }
}
