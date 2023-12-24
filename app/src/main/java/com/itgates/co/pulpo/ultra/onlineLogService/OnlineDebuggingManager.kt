package com.itgates.co.pulpo.ultra.onlineLogService

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedOnlineLogModel
import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedOnlineLogsListModel
import java.util.Date

object OnlineDebuggingManager {
    private val debuggingList = HashMap<Long, String>()
    private var debuggingTop = 0L
    private var enabled = false
    private var disabledTime = Date().time - (2 * 60 * 1000)

    fun log(tag: String, message: String) {
        if (enabled) {
            debuggingList[debuggingTop] = "$tag: $message"
            debuggingTop++
        }
    }

    fun deleteLogs(ids: List<Long>) {
        ids.forEach { debuggingList.remove(it) }
    }

    fun getUploadedList(): UploadedOnlineLogsListModel {
        return UploadedOnlineLogsListModel(
            debuggingList.map {
                UploadedOnlineLogModel(it.key, it.value)
            }
        )
    }

    fun isReadyToEnable(): Boolean {
        return ((Date().time - disabledTime) > (90 * 1000))
    }

    fun enable() {
        if (isReadyToEnable()) enabled = true
    }

    fun disable() {
        enabled = false
        disabledTime = Date().time
    }

    val isEnabled: Boolean get() = enabled


    // ---------------------------------------------------------------------------------------------
    // Get the FirebaseAnalytics instance
    var firebaseAnalytics: FirebaseAnalytics? = null
    fun enableFirebaseAnalytics(context: Context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        firebaseAnalytics?.setAnalyticsCollectionEnabled(true)
    }

    fun sendFirebaseLog(id: String, message: String) {
        // Log a custom event
        val params = Bundle()
        params.putString(id, message)
        firebaseAnalytics?.logEvent("kill", params)
    }
}