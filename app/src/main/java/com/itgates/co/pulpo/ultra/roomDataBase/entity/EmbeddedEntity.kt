package com.itgates.co.pulpo.ultra.roomDataBase.entity

import androidx.room.ColumnInfo

data class EmbeddedEntity (
    @ColumnInfo(name = "name") val name: String,
)