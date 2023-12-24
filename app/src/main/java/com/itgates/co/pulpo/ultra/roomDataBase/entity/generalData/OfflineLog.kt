package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.BuildConfig
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.Utilities
import java.io.File
import java.util.*

@Entity(tableName = TablesNames.OfflineLogTable)
data class OfflineLog(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo(name = "message_id") var messageId: Int,
    @ColumnInfo(name = "user_id") var userId: Long,
    @ColumnInfo(name = "android_id") val androidId: String,
    @ColumnInfo(name = "device_name") val deviceName: String,
    @ColumnInfo(name = "app_version") val appVersion: String,
    @ColumnInfo(name = "form_id") val formId: Int,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "created_on") val createdOn: String,
) {
    constructor(
        messageId: Int, userId: Long, androidId: String, formId: Int
    ) : this (
        0L, 0L, messageId, userId, androidId, Utilities.getDeviceName(),
        "${BuildConfig.BUILD_TYPE}.${BuildConfig.VERSION_NAME}", formId, false,
        GlobalFormats.getFullDate(Locale.getDefault(), Date())
    )

    override fun toString(): String {
        return "   id: $id\n" +
                "   onlineId: $onlineId\n" +
                "   messageId: $messageId\n" +
                "   userId: $userId\n" +
                "   androidId: $androidId\n" +
                "   deviceName: $deviceName\n" +
                "   appVersion: $appVersion\n" +
                "   formId: $formId\n" +
                "   isSynced: $isSynced\n" +
                "   createdOn: $createdOn" +
                " ...END_OF_OFFLINE_LOG..."
    }
}