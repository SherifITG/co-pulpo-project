package com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.AccountTypeColumns

@Entity(tableName = TablesNames.AccountTypeTable)
data class AccountType(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_account_type_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = AccountTypeColumns.SORT) val sorting: Int,
    @ColumnInfo(name = AccountTypeColumns.SHIFT_ID) val shiftId: Int,
    @ColumnInfo(name = AccountTypeColumns.ACCEPTED_DISTANCE) val acceptedDistance: Int
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   sorting: $sorting\n" +
                "   shiftId: $shiftId\n" +
                "   acceptedDistance: $acceptedDistance"
    }
}