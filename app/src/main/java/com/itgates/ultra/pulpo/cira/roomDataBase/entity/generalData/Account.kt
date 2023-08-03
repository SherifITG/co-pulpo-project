package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.AccountColumns

@Entity(
    tableName = TablesNames.AccountTable,
    primaryKeys = [AccountColumns.ID, AccountColumns.DIVISION_ID, AccountColumns.TBL]
)
data class Account(
    @ColumnInfo(AccountColumns.ID) override val id: Long,
    @Embedded(prefix = "embedded_account_") override val embedded: EmbeddedEntity,
    @ColumnInfo(AccountColumns.TEAM_ID) val teamId: Long,
    @ColumnInfo(AccountColumns.DIVISION_ID) val divisionId: Long,
    @ColumnInfo(AccountColumns.CLASS_ID) val classId: Long,
    @ColumnInfo(AccountColumns.LL_FIRST) val llFirst: String,
    @ColumnInfo(AccountColumns.LG_FIRST) val lgFirst: String,
    @ColumnInfo(AccountColumns.REFERENCE_ID) val referenceId: Long,
    @ColumnInfo(AccountColumns.BRICK_ID) val brickId: Long,
    @ColumnInfo(AccountColumns.ADDRESS) val address: String,
    @ColumnInfo(AccountColumns.TELEPHONE) val telephone: String,
    @ColumnInfo(AccountColumns.MOBILE) val mobile: String,
    @ColumnInfo(AccountColumns.TBL) val table: String
): IdAndNameObj(id, embedded)
