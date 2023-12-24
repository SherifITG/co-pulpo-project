package com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.BrickColumns

@Entity(tableName = TablesNames.BrickTable)
data class Brick(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_brick_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = BrickColumns.LINE_ID) val lineId: String,
    @ColumnInfo(name = BrickColumns.TERRITORY_ID) val terId: String
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   lineId: $lineId\n" +
                "   terId: $terId"
    }
}