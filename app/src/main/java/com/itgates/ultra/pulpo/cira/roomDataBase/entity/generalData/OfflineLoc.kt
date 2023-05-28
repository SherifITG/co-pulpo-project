package com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import java.io.File
import java.util.*

@Entity(tableName = TablesNames.OfflineLocTable)
data class OfflineLoc(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo(name = "message_id") var messageId: Int,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "ll") val ll: String,
    @ColumnInfo(name = "lg") val lg: String,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "created_on") val createdOn: String,
) {
    constructor(
        messageId: Int, userId: Long, ip: String, ll: String, lg: String
    ) : this (
        0L, 0L, messageId, userId, ip, ll, lg, false,
        GlobalFormats.getFullDate(Locale.getDefault(), Date())
    )
}