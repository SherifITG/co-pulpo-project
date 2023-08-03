package com.itgates.ultra.pulpo.cira.roomDataBase.daos

import androidx.room.*
import com.itgates.ultra.pulpo.cira.roomDataBase.converters.RoomMultipleListsModule
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.TablesNames.AccountTable
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.queries.MainRoomQuery
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.queries.RelationalRoomQuery
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.*

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
//    @Query("SELECT * FROM ${TablesNames.AccountTypeTable}")
//    suspend fun loadAllAccountType(): List<AccountType>

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
interface BrickDao : GenericDao<Brick> {
    @Query(MainRoomQuery.bricksQuery)
    suspend fun loadActualBricks(divIds: List<Long>): List<Brick>

    @Query("DELETE FROM ${TablesNames.BrickTable}")
    suspend fun deleteAll()
}

@Dao
interface ClassDao : GenericDao<Class> {

    @Query("DELETE FROM ${TablesNames.ClassTable}")
    suspend fun deleteAll()

    @Query(RelationalRoomQuery.classesQuery)
    suspend fun loadClassesByIdList(
        divIds: List<Long>,
        brickIds: List<Long>,
        accTypeTables: List<String>
    ): List<Class>
}

@Dao
interface DivisionDao : GenericDao<Division> {
    @Query(MainRoomQuery.divisionsQuery)
    suspend fun loadActualUserDivisions(ids: List<Long>): List<Division>

    @Query("DELETE FROM ${TablesNames.DivisionTable}")
    suspend fun deleteAll()
}

@Dao
interface SettingDao : GenericDao<Setting> {
    @Query(MainRoomQuery.settingByNamesListQuery)
    suspend fun loadAll(names: List<String>): List<Setting>

    @Query("DELETE FROM ${TablesNames.SettingTable}")
    suspend fun deleteAll()
}

@Dao
interface IdAndNameDao : GenericDao<IdAndNameEntity> {
//    @Query("SELECT * FROM ${TablesNames.IdAndNameTable}")
//    suspend fun loadAllIdAndNameRecords(): List<IdAndNameEntity>

    @Query(MainRoomQuery.idAndNameObjectsQuery)
    suspend fun loadAllByTAble(tableId: IdAndNameTablesNamesEnum): List<IdAndNameEntity>

    @Query(MainRoomQuery.idAndNameObjectsByTablesListQuery)
    suspend fun loadAllByTAblesList(tableIds: List<IdAndNameTablesNamesEnum>): List<IdAndNameEntity>

    @Query("DELETE FROM ${TablesNames.IdAndNameTable}")
    suspend fun deleteAll()
}

@Dao
interface AccountDao : GenericDao<Account> {
    @Query(MainRoomQuery.accountsQuery)
    suspend fun loadActualAccounts(divId: Long, brickId: Long, accTypeTable: String): List<Account>

    @Query(MainRoomQuery.accountsQueryWithoutBrick)
    suspend fun loadActualAccountsWithoutBrick(divId: Long, accTypeTable: String): List<Account>

    @Query(RelationalRoomQuery.accountsListQuery)
    suspend fun loadAllAccountReportData(): List<AccountData>

    @Query("select * from $AccountTable")
    suspend fun loadAllAccountReport(): List<Account>

    @Query(MainRoomQuery.updateAccountLocationQuery)
    suspend fun updateAccountLocation(llFirst: String, lgFirst: String, id: Long, table: String)

    @Query("DELETE FROM ${TablesNames.AccountTable}")
    suspend fun deleteAll()
}

@Dao
interface DoctorDao : GenericDao<Doctor> {
    @Query(MainRoomQuery.doctorsQuery)
    suspend fun loadActualDoctors(accountId: Long, table: String): List<Doctor>

    @Query(RelationalRoomQuery.doctorsListQuery)
    suspend fun loadAllDoctorReportData(tableId: IdAndNameTablesNamesEnum): List<DoctorData>

    @Query(RelationalRoomQuery.doctorsPlanningListQuery)
    suspend fun loadAllDoctorPlanningData(tableId: IdAndNameTablesNamesEnum): List<DoctorPlanningData>

    @Query("DELETE FROM ${TablesNames.DoctorTable}")
    suspend fun deleteAll()
}

@Dao
interface PresentationDao : GenericDao<Presentation> {

    @Query("SELECT * FROM ${TablesNames.PresentationTable}")
    suspend fun loadPresentations(): List<Presentation>

    @Query("DELETE FROM ${TablesNames.PresentationTable}")
    suspend fun deleteAll()
}

@Dao
interface SlideDao : GenericDao<Slide> {
    @Query("SELECT * FROM ${TablesNames.SlideTable} WHERE presentation_id = :presentationId")
    suspend fun loadByPresentationId(presentationId: Long): List<Slide>

    @Query("DELETE FROM ${TablesNames.SlideTable}")
    suspend fun deleteAll()
}

@Dao
interface PlannedVisitDao : GenericDao<PlannedVisit> {

    @Query(RelationalRoomQuery.plannedVisitsListQuery)
    suspend fun loadRelationalPlannedVisits(): List<RelationalPlannedVisit>

    @Query(RelationalRoomQuery.todayPlannedVisitsListQuery)
    suspend fun loadTodayRelationalPlannedVisits(today: String): List<RelationalPlannedVisit>

    @Query(RelationalRoomQuery.plannedOfficeWorksListQuery)
    suspend fun loadRelationalPlannedOfficeWorks(tableId: IdAndNameTablesNamesEnum): List<RelationalPlannedOfficeWork>

    @Query("UPDATE ${TablesNames.PlannedVisitTable} SET is_done = :isDone WHERE id = :doneId")
    suspend fun markAsDone(isDone: Boolean, doneId: Long)

    @Query("DELETE FROM ${TablesNames.PlannedVisitTable}")
    suspend fun deleteAll()

}

@Dao
interface ActualVisitDao : GenericDao<ActualVisit> {
    @Query(MainRoomQuery.insertActualVisitWithValidationQuery)
    suspend fun insertActualVisitWithValidation(
        onlineId: Long, divisionId: Long, accountTypeId: Int, itemId: Long, itemDoctorId: Long,
        noOfDoctors: Int,plannedVisitId: Long, multiplicity: MultiplicityEnum, startDate: String,
        startTime: String, endDate: String, endTime: String, shift: ShiftEnum, comments: String,
        insertionDate: String, insertionTime: String, userId: Long, teamId: Long, llStart: Double,
        lgStart: Double, llEnd: Double, lgEnd: Double, visitDuration: String, visitDeviation: Long,
        isSynced: Boolean, syncDate: String, syncTime: String, addedDate: String,
        multipleListsInfo: RoomMultipleListsModule
    ): Long

    @Query(MainRoomQuery.insertOfficeWorkWithValidationQuery)
    suspend fun insertOfficeWorkWithValidation(
        onlineId: Long, divisionId: Long, accountTypeId: Int, itemId: Long, itemDoctorId: Long,
        noOfDoctors: Int,plannedVisitId: Long, multiplicity: MultiplicityEnum, startDate: String,
        startTime: String, endDate: String, endTime: String, shift: ShiftEnum, comments: String,
        insertionDate: String, insertionTime: String, userId: Long, teamId: Long, llStart: Double,
        lgStart: Double, llEnd: Double, lgEnd: Double, visitDuration: String, visitDeviation: Long,
        isSynced: Boolean, syncDate: String, syncTime: String, addedDate: String,
        multipleListsInfo: RoomMultipleListsModule
    ): Long

    @Query(MainRoomQuery.updateSyncedActualVisitQuery)
    suspend fun updateSyncedActualVisits(
        onlineId: Long, syncDate: String, syncTime: String, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedActualVisitQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<ActualVisit>

    @Query(RelationalRoomQuery.actualVisitsListQuery)
    suspend fun loadRelationalActualVisits(): List<RelationalActualVisit>

    @Query(RelationalRoomQuery.actualOfficeWorksListQuery)
    suspend fun loadRelationalOfficeWorkReports(tableId: IdAndNameTablesNamesEnum): List<RelationalOfficeWorkReport>

    @Query("DELETE FROM ${TablesNames.ActualVisitTable}")
    suspend fun deleteAll()

}

@Dao
interface OfflineLogDao : GenericDao<OfflineLog> {
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
    @Query(MainRoomQuery.updateSyncedOfflineLocQuery)
    suspend fun updateSyncedOfflineLocations(
        onlineId: Long, isSynced: Boolean, offlineId: Long
    )

    @Query(MainRoomQuery.unSyncedOfflineLocQuery)
    suspend fun loadUnSyncedRecords(isSynced: Boolean): List<OfflineLoc>

    @Query("DELETE FROM ${TablesNames.OfflineLocTable}")
    suspend fun deleteAll()

}

@Dao
interface NewPlanDao : GenericDao<NewPlanEntity> {

    @Query(MainRoomQuery.insertNewPlanWithValidationQuery)
    suspend fun insertNewPlanWithValidation(
        onlineId: Long, divId: Long, accTypeId: Int, itemId: Long, itemDoctorId: Long, members: Long,
        visitDate: String, visitTime: String, shift: Int, insertionDate: String, userId: Long,
        teamId: Long, isApproved: Boolean, relatedId: Long, isSynced: Boolean, syncDate: String,
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

    @Query("UPDATE ${TablesNames.NewPlanTable} SET is_approved = :isApproved WHERE id = :doneId")
    suspend fun markAsApproved(isApproved: Boolean, doneId: Long)

    @Query("DELETE FROM ${TablesNames.NewPlanTable}")
    suspend fun deleteAll()

}