package com.itgates.co.pulpo.ultra.ui.utils.currentValuesViewModels

import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.CoroutineManager.closableLaunch
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.AccountType
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Brick
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Class
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Division
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameData
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.AccountData
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.DoctorData
import com.itgates.co.pulpo.ultra.ui.activities.AccountsReportActivity
import com.itgates.co.pulpo.ultra.utilities.Globals
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AccountCurrentValues(private val activity: AccountsReportActivity) {
    var userId by Delegates.notNull<Long>()

    init {
        CoroutineManager.getScope().closableLaunch {
            userId = activity.getDataStoreService().getDataStringObjAsync(PreferenceKeys.USER_ID)
                .await().toLong()
        }
    }

    var accountsDataListToShow: List<AccountData> = listOf()
    var accountsDataList: List<AccountData> = listOf()
    var doctorsDataList: List<DoctorData> = listOf()

    var divisionsList: List<IdAndNameData> = listOf()
    var bricksList: List<IdAndNameData> = listOf()
    var accountTypesList: List<IdAndNameData> = listOf()
    var classesList: List<IdAndNameData> = listOf()

    // current values
    var divisionCurrentValue: IdAndNameData = Globals.globalSelectOptions!!.divisionStartValue
    var brickCurrentValue: IdAndNameData = Globals.globalSelectOptions!!.brickStartValue
    var accTypeCurrentValue: IdAndNameData = Globals.globalSelectOptions!!.accTypeStartValue
    var classCurrentValue: IdAndNameData = Globals.globalSelectOptions!!.classStartValue

    fun isDivisionSelected(): Boolean = divisionCurrentValue.isSelected
    fun isBrickSelected(): Boolean = brickCurrentValue.isSelected
    fun isAccTypeSelected(): Boolean = accTypeCurrentValue.isSelected
    fun isClassSelected(): Boolean = classCurrentValue.isSelected

    fun isAllDataReady(): Boolean {
        return isDivisionSelected() && isBrickSelected() && isAccTypeSelected() && isClassSelected()
    }

    fun notifyChanges(idAndNameData: IdAndNameData) {
        when(idAndNameData.itemData) {
            is Division -> {
                divisionCurrentValue = idAndNameData

                val divIds = if (isNameEqualAll(divisionCurrentValue)) {
                    divisionsList.map { it.id }
                }
                else {
                    listOf(divisionCurrentValue.id)
                }

                activity.loadBrickData(divIds)

                brickCurrentValue = Globals.globalSelectOptions!!.brickStartValue
                accTypeCurrentValue = Globals.globalSelectOptions!!.accTypeStartValue
            }
            is Brick -> {
                brickCurrentValue = idAndNameData

                val divIds = if (isNameEqualAll(divisionCurrentValue)) {
                    divisionsList.map { it.id }
                }
                else {
                    listOf(divisionCurrentValue.id)
                }
                val brickIds = if (isNameEqualAll(brickCurrentValue)) {
                    bricksList.map { it.id }
                }
                else {
                    listOf(brickCurrentValue.id)
                }

                activity.loadAccTypeData(divIds, brickIds)

                accTypeCurrentValue = Globals.globalSelectOptions!!.accTypeStartValue
            }
            is AccountType -> {
                accTypeCurrentValue = idAndNameData

                val divIds = if (isNameEqualAll(divisionCurrentValue)) {
                    divisionsList.map { it.id }
                }
                else {
                    listOf(divisionCurrentValue.id)
                }
                val brickIds = if (isNameEqualAll(brickCurrentValue)) {
                    bricksList.map { it.id }
                }
                else {
                    listOf(brickCurrentValue.id)
                }
                val accTypeIds = if (isNameEqualAll(accTypeCurrentValue)) {
                    accountTypesList.map { it.id.toInt() }
                }
                else {
                    listOf(accTypeCurrentValue.id.toInt())
                }

                activity.loadClassesData(divIds, brickIds, accTypeIds)
            }
            is Class -> {
                classCurrentValue = idAndNameData
            }
        }
    }

    private fun isNameEqualAll(idAndNameData: IdAndNameData): Boolean {
        return idAndNameData.name == activity.getString(R.string.all_title)
    }
}