package com.itgates.co.pulpo.ultra.roomDataBase.roomUtils

import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity

open class IdAndNameObj(
    open val id: Long,
    open val embedded: EmbeddedEntity,
//    val tableId: IdAndNameTablesNamesEnum = IdAndNameTablesNamesEnum.NULL
)