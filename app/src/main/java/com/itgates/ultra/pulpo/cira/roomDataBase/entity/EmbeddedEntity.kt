package com.itgates.ultra.pulpo.cira.roomDataBase.entity

import androidx.room.ColumnInfo

data class EmbeddedEntity (
    @ColumnInfo(name = "name") val name: String,
)