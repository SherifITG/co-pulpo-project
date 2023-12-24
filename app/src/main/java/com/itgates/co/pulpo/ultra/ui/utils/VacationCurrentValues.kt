package com.itgates.co.pulpo.ultra.ui.utils

import android.net.Uri
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.VacationType
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.DurationEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.ui.activities.VacationActivity
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import kotlinx.coroutines.launch
import java.util.*
import kotlin.properties.Delegates

class VacationCurrentValues(private val activity: VacationActivity) {
    var userId by Delegates.notNull<Long>()

    companion object {
        // start values
        val vacationTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Vacation Type"), -2)

        // texts start values
        const val noteStartValue = ""
    }

    init {
        CoroutineManager.getScope().launch {
            userId = activity.getDataStoreService().getDataObjAsync(PreferenceKeys.USER_ID)
                .await().toLong()
        }
    }

//    var settingMap: Map<String, Int> = mapOf()
    var vacationTypeList: List<VacationType> = listOf()
    val durationList: List<IdAndNameEntity> = listOf(
        IdAndNameEntity(DurationEnum.FULL_DAY.index.toLong(), DURATION, EmbeddedEntity(DurationEnum.FULL_DAY.text), -2),
        IdAndNameEntity(DurationEnum.HALF_DAY.index.toLong(), DURATION, EmbeddedEntity(DurationEnum.HALF_DAY.text), -2)
    )
    val shiftList: List<IdAndNameEntity> = listOf(
        IdAndNameEntity(ShiftEnum.AM_SHIFT.index.toLong(), SHIFT, EmbeddedEntity(ShiftEnum.AM_SHIFT.text), -2),
        IdAndNameEntity(ShiftEnum.PM_SHIFT.index.toLong(), SHIFT, EmbeddedEntity(ShiftEnum.PM_SHIFT.text), -2)
    )

    // current values
    var vacationTypeCurrentValue: IdAndNameObj = vacationTypeStartValue
    var durationCurrentValue: IdAndNameObj = durationList[0]
    var shiftCurrentValue: IdAndNameObj = shiftList[0]
    var noteTextCurrentValue: String = noteStartValue

    val attachmentsUris = arrayListOf<Uri>()

    var dateFrom = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
    var dateTo = GlobalFormats.getDashedDate(Locale.getDefault(), Date())

    // error values
    var vacationTypeErrorValue = false
    var textNoteError = false

    fun isVacationTypeSelected(): Boolean = vacationTypeCurrentValue is VacationType
    fun isNoteCompleted(): Boolean = noteTextCurrentValue.isNotEmpty()
    fun isFullDayDuration(): Boolean = durationCurrentValue.id == DurationEnum.FULL_DAY.index.toLong()
    fun isAllDataCompleted(): Boolean {
        return isVacationTypeSelected()
                && isNoteCompleted()
                && (!(vacationTypeCurrentValue as VacationType).isAttachRequired || attachmentsUris.isNotEmpty())
    }

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is VacationType -> {
                vacationTypeCurrentValue = idAndNameObj
            }
            is IdAndNameEntity -> {
                if (idAndNameObj.tableId == DURATION) {
                    durationCurrentValue = idAndNameObj
                    if (idAndNameObj.id.toInt() == DurationEnum.HALF_DAY.index.toInt()) {
                        dateTo = dateFrom
                    }
                }
                else if (idAndNameObj.tableId == SHIFT) {
                    shiftCurrentValue = idAndNameObj
                }
            }
        }
    }

    fun notifyTextChanges(newText: String) {
        noteTextCurrentValue = newText
    }
}