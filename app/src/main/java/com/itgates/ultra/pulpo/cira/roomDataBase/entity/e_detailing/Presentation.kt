package com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.tablesEnums.AccountColumns

@Entity(tableName = TablesNames.PresentationTable)
data class Presentation(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_presentation_") override val embedded: EmbeddedEntity,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("active") val active: Int,
    @ColumnInfo("product_id") val productId: Long,
    @ColumnInfo("team_id") val teamId: Long,
    @ColumnInfo("product_name") val productName: String
): IdAndNameObj(id, embedded)
