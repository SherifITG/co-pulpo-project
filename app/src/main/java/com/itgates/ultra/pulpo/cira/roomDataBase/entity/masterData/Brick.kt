package com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames

@Entity(tableName = TablesNames.BrickTable)
data class Brick(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_brick_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "team_id") val teamId: String,
    @ColumnInfo(name = "ter_id") val terId: String
): IdAndNameObj(id, embedded)