package com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.LineTable)
data class Line(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_line_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "unplanned_limit") val unplannedLimit: Int
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   unplannedLimit: $unplannedLimit"
    }
}