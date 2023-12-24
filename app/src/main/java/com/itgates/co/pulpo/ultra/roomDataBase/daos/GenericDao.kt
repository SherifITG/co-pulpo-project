package com.itgates.co.pulpo.ultra.roomDataBase.daos

import androidx.room.*
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.co.pulpo.ultra.roomDataBase.converters.RoomPathListModule
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.DurationEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.queries.MainRoomQuery
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.queries.RelationalRoomQuery
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.*
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import java.util.*

interface GenericDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(row: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rows: List<T>)

    @Update
    suspend fun update(row: T)

    @Update
    suspend fun updateAll(rows: List<T>)

    @Delete
    suspend fun delete(entity: T)

}

@Dao
interface AccountTypeDao : GenericDao<AccountType> {
    @Query("SELECT * FROM ${TablesNames.AccountTypeTable}")
    suspend fun findAll(): List<AccountType>

    @Query(MainRoomQuery.accountTypesQuery)
    suspend fun loadActualAccountTypes(divIds: List<Long>, brickIds: List<Long>): List<AccountType>

    @Query(MainRoomQuery.accountTypesQueryWithoutBrick)
    suspend fun loadActualAccountTypesWithoutBrick(divIds: List<Long>): List<AccountType>

    @Query("SELECT * FROM ${TablesNames.AccountTypeTable}")
    suspend fun loadAllAccountTypes(): List<AccountType>

    @Query("DELETE FROM ${TablesNames.AccountTypeTable}")
    suspend fun deleteAll()
}

@Dao
interface LineDao : GenericDao<Line> {
    @Query("SELECT * FROM ${TablesNames.LineTable}")
    suspend fun findAll(): List<Line>

    @Query(RelationalRoomQuery.relationalLinesListQuery)
    suspend fun loadRelationalLines(today: String): List<RelationalLine>

    @Query("DELETE FROM ${TablesNames.LineTable}")
    suspend fun deleteAll()
}

@Dao
interface BrickDao : GenericDao<Brick> {
    @Query("SELECT * FROM ${TablesNames.BrickTable}")
    suspend fun findAll(): List<Brick>

    @Query(MainRoomQuery.bricksQuery)
    suspend fun loadActualBricks(divIds: List<Long>): List<Brick>

    @Query("DELETE FROM ${TablesNames.BrickTable}")
    suspend fun deleteAll()
}

@Dao
interface DivisionDao : GenericDao<Division> {
    @Query(MainRoomQuery.divisionsQuery)
    suspend fun findAll(): List<Division>

    @Query("DELETE FROM ${TablesNames.DivisionTable}")
    suspend fun deleteAll()
}

@Dao
interface SettingDao : GenericDao<Setting> {
    @Query("SELECT * FROM ${TablesNames.SettingTable}")
    suspend fun findAll(): List<Setting>

    @Query(MainRoomQuery.settingByNamesListQuery)
    suspend fun loadAll(names: List<String>): List<Setting>

    @Query("DELETE FROM ${TablesNames.SettingTable}")
    suspend fun deleteAll()
}

@Dao
interface VacationTypeDao : GenericDao<VacationType> {
    @Query("SELECT * FROM ${TablesNames.VacationTypeTable}")
    suspend fun findAll(): List<VacationType>

//    @Query(MainRoomQuery.settingByNamesListQuery)
//    suspend fun loadAll(names: List<String>): List<Setting>

    @Query("DELETE FROM ${TablesNames.VacationTypeTable}")
    suspend fun deleteAll()
}

@Dao
interface IdAndNameDao : GenericDao<IdAndNameEntity> {
    @Query("SELECT * FROM ${TablesNames.IdAndNameTable}")
    suspend fun findAll(): List<IdAndNameEntity>

    @Query(MainRoomQuery.idAndNameObjectsQuery)
    suspend fun loadAllByTAble(tableId: IdAndNameTablesNamesEnum): List<IdAndNameEntity>

    @Query(MainRoomQuery.idAndNameObjectsByTablesListQuery)
    suspend fun loadByLineAndByTablesList(tableIds: List<IdAndNameTablesNamesEnum>, lineId: Long): List<IdAndNameEntity>

    @Query(MainRoomQuery.idAndNameObjectsByTablesListWithoutLineIdQuery)
    suspend fun loadAllByTablesList(tableIds: List<IdAndNameTablesNamesEnum>): List<IdAndNameEntity>

    @Query(RelationalRoomQuery.classesQuery)
    suspend fun loadClassesByIdList(
        tableId: IdAndNameTablesNamesEnum,
        divIds: List<Long>,
        brickIds: List<Long>,
        accTypeIds: List<Int>
    ): List<IdAndNameEntity>

    @Query("DELETE FROM ${TablesNames.IdAndNameTable}")
    suspend fun deleteAll()
}

@Dao
interface AccountDao : GenericDao<Account> {
    @Query("SELECT * FROM ${TablesNames.AccountTable}")
    suspend fun findAll(): List<Account>

    @Query(MainRoomQuery.accountsQuery)
    suspend fun loadActualAccounts(lineId: Long, divId: Long, brickId: Long, accTypeId: Int): List<Account>

    @Query(MainRoomQuery.accountsQueryWithoutBrick)
    suspend fun loadActualAccountsWithoutBrick(lineId: Long, divId: Long, accTypeId: Int): List<Account>

    @Query(RelationalRoomQuery.accountsListQuery)
    suspend fun loadAllAccountReportData(
        classId: IdAndNameTablesNamesEnum = IdAndNameTablesNamesEnum.CLASS
    ): List<AccountData>

    @Query(MainRoomQuery.updateAccountLocationQuery)
    suspend fun updateAccountLocation(
        llFirst: String, lgFirst: String,
        id: Long, accTypeId: Int, lineId: Long, divId: Long, brickId: Long
    )

    @Query("DELETE FROM ${TablesNames.AccountTable}")
    suspend fun deleteAll()
}

@Dao
interface DoctorDao : GenericDao<Doctor> {
    @Query("SELECT * FROM ${TablesNames.DoctorTable}")
    suspend fun findAll(): List<Doctor>

    @Query(MainRoomQuery.doctorsQuery)
    suspend fun loadActualDoctors(lineId: Long, accountId: Long, accountTypeId: Int): List<Doctor>

    @Query(RelationalRoomQuery.doctorsListQuery)
    suspend fun loadAllDoctorReportData(tableId: IdAndNameTablesNamesEnum): List<DoctorData>

    @Query(RelationalRoomQuery.doctorsPlanningListQuery)
    suspend fun loadAllDoctorPlanningData(
        specId: IdAndNameTablesNamesEnum = IdAndNameTablesNamesEnum.SPECIALITY,
        classId: IdAndNameTablesNamesEnum = IdAndNameTablesNamesEnum.CLASS,
        currentDateString: String = GlobalFormats.getDashedDateWithoutDayForDB(Locale.getDefault(), Date())
    ): List<DoctorPlanningData>

    @Query("DELETE FROM ${TablesNames.DoctorTable}")
    suspend fun deleteAll()
}

@Dao
interface PresentationDao : GenericDao<Presentation> {
    @Query("SELECT * FROM ${TablesNames.PresentationTable}")
    suspend fun findAll(): List<Presentation>

    @Query("SELECT * FROM ${TablesNames.PresentationTable}")
    suspend fun loadPresentations(): List<Presentation>

    @Query("DELETE FROM ${TablesNames.PresentationTable}")
    suspend fun deleteAll()
}

@Dao
interface SlideDao : GenericDao<Slide> {
    @Query("SELECT * FROM ${TablesNames.SlideTable}")
    suspend fun findAll(): List<Slide>

    @Query("SELECT * FROM ${TablesNames.SlideTable} WHERE presentation_id = :presentationId")
    suspend fun loadByPresentationId(presentationId: Long): List<Slide>

    @Query("DELETE FROM ${TablesNames.SlideTable}")
    suspend fun deleteAll()
}

@Dao
interface PlannedVisitDao : GenericDao<PlannedVisit> {
    @Query("SELECT * FROM ${TablesNames.PlannedVisitTable}")
    suspend fun findAll(): List<PlannedVisit>

    @Query(RelationalRoomQuery.plannedVisitsListQuery)
    suspend fun loadRelationalPlannedVisits(): List<RelationalPlannedVisit>

    @Query(RelationalRoomQuery.todayPlannedVisitsListQuery)
    suspend fun loadTodayRelationalPlannedVisits(today: String): List<RelationalPlannedVisit>

    @Query(RelationalRoomQuery.todayPlannedVisitsAndOWCountQuery)
    suspend fun loadTodayPlannedVisitsAndOWCount(today: String): Long

    @Query("UPDATE ${TablesNames.PlannedVisitTable} SET is_done = :isDone WHERE id = :doneId")
    suspend fun markAsDone(isDone: Boolean, doneId: Long)

    @Query("DELETE FROM ${TablesNames.PlannedVisitTable}")
    suspend fun deleteAll()

}

@Dao
interface PlannedOWDao : GenericDao<PlannedOW> {
    @Query("SELECT * FROM ${TablesNames.PlannedOWTable}")
    suspend fun findAll(): List<PlannedOW>

    @Query(RelationalRoomQuery.todayPlannedOfficeWorksListQuery)
    suspend fun loadTodayRelationalPlannedOfficeWorks(today: String, tableId: IdAndNameTablesNamesEnum): List<RelationalPlannedOfficeWork>

    @Query(RelationalRoomQuery.plannedOfficeWorksListQuery)
    suspend fun loadRelationalPlannedOfficeWorks(tableId: IdAndNameTablesNamesEnum): List<RelationalPlannedOfficeWork>

    @Query("UPDATE ${TablesNames.PlannedOWTable} SET is_done = :isDone WHERE id = :doneId")
    suspend fun markAsDone(isDone: Boolean, doneId: Long)

    @Query("DELETE FROM ${TablesNames.PlannedOWTable}")
    suspend fun deleteAll()

}

@Dao
interface ActualVisitDao : GenericDao<ActualVisit> {
    @Query("SELECT * FROM ${TablesNames.ActualVisitTable}")
    suspend fun findAll(): List<ActualVisit>

    @Query(MainRoomQuery.insertActualVisitWithValidationQuery)
    suspend fun insertActualVisitWithValidation(
        onlineId: Long, divisionId: Long, brickId: Long, accountTypeId: Int, accountId: Long,
        accountDoctorId: Long, noOfDoctors: Int,plannedVisitId: Long, multiplicity: MultiplicityEnum,
        startDate: String, startTime: String, endDate: String, endTime: String, shift: ShiftEnum,
        userId: Long, lineId: Long, llStart: Double, lgStart: Double, llEnd: Double, lgEnd: Double,
        visitDuration: String, visitDeviation: Long, isSynced: Boolean, syncDate: String,
        syncTime: String, multipleListsInfo: RoomMultipleListsModule
    ): Long

    @Query(MainRoomQuery.updateSyncedActualVisitQuery)
    suspend fun updateSyncedActualVisits(
        onlineId: Long, syncDate: String, syncTime: String, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedActualVisitQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<ActualVisit>

    @Query(RelationalRoomQuery.actualVisitsListQuery)
    suspend fun loadRelationalActualVisits(): List<RelationalActualVisit>

    @Query("DELETE FROM ${TablesNames.ActualVisitTable}")
    suspend fun deleteAll()

}

@Dao
interface OfficeWorkDao : GenericDao<OfficeWork> {
    @Query("SELECT * FROM ${TablesNames.OfficeWorkTable}")
    suspend fun findAll(): List<OfficeWork>

    @Query(MainRoomQuery.insertOfficeWorkWithValidationQuery)
    suspend fun insertOfficeWorkWithValidation(
        onlineId: Long, owTypeId: Int, shift: ShiftEnum, plannedOwId: Long, notes: String, startDate: String,
        startTime: String, endDate: String, endTime: String, userId: Long, isSynced: Boolean,
        syncDate: String, syncTime: String,
    ): Long

    @Query(MainRoomQuery.insertFullDayOfficeWorkWithValidationQuery)
    suspend fun insertFullDayOfficeWorkWithValidation(
        onlineId: Long, owTypeId: Int,
        shift_1: ShiftEnum, shift_2: ShiftEnum,
        plannedOwId: Long, notes: String, startDate: String,
        startTime: String, endDate: String, endTime: String, userId: Long, isSynced: Boolean,
        syncDate: String, syncTime: String,
    ): Long

    @Query(MainRoomQuery.updateSyncedOfficeWorkQuery)
    suspend fun updateSyncedOfficeWorks(
        onlineId: Long, syncDate: String, syncTime: String, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedOfficeWorkQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<OfficeWork>

    @Query(RelationalRoomQuery.actualOfficeWorksListQuery)
    suspend fun loadRelationalOfficeWorkReports(tableId: IdAndNameTablesNamesEnum): List<RelationalOfficeWorkReport>

    @Query("DELETE FROM ${TablesNames.OfficeWorkTable}")
    suspend fun deleteAll()

}

@Dao
interface VacationDao : GenericDao<Vacation> {
    @Query("SELECT * FROM ${TablesNames.VacationTable}")
    suspend fun findAll(): List<Vacation>

    @Query(MainRoomQuery.insertFullDayVacationWithValidationQuery)
    suspend fun insertFullDayVacationWithValidation(
        onlineId: Long, vacationTypeId: Long, durationType: DurationEnum, shift: ShiftEnum,
        dateFrom: String, dateTo: String, note: String, userId: Long, isApproved: Boolean,
        isSynced: Boolean, syncDate: String, syncTime: String, uriListsInfo: RoomPathListModule
    ): Long

    @Query(MainRoomQuery.insertHalfDayVacationWithValidationQuery)
    suspend fun insertHalfDayVacationWithValidation(
        onlineId: Long, vacationTypeId: Long, durationType: DurationEnum, shift: ShiftEnum,
        dateFrom: String, dateTo: String, note: String, userId: Long, isApproved: Boolean,
        isSynced: Boolean, syncDate: String, syncTime: String, uriListsInfo: RoomPathListModule
    ): Long

    @Query(MainRoomQuery.updateSyncedVacationQuery)
    suspend fun updateSyncedVacations(
        onlineId: Long, syncDate: String, syncTime: String, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedVacationQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<Vacation>
    // TODO review to migrate
    @Query(RelationalRoomQuery.newPlanListQuery)
    suspend fun loadRelationalNewPlans(): List<RelationalNewPlan>

    @Query("UPDATE ${TablesNames.VacationTable} SET is_approved = :isApproved WHERE id = :approvedId")
    suspend fun markAsApproved(isApproved: Boolean, approvedId: Long)

    @Query("DELETE FROM ${TablesNames.VacationTable}")
    suspend fun deleteAll()

}

@Dao
interface OfflineLogDao : GenericDao<OfflineLog> {
    @Query("SELECT * FROM ${TablesNames.OfflineLogTable}")
    suspend fun findAll(): List<OfflineLog>

    @Query(MainRoomQuery.updateSyncedOfflineLogQuery)
    suspend fun updateSyncedOfflineLogs(
        onlineId: Long, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedOfflineLogQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<OfflineLog>

    @Query("DELETE FROM ${TablesNames.OfflineLogTable}")
    suspend fun deleteAll()

}

@Dao
interface OfflineLocDao : GenericDao<OfflineLoc> {
    @Query("SELECT * FROM ${TablesNames.OfflineLocTable}")
    suspend fun findAll(): List<OfflineLoc>

    @Query(MainRoomQuery.updateSyncedOfflineLocQuery)
    suspend fun updateSyncedOfflineLocations(
        onlineId: Long, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedOfflineLocQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<OfflineLoc>

    @Query("SELECT * FROM ${TablesNames.OfflineLocTable} ORDER BY id DESC LIMIT 1")
    fun getLastRecord(): OfflineLoc?

    @Query("DELETE FROM ${TablesNames.OfflineLocTable}")
    suspend fun deleteAll()

}

@Dao
interface NewPlanDao : GenericDao<NewPlanEntity> {
    @Query("SELECT * FROM ${TablesNames.NewPlanTable}")
    suspend fun findAll(): List<NewPlanEntity>

    @Query(MainRoomQuery.insertNewPlanWithValidationQuery)
    suspend fun insertNewPlanWithValidation(
        onlineId: Long, divId: Long, accTypeId: Int, accountId: Long, accountDoctorId: Long, members: Long,
        visitDate: String, visitTime: String, shift: Int, insertionDate: String, userId: Long,
        lineId: Long, isApproved: Boolean, relatedId: Long, isSynced: Boolean, syncDate: String,
        syncTime: String,
    ): Long

    @Query(MainRoomQuery.updateSyncedNewPlanQuery)
    suspend fun updateSyncedNewPlans(
        onlineId: Long, syncDate: String, syncTime: String, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedNewPlanQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<NewPlanEntity>

    @Query(RelationalRoomQuery.newPlanListQuery)
    suspend fun loadRelationalNewPlans(): List<RelationalNewPlan>

    @Query("UPDATE ${TablesNames.NewPlanTable} SET is_approved = :isApproved WHERE id = :approvedId")
    suspend fun markAsApproved(isApproved: Boolean, approvedId: Long)

    @Query("DELETE FROM ${TablesNames.NewPlanTable}")
    suspend fun deleteAll()

}