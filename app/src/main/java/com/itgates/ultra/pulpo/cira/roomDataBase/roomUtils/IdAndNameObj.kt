package com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils

import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity

open class IdAndNameObj(
    open val id: Long,
    open val embedded: EmbeddedEntity,
//    val tableId: IdAndNameTablesNamesEnum = IdAndNameTablesNamesEnum.NULL
)