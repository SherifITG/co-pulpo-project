package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.OfflineLoc

data class UploadedOfflineLocModel(
    val id: Long,
    val offline_id: Long,
    val message_id: Int,
    val android_id: String,
    val device_name: String,
    val app_version: String,
    val ll: String,
    val lg: String,
    val created_on: String,
    val repeat: Long
) {
    constructor(offlineLoc: OfflineLoc): this (
        offlineLoc.onlineId, offlineLoc.id, offlineLoc.messageId, offlineLoc.androidId,
        offlineLoc.deviceName, offlineLoc.appVersion, offlineLoc.ll, offlineLoc.lg,
        offlineLoc.createdOn, offlineLoc.repeat
    )
}