package com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.AccountColumns

@Entity(tableName = TablesNames.PresentationTable)
data class Presentation(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_presentation_") override val embedded: EmbeddedEntity,
    @ColumnInfo("product_id") val productId: Long,
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   product_id: $productId"
    }
}
