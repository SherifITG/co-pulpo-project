package com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.DivisionTable)
data class Division(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_division_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "line_id") val lineId: Long,
    @ColumnInfo(name = "type_id") val typeId: Long,
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   lineId: $lineId\n" +
                "   typeId: $typeId"
    }
}