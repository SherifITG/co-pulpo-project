package com.itgates.co.pulpo.ultra.network.models.requestModels

import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.OfflineLog

data class UploadedOfflineLogModel(
    val id: Long,
    val offline_id: Long,
    val message_id: Int,
    val android_id: String,
    val device_name: String,
    val app_version: String,
    val form_id: Int,
    val created_on: String
) {
    constructor(offlineLog: OfflineLog): this (
        offlineLog.onlineId, offlineLog.id, offlineLog.messageId, offlineLog.androidId,
        offlineLog.deviceName, offlineLog.appVersion, offlineLog.formId, offlineLog.createdOn
    )
}