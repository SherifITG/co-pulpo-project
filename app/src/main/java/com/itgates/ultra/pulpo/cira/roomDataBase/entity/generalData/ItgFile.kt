package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import java.io.File

@Entity(tableName = TablesNames.FileTable)
data class ItgFile(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_file_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "file") val file: File
): IdAndNameObj(id, embedded)