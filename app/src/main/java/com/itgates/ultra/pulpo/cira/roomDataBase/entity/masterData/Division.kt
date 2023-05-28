package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.DivisionTable)
data class Division(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_division_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "team_id") val teamId: String?,
    @ColumnInfo(name = "parent_id") val parentId: String?,
    @ColumnInfo(name = "type_id") val typeId: String?,
    @ColumnInfo(name = "date_from") val dateFrom: String?,
    @ColumnInfo(name = "date_to") val dateTo: String?,
    @ColumnInfo(name = "sorting") val sorting: String?,
    @ColumnInfo(name = "related_id") val relatedId: String?
): IdAndNameObj(id, embedded)