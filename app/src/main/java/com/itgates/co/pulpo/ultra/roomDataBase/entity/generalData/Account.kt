package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.AccountColumns

@Entity(
    tableName = TablesNames.AccountTable,
    primaryKeys = [
        AccountColumns.ID, AccountColumns.ACCOUNT_TYPE_ID, AccountColumns.LINE_ID,
        AccountColumns.DIVISION_ID, AccountColumns.BRICK_ID
    ]
)
data class Account(
    @ColumnInfo(AccountColumns.ID) override val id: Long,
    @Embedded(prefix = "embedded_account_") override val embedded: EmbeddedEntity,
    @ColumnInfo(AccountColumns.LINE_ID) val lineId: Long,
    @ColumnInfo(AccountColumns.DIVISION_ID) val divisionId: Long,
    @ColumnInfo(AccountColumns.CLASS_ID) val classId: Long,
    @ColumnInfo(AccountColumns.BRICK_ID) val brickId: Long,
    @ColumnInfo(AccountColumns.ACCOUNT_TYPE_ID) val accountTypeId: Int,
    @ColumnInfo(AccountColumns.ADDRESS) val address: String,
    @ColumnInfo(AccountColumns.TELEPHONE) val telephone: String,
    @ColumnInfo(AccountColumns.MOBILE) val mobile: String,
    @ColumnInfo(AccountColumns.EMAIL) val email: String,
    @ColumnInfo(AccountColumns.LL_FIRST) val llFirst: String,
    @ColumnInfo(AccountColumns.LG_FIRST) val lgFirst: String,
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   lineId: $lineId\n" +
                "   divisionId: $divisionId\n" +
                "   classId: $classId\n" +
                "   brickId: $brickId\n" +
                "   accountTypeId: $accountTypeId\n" +
                "   address: $address\n" +
                "   telephone: $telephone\n" +
                "   mobile: $mobile\n" +
                "   llFirst: $llFirst\n" +
                "   lgFirst: $lgFirst"
    }
}
