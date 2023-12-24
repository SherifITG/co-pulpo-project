package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.MultiplicityEnum

@Entity(tableName = TablesNames.PlannedVisitTable)
data class PlannedVisit(
    @PrimaryKey val id: Long,
    @ColumnInfo("line_id") val lineId: Long,
    @ColumnInfo("div_id") val divisionId: Long,// change and the relational query
    @ColumnInfo("brick_id") val brickId: Long,
    @ColumnInfo("acc_type_id") val accountTypeId: Long,
    @ColumnInfo("account_id") val accountId: Long,
    @ColumnInfo("account_doctor_id") val accountDoctorId: Long?,
    @ColumnInfo("speciality_id") val specialityId: Long?,
    @ColumnInfo("acc_class") val accountClass: Long,
    @ColumnInfo("doc_class") val doctorClass: Long,
    @ColumnInfo("visit_type") val visitType: MultiplicityEnum,
    @ColumnInfo("visit_date") val visitDate: String,
    @ColumnInfo("visit_time") val visitTime: String,
    @ColumnInfo("is_done") val isDone: Boolean
) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   lineId: $lineId\n" +
                "   divisionId: $divisionId\n" +
                "   brickId: $brickId\n" +
                "   accountTypeId: $accountTypeId\n" +
                "   accountId: $accountId\n" +
                "   accountDoctorId: ${accountDoctorId.toString()}\n" +
                "   specialityId: ${specialityId.toString()}\n" +
                "   accountClass: $accountClass\n" +
                "   doctorClass: $doctorClass\n" +
                "   visitType: $visitType\n" +
                "   visitDate: $visitDate\n" +
                "   visitTime: $visitTime\n" +
                "   isDone: $isDone\n" +
                "   lineId: $lineId"
    }
}
