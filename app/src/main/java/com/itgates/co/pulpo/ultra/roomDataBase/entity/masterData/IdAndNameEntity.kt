package com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.IdAndNameTable, primaryKeys = ["id", "table_identifier"])
data class IdAndNameEntity(
    @ColumnInfo(name = "id") override val id: Long,
    @ColumnInfo(name = "table_identifier") val tableId: IdAndNameTablesNamesEnum,
    @Embedded(prefix = "embedded_entity_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "line_id") val lineId: Long,
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   tableId: $tableId\n" +
                "   name: ${embedded.name}\n" +
                "   lineId: $lineId"
    }
}