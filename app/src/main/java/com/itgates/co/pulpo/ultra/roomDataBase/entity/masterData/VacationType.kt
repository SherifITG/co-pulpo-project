package com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.tablesEnums.AccountTypeColumns

@Entity(tableName = TablesNames.VacationTypeTable)
data class VacationType(
    @PrimaryKey override val id: Long,
    @Embedded(prefix = "embedded_vacation_type_") override val embedded: EmbeddedEntity,
    @ColumnInfo(name = "is_attach_required") val isAttachRequired: Boolean
): IdAndNameObj(id, embedded) {
    override fun toString(): String {
        return "   id: $id\n" +
                "   name: ${embedded.name}\n" +
                "   isAttachRequired: isAttachRequired"
    }
}