package com.itgates.ultra.pulpo.cira.ui.utils

import android.location.Location
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.ultra.pulpo.cira.ui.activities.OfficeWorkActivity
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_startLocation
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_startDate
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.properties.Delegates

class OfficeWorkCurrentValues(private val activity: OfficeWorkActivity) {
    var userId by Delegates.notNull<Long>()

    companion object {
        // start values
        val officeWorkStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Office Work"))

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
        IdAndNameEntity(2L, SHIFT, EmbeddedEntity(ShiftEnum.AM_SHIFT.text)),
        IdAndNameEntity(1L, SHIFT, EmbeddedEntity(ShiftEnum.PM_SHIFT.text))
    )

    // current values
    var officeWorkCurrentValue: IdAndNameObj = officeWorkStartValue
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

    fun isOfficeWorkSelected(): Boolean = (officeWorkCurrentValue as IdAndNameEntity).tableId == OFFICE_WORK_TYPE
    fun isCommentCompleted(): Boolean = commentTextCurrentValue.isNotEmpty()
    fun isAllDataCompleted(): Boolean = isOfficeWorkSelected() && isCommentCompleted()

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is IdAndNameEntity -> {
                if (idAndNameObj.tableId == OFFICE_WORK_TYPE) {
                    println("lll ${idAndNameObj.id}, ${idAndNameObj.embedded.name}")
                    officeWorkCurrentValue = idAndNameObj
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