package com.itgates.ultra.pulpo.cira.ui.utils

import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.NewPlanEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.AccountType
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Class
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Division
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.AccountData
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.DoctorData
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.DoctorPlanningData
import com.itgates.ultra.pulpo.cira.ui.activities.PlanningActivity
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.streams.toList

class PlanningCurrentValues(private val activity: PlanningActivity) {
    var userId by Delegates.notNull<Long>()

    init {
        CoroutineManager.getScope().launch {
            userId = activity.getDataStoreService().getDataObjAsync(PreferenceKeys.USER_ID)
                .await().toLong()
        }
    }

    companion object {
        // start values
        val divisionStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Division"))
        val brickStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Brick"))
        val accTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Acc Type"))
        val classStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Class"))
    }

    var accountsDataListToShow: List<AccountData> = listOf()
    var accountsDataList: List<AccountData> = listOf()

    var doctorsDataList: List<DoctorPlanningData> = listOf()

    val amDoctorsDataList: ArrayList<DoctorPlanningData> = ArrayList()
    var amDoctorsDataListToShow: ArrayList<DoctorPlanningData> = ArrayList()
    val pmDoctorsDataList: ArrayList<DoctorPlanningData> = ArrayList()
    var pmDoctorsDataListToShow: ArrayList<DoctorPlanningData> = ArrayList()
    val otherDoctorsDataList: ArrayList<DoctorPlanningData> = ArrayList()
    var otherDoctorsDataListToShow: ArrayList<DoctorPlanningData> = ArrayList()

    private val doctorAccountMapping: ArrayList<Int> = ArrayList()

    var divisionsList: List<Division> = listOf()
    var bricksList: List<Brick> = listOf()
    var accountTypesList: List<AccountType> = listOf()
    var allAccountTypesList: List<AccountType> = listOf()
    var classesList: List<Class> = listOf()

    val selectedDoctors = ArrayList<DoctorPlanningData>()

    // current values
    var divisionCurrentValue: IdAndNameObj = divisionStartValue
    var brickCurrentValue: IdAndNameObj = brickStartValue
    var accTypeCurrentValue: IdAndNameObj = accTypeStartValue
    var classCurrentValue: IdAndNameObj = classStartValue

    var tapNavigatingFun = {}

    fun isDivisionSelected(): Boolean = divisionCurrentValue !is IdAndNameEntity
    fun isBrickSelected(): Boolean = brickCurrentValue !is IdAndNameEntity
    fun isAccTypeSelected(): Boolean = accTypeCurrentValue !is IdAndNameEntity
    fun isClassSelected(): Boolean = classCurrentValue !is IdAndNameEntity

    fun isAllDataReady(): Boolean {
        return isDivisionSelected() && isBrickSelected() && isAccTypeSelected() && isClassSelected()
    }

    fun getDoctorListsMap(list: List<DoctorPlanningData>): Map<String, ArrayList<DoctorPlanningData>> {
        val dataMap: Map<String, ArrayList<DoctorPlanningData>> = allAccountTypesList.associate { accType ->
            accType.table to ArrayList()
        }
        list.forEach {
            dataMap[it.doctor.table]?.add(it)
        }
        return dataMap
    }

    fun distributeDoctorsList() {
        doctorsDataList.forEach {
            when(it.catId) {
                1 -> {
                    pmDoctorsDataList.add(it)
                    pmDoctorsDataListToShow.add(it)
                }
                2 -> {
                    amDoctorsDataList.add(it)
                    amDoctorsDataListToShow.add(it)
                }
                3 -> {
                    otherDoctorsDataList.add(it)
                    otherDoctorsDataListToShow.add(it)
                }
            }
        }
        println("9999999999 ${pmDoctorsDataListToShow.size}")
        println("9999999999 ${amDoctorsDataListToShow.size}")
        println("9999999999 ${otherDoctorsDataListToShow.size}")
    }

    fun getDoctorAccount(doctorData: DoctorData): AccountData? {
        var accountData: AccountData? = null
        accountsDataList.forEach {
            if (it.account.id == doctorData.doctor.accountId) accountData = it
        }

        return accountData
    }

    fun getAccountTypeCategory(table: String): Int? {
        var accountTypeCategory: Int? = null
        allAccountTypesList.forEach {
            if (it.table == table) accountTypeCategory = it.catId
        }

        return accountTypeCategory
    }

    fun getDoctorAccountType(doctorData: DoctorPlanningData): AccountType? {
        var accountType: AccountType? = null
        allAccountTypesList.forEach {
            if (it.table == doctorData.doctor.table) accountType = it
        }

        return accountType
    }

    fun createNewPlanInstance(
        divisionId: Long, accountTypeId: Long, itemId: Long, itemDoctorId: Long, members: Long,
        visitDate: String, shift: Int, teamId: Long
    ): NewPlanEntity {
        println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        println("$divisionId $accountTypeId $itemId $itemDoctorId")
        println("$members $visitDate $shift $userId $teamId")
        return NewPlanEntity(
            divisionId, accountTypeId, itemId, itemDoctorId, members, visitDate, shift,
            userId, teamId, 0
        )
    }

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is Division -> {
                divisionCurrentValue = idAndNameObj

                val divIds = if (divisionCurrentValue.embedded.name == "All") {
                    divisionsList.stream().map { it.id }.toList()
                }
                else {
                    listOf(divisionCurrentValue.id)
                }

                activity.loadBrickData(divIds)

                brickCurrentValue = ActualCurrentValues.brickStartValue
                accTypeCurrentValue = ActualCurrentValues.accTypeStartValue
            }
            is Brick -> {
                brickCurrentValue = idAndNameObj

                val divIds = if (divisionCurrentValue.embedded.name == "All") {
                    divisionsList.stream().map { it.id }.toList()
                }
                else {
                    listOf(divisionCurrentValue.id)
                }
                val brickIds = if (brickCurrentValue.embedded.name == "All") {
                    bricksList.stream().map { it.id }.toList()
                }
                else {
                    listOf(brickCurrentValue.id)
                }

                activity.loadAccTypeData(divIds, brickIds)

                accTypeCurrentValue = ActualCurrentValues.accTypeStartValue
            }
            is AccountType -> {
                accTypeCurrentValue = idAndNameObj

                val divIds = if (divisionCurrentValue.embedded.name == "All") {
                    divisionsList.stream().map { it.id }.toList()
                }
                else {
                    listOf(divisionCurrentValue.id)
                }
                val brickIds = if (brickCurrentValue.embedded.name == "All") {
                    bricksList.stream().map { it.id }.toList()
                }
                else {
                    listOf(brickCurrentValue.id)
                }
                val accTypeTables = if (accTypeCurrentValue.embedded.name == "All") {
                    accountTypesList.stream().map { it.table }.toList()
                }
                else {
                    listOf((accTypeCurrentValue as AccountType).table)
                }

                activity.loadClassesData(divIds, brickIds, accTypeTables)
            }
            is Class -> {
                classCurrentValue = idAndNameObj
            }
        }
    }
}