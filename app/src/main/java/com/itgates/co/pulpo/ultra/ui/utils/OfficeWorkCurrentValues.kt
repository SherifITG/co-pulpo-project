package com.itgates.co.pulpo.ultra.ui.utils

import android.location.Location
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.ui.activities.OfficeWorkActivity
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_startLocation
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_startDate
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.properties.Delegates

class OfficeWorkCurrentValues(private val activity: OfficeWorkActivity) {
    var userId by Delegates.notNull<Long>()

    companion object {
        // start values
        val officeWorkTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Office Work Type"), -2)

        // texts start values
        const val commentStartValue = ""
    }

    init {
        CoroutineManager.getScope().launch {
            userId = activity.getDataStoreService().getDataObjAsync(PreferenceKeys.USER_ID)
                .await().toLong()
        }
    }

//    var settingMap: Map<String, Int> = mapOf()
    var officeWorkList: List<IdAndNameEntity> = listOf()
    val shiftList: List<IdAndNameEntity> = listOf(
        IdAndNameEntity(ShiftEnum.AM_SHIFT.index.toLong(), SHIFT, EmbeddedEntity(ShiftEnum.AM_SHIFT.text), -2),
        IdAndNameEntity(ShiftEnum.PM_SHIFT.index.toLong(), SHIFT, EmbeddedEntity(ShiftEnum.PM_SHIFT.text), -2),
        IdAndNameEntity(ShiftEnum.FULL_DAY.index.toLong(), SHIFT, EmbeddedEntity(ShiftEnum.FULL_DAY.text), -2)
    )

    // current values
    var officeWorkTypeCurrentValue: IdAndNameObj = officeWorkTypeStartValue
    var shiftCurrentValue: IdAndNameObj = shiftList[0]
    var commentTextCurrentValue: String = commentStartValue

    // error values
    var officeWorkErrorValue = false
    var textCommentError = false

    // dates and times and locations
    var endDate: Date? = null
    var startDate: Date? = actualActivity_startDate
    var endLocation: Location? = null
    var startLocation: Location? = actualActivity_startLocation

    fun isOfficeWorkSelected(): Boolean = (officeWorkTypeCurrentValue as IdAndNameEntity).tableId == OFFICE_WORK_TYPE
    fun isCommentCompleted(): Boolean = commentTextCurrentValue.isNotEmpty()
    fun isAllDataCompleted(): Boolean = isOfficeWorkSelected() && isCommentCompleted()

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is IdAndNameEntity -> {
                if (idAndNameObj.tableId == OFFICE_WORK_TYPE) {
                    println("lll ${idAndNameObj.id}, ${idAndNameObj.embedded.name}")
                    officeWorkTypeCurrentValue = idAndNameObj
                }
                else if (idAndNameObj.tableId == SHIFT) {
                    shiftCurrentValue = idAndNameObj
                }
            }
        }
    }

    fun notifyTextChanges(newText: String) {
        commentTextCurrentValue = newText
    }
}