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
    private val justUploadedNewPlanIds = arrayListOf<Long>()

    private var actualLastTriggerTime: Date? = null
    private var newPlanLastTriggerTime: Date? = null
    private val actualEndEventTriggered = MutableStateFlow(false)
    private val newPlanEndEventTriggered = MutableStateFlow(false)
    var actualUploadingFlow: Flow<Boolean> = actualEndEventTriggered
    var newPlanUploadingFlow: Flow<Boolean> = newPlanEndEventTriggered

    var trustedLocationInfo: LocationClient.LocationInfo? = null
    var actualVisitStartLocation: Location? = null
    var actualVisitStartDate: Date? = null

    fun triggerActualEndEvent() {
        synchronized(this) {
            if (actualLastTriggerTime == null || Date().time.minus(actualLastTriggerTime!!.time) > 10000) {
                actualEndEventTriggered.value = !actualEndEventTriggered.value
                actualLastTriggerTime = Date()
            }
        }
    }

    fun triggerNewPlanEndEvent() {
        synchronized(this) {
            if (newPlanLastTriggerTime == null || Date().time.minus(newPlanLastTriggerTime!!.time) > 10000) {
                newPlanEndEventTriggered.value = !newPlanEndEventTriggered.value
                newPlanLastTriggerTime = Date()
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

    fun addNewPlanIdToJustUploadedList(ids: List<Long>) {
        justUploadedNewPlanIds.addAll(ids)

        Executors
            .newSingleThreadScheduledExecutor()
            .schedule({
                justUploadedNewPlanIds.removeAll(ids.toSet())
            }, 2, TimeUnit.SECONDS)
    }

    fun isNewPlanIdInJustUploadedList(id: Long): Boolean {
        return justUploadedNewPlanIds.contains(id)
    }
}