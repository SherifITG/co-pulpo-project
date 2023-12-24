package com.itgates.co.pulpo.ultra.ui.utils

import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.NewPlanEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.AccountType
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Brick
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Division
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.AccountData
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.DoctorPlanningData
import com.itgates.co.pulpo.ultra.ui.activities.PlanningActivity
import com.itgates.co.pulpo.ultra.utilities.Utilities
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
        val divisionStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Division"), -2)
        val brickStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Brick"), -2)
        val accTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Acc Type"), -2)
        val classStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Class"), -2)

        val coverageList: List<IdAndNameEntity> = listOf(
            IdAndNameEntity(0L, COVERAGE, EmbeddedEntity("All"), -2),
            IdAndNameEntity(1L, PRODUCT_SAMPLE, EmbeddedEntity("Covered"), -2),
            IdAndNameEntity(2L, PRODUCT_SAMPLE, EmbeddedEntity("Partially Covered"), -2),
            IdAndNameEntity(3L, PRODUCT_SAMPLE, EmbeddedEntity("Uncovered"), -2),
            IdAndNameEntity(4L, PRODUCT_SAMPLE, EmbeddedEntity("No Coverage"), -2)
        )
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
    var classesList: List<IdAndNameEntity> = listOf()

    val selectedDoctors = ArrayList<DoctorPlanningData>()
    var selectedDoctorsStatus = HashMap<Int, Long>()

    // current values
    var divisionCurrentValue: IdAndNameObj = divisionStartValue
    var brickCurrentValue: IdAndNameObj = brickStartValue
    var accTypeCurrentValue: IdAndNameObj = accTypeStartValue
    var classCurrentValue: IdAndNameObj = classStartValue
    var coverageCurrentValue: IdAndNameObj = coverageList[0]

    var tapNavigatingFun = {
        // any message
        Utilities.createCustomToast(activity, "Some error, You can refresh the activity our app")
    }

    fun isDivisionSelected(): Boolean = divisionCurrentValue !is IdAndNameEntity
    fun isBrickSelected(): Boolean = brickCurrentValue !is IdAndNameEntity
    fun isAccTypeSelected(): Boolean = accTypeCurrentValue !is IdAndNameEntity
    fun isClassSelected(): Boolean = classCurrentValue !is IdAndNameEntity

    fun isAllDataReady(): Boolean {
        return isDivisionSelected() && isBrickSelected() && isAccTypeSelected() && isClassSelected()
    }

    fun getDoctorListsMap(list: List<DoctorPlanningData>): Map<String, ArrayList<DoctorPlanningData>> {
        val dataMap: Map<String, ArrayList<DoctorPlanningData>> = allAccountTypesList.associate { accType ->
            accType.embedded.name to ArrayList()
        }
        list.forEach {
            dataMap[it.doctor.embedded.name]?.add(it)
        }
        return dataMap
    }

    fun distributeDoctorsList() {
        doctorsDataList.forEach {
            when(it.shiftId) {
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
    }

    fun createNewPlanInstance(
        divisionId: Long, accountTypeId: Long, itemId: Long, itemDoctorId: Long, members: Long,
        visitDate: String, shift: Int, lineId: Long
    ): NewPlanEntity {
        println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        println("$divisionId $accountTypeId $itemId $itemDoctorId")
        println("$members $visitDate $shift $userId $lineId")
        return NewPlanEntity(
            divisionId, accountTypeId, itemId, itemDoctorId, members, visitDate, shift,
            userId, lineId, 0
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
                val accTypeIds = if (accTypeCurrentValue.embedded.name == "All") {
                    accountTypesList.stream().map { it.id.toInt() }.toList()
                }
                else {
                    listOf(accTypeCurrentValue.id.toInt())
                }

                activity.loadClassesData(divIds, brickIds, accTypeIds)
            }
            is IdAndNameEntity -> {
                classCurrentValue = idAndNameObj
            }
        }
    }
}