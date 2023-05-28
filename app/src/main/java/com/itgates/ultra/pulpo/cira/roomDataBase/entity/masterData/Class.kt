package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.ClassTable)
data class Class(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_class_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "cat_id") val categoryId: Long,
): IdAndNameObj(id, embedded)