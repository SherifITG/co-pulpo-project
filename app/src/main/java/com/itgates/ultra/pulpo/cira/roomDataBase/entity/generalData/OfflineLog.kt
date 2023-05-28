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

@Entity(tableName = TablesNames.OfflineLogTable)
data class OfflineLog(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo(name = "message_id") var messageId: Int,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "action_id") val actionId: Int,
    @ColumnInfo(name = "form_id") val formId: Int,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "created_on") val createdOn: String,
) {
    constructor(
        messageId: Int, userId: Long, ip: String, actionId: Int, formId: Int
    ) : this (
        0L, 0L, messageId, userId, ip, actionId, formId, false,
        GlobalFormats.getFullDate(Locale.getDefault(), Date())
    )
}