package com.itgates.ultra.pulpo.cira.utilities

import android.location.Location
import com.itgates.ultra.pulpo.cira.locationPackage.LocationClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Globals {

    private val justUploadedActualIds = arrayListOf<Long>()

    private var lastTriggerTime: Date? = null
    private val actualEndEventTriggered = MutableStateFlow(false)
    var uploadingFlow: Flow<Boolean> = actualEndEventTriggered

    var trustedLocationInfo: LocationClient.LocationInfo? = null
    var actualVisitStartLocation: Location? = null
    var actualVisitStartDate: Date? = null

    fun triggerActualEndEvent() {
        synchronized(this) {
            if (lastTriggerTime == null || Date().time.minus(lastTriggerTime!!.time) > 10000) {
                actualEndEventTriggered.value = !actualEndEventTriggered.value
                lastTriggerTime = Date()
            }
        }
    }

    fun addActualIdToJustUploadedList(ids: List<Long>) {
        justUploadedActualIds.addAll(ids)

        Executors
            .newSingleThreadScheduledExecutor()
            .schedule({
                justUploadedActualIds.removeAll(ids.toSet())
            }, 2, TimeUnit.SECONDS)
    }

    fun isActualIdInJustUploadedList(id: Long): Boolean {
        return justUploadedActualIds.contains(id)
    }
}