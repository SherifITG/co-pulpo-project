package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.AccountTypeColumns

@Entity(tableName = TablesNames.AccountTypeTable)
data class AccountType(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_account_type_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = AccountTypeColumns.TBL) val table: String,
    @ColumnInfo(name = AccountTypeColumns.SHORTCUT) val shortcut: String,
    @ColumnInfo(name = AccountTypeColumns.SORTING) val sorting: String,
    @ColumnInfo(name = AccountTypeColumns.CATEGORY_ID) val catId: Int
): IdAndNameObj(id, embedded)