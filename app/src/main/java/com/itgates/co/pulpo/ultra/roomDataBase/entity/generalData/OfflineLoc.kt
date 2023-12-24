package com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.itgates.co.pulpo.ultra.BuildConfig
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.Utilities
import java.util.*

@Entity(tableName = TablesNames.OfflineLocTable)
data class OfflineLoc(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "online_id") var onlineId: Long,
    @ColumnInfo(name = "message_id") var messageId: Int,
    @ColumnInfo(name = "user_id") var userIdf: Long,
    @ColumnInfo(name = "android_id") val androidId: String,
    @ColumnInfo(name = "device_name") val deviceName: String,
    @ColumnInfo(name = "app_version") val appVersion: String,
    @ColumnInfo(name = "ll") val ll: String,
    @ColumnInfo(name = "lg") val lg: String,
    @ColumnInfo(name = "is_synced") var isSynced: Boolean,
    @ColumnInfo(name = "created_on") val createdOn: String,
    @ColumnInfo(name = "repeat") val repeat: Long,
) {
    constructor(
        messageId: Int, userId: Long, androidId: String, ll: String, lg: String
    ) : this (
        0L, 0L, messageId, userId, androidId, Utilities.getDeviceName(),
        "${BuildConfig.BUILD_TYPE}.${BuildConfig.VERSION_NAME}",
        ll, lg, false,
        GlobalFormats.getFullDate(Locale.getDefault(), Date()), 1
    )

    override fun toString(): String {
        return "   id: $id\n" +
                "   onlineId: $onlineId\n" +
                "   messageId: $messageId\n" +
                "   userId: $userIdf\n" +
                "   androidId: $androidId\n" +
                "   deviceName: $deviceName\n" +
                "   appVersion: $appVersion\n" +
                "   ll: $ll\n" +
                "   lg: $lg\n" +
                "   isSynced: $isSynced\n" +
                "   createdOn: $createdOn\n" +
                "   repeat: $repeat" +
                " ...END_OF_OFFLINE_LOC..."
    }

    fun isEqual(other: OfflineLoc): Boolean {
        return this.messageId == other.messageId && this.userIdf == other.userIdf &&
                this.androidId == other.androidId && this.deviceName == other.deviceName &&
                this.appVersion == other.appVersion && this.ll == other.ll && this.lg == other.lg
    }
}