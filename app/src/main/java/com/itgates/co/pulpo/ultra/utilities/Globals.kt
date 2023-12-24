package com.itgates.co.pulpo.ultra.utilities

import android.location.Location
import com.itgates.co.pulpo.ultra.locationPackage.LocationClient
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Setting
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.RelationalLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Globals {

    private val justUploadedActualIds = arrayListOf<Long>()
    private val justUploadedOWIds = arrayListOf<Long>()
    private val justUploadedVacationIds = arrayListOf<Long>()
    private val justUploadedNewPlanIds = arrayListOf<Long>()
    private val justUploadedOfflineLogIds = arrayListOf<Long>()
    private val justUploadedOfflineLocationIds = arrayListOf<Long>()
//    private val justUploadedOnlineDebugIds = arrayListOf<Long>()

    private var actualLastTriggerTime: Date? = null
    private var owLastTriggerTime: Date? = null
    private var vacationLastTriggerTime: Date? = null
    private var newPlanLastTriggerTime: Date? = null
    private var offlineLogsLastTriggerTime: Date? = null
    private var offlineLocationsLastTriggerTime: Date? = null
//    private var onlineDebugsLastTriggerTime: Date? = null
    private val actualEndEventTriggered = MutableStateFlow(false)
    private val owEndEventTriggered = MutableStateFlow(false)
    private val vacationEndEventTriggered = MutableStateFlow(false)
    private val newPlanEndEventTriggered = MutableStateFlow(false)
    private val offlineLogsEventTriggered = MutableStateFlow(false)
    private val offlineLocationsEventTriggered = MutableStateFlow(false)
//    private val onlineDebugsEventTriggered = MutableStateFlow(false)
    var actualUploadingFlow: Flow<Boolean> = actualEndEventTriggered
    var owUploadingFlow: Flow<Boolean> = owEndEventTriggered
    var vacationUploadingFlow: Flow<Boolean> = vacationEndEventTriggered
    var newPlanUploadingFlow: Flow<Boolean> = newPlanEndEventTriggered
    var offlineLogsUploadingFlow: Flow<Boolean> = offlineLogsEventTriggered
    var offlineLocationsUploadingFlow: Flow<Boolean> = offlineLocationsEventTriggered
//    var onlineDebugsUploadingFlow: Flow<Boolean> = onlineDebugsEventTriggered

    var trustedLocationInfo: LocationClient.LocationInfo? = null
    var actualVisitStartLocation: Location? = null
    var actualVisitStartDate: Date? = null

    var seasonSetting = Setting(
        0L,
        EmbeddedEntity(SettingEnum.SEASON.text),
        "0",
    )
    var timeZoneSetting = Setting(
        0L,
        EmbeddedEntity(SettingEnum.TIME_ZONE.text),
        "Africa/Cairo,Asia/Riyadh",
    )

    var lineData: List<RelationalLine> = listOf()

    fun triggerActualEndEvent() {
        synchronized(this) {
            if (actualLastTriggerTime == null || Date().time.minus(actualLastTriggerTime!!.time) > 10000) {
                actualEndEventTriggered.value = !actualEndEventTriggered.value
                actualLastTriggerTime = Date()
            }
        }
    }

    fun triggerOWEndEvent() {
        synchronized(this) {
            if (owLastTriggerTime == null || Date().time.minus(owLastTriggerTime!!.time) > 10000) {
                owEndEventTriggered.value = !owEndEventTriggered.value
                owLastTriggerTime = Date()
            }
        }
    }

    fun triggerVacationEndEvent() {
        synchronized(this) {
            if (vacationLastTriggerTime == null || Date().time.minus(vacationLastTriggerTime!!.time) > 10000) {
                vacationEndEventTriggered.value = !vacationEndEventTriggered.value
                vacationLastTriggerTime = Date()
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

    fun triggerOfflineLogsEvent() {
        synchronized(this) {
            if (offlineLogsLastTriggerTime == null || Date().time.minus(offlineLogsLastTriggerTime!!.time) > 10000) {
                offlineLogsEventTriggered.value = !offlineLogsEventTriggered.value
                offlineLogsLastTriggerTime = Date()
            }
        }
    }

    fun triggerOfflineLocationsEvent() {
        synchronized(this) {
            if (offlineLocationsLastTriggerTime == null || Date().time.minus(offlineLocationsLastTriggerTime!!.time) > 10000) {
                offlineLocationsEventTriggered.value = !offlineLocationsEventTriggered.value
                offlineLocationsLastTriggerTime = Date()
            }
        }
    }

//    fun triggerOnlineDebugsEvent() {
//        synchronized(this) {
//            if (onlineDebugsLastTriggerTime == null || Date().time.minus(onlineDebugsLastTriggerTime!!.time) > 10000) {
//                onlineDebugsEventTriggered.value = !onlineDebugsEventTriggered.value
//                onlineDebugsLastTriggerTime = Date()
//            }
//        }
//    }

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

    fun addOWIdToJustUploadedList(ids: List<Long>) {
        justUploadedOWIds.addAll(ids)

        Executors
            .newSingleThreadScheduledExecutor()
            .schedule({
                justUploadedOWIds.removeAll(ids.toSet())
            }, 2, TimeUnit.SECONDS)
    }

    fun isOWIdInJustUploadedList(id: Long): Boolean {
        return justUploadedOWIds.contains(id)
    }

    fun addVacationIdToJustUploadedList(ids: List<Long>) {
        justUploadedVacationIds.addAll(ids)

        Executors
            .newSingleThreadScheduledExecutor()
            .schedule({
                justUploadedVacationIds.removeAll(ids.toSet())
            }, 2, TimeUnit.SECONDS)
    }

    fun isVacationIdInJustUploadedList(id: Long): Boolean {
        return justUploadedVacationIds.contains(id)
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

    fun addOfflineLogIdToJustUploadedList(ids: List<Long>) {
        justUploadedOfflineLogIds.addAll(ids)

        Executors
            .newSingleThreadScheduledExecutor()
            .schedule({
                justUploadedOfflineLogIds.removeAll(ids.toSet())
            }, 2, TimeUnit.SECONDS)
    }

    fun isOfflineLogIdInJustUploadedList(id: Long): Boolean {
        return justUploadedOfflineLogIds.contains(id)
    }

    fun addOfflineLocIdToJustUploadedList(ids: List<Long>) {
        justUploadedOfflineLocationIds.addAll(ids)

        Executors
            .newSingleThreadScheduledExecutor()
            .schedule({
                justUploadedOfflineLocationIds.removeAll(ids.toSet())
            }, 2, TimeUnit.SECONDS)
    }

    fun isOfflineLocIdInJustUploadedList(id: Long): Boolean {
        return justUploadedOfflineLocationIds.contains(id)
    }

//    fun addOnlineLogIdToJustUploadedList(ids: List<Long>) {
//        justUploadedOnlineDebugIds.addAll(ids)
//
//        Executors
//            .newSingleThreadScheduledExecutor()
//            .schedule({
//                justUploadedOnlineDebugIds.removeAll(ids.toSet())
//            }, 2, TimeUnit.SECONDS)
//    }
//
//    fun isOnlineLogIdInJustUploadedList(id: Long): Boolean {
//        return justUploadedOnlineDebugIds.contains(id)
//    }

    fun isLineUnplannedLimitComplete(lineId: Long): Boolean {
        lineData.forEach {
            if (it.line.id == lineId) return it.isLineUnplannedLimitComplete()
        }
        return false
    }

    fun isAllLinesUnplannedLimitComplete(): Boolean {
        var allLinesComplete = lineData.isNotEmpty()
        lineData.forEach {
            allLinesComplete = allLinesComplete && it.isLineUnplannedLimitComplete()
        }
        return allLinesComplete
    }

    fun lineNameByDivId(lineId: Long): String {
        lineData.forEach {
            if (it.line.id == lineId) return it.line.embedded.name
        }
        return "NO_LINE"
    }

    fun lineUnplannedLimitByDivId(lineId: Long): Int {
        lineData.forEach {
            if (it.line.id == lineId) return it.line.unplannedLimit
        }
        return 0
    }
}