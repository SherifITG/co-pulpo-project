package com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.AccountColumns

@Entity(tableName = TablesNames.SlideTable)
data class Slide(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_slide_") override val embedded: EmbeddedEntity,
    @ColumnInfo("slide_type") val slideType: String,
    @ColumnInfo("file_path") val filePath: String,
    @ColumnInfo("presentation_id") val presentationId: Long,
    @ColumnInfo("thumbnail_id") val thumbnailId: Long,
    @ColumnInfo("thumbnail_path") val thumbnailPath: String,
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   slide_type: $slideType\n" +
                "   file_path: $filePath\n" +
                "   presentation_id: $presentationId\n" +
                "   thumbnail_id: $thumbnailId\n" +
                "   thumbnail_path: $thumbnailPath"
    }
}
