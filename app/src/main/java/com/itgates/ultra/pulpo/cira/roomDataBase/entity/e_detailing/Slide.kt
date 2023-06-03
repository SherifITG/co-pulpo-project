package com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.AccountColumns

@Entity(tableName = TablesNames.SlideTable)
data class Slide(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_slide_") override val embedded: EmbeddedEntity,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("presentation_id") val presentationId: Long,
    @ColumnInfo("product_id") val productId: Long,
    @ColumnInfo("slide_type") val slideType: String,
    @ColumnInfo("file_path") val filePath: String,
    @ColumnInfo("structure") val structure: String,
): IdAndNameObj(id, embedded)
