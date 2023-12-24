package com.itgates.co.pulpo.ultra.repository

import android.util.Log
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.ActualVisitDTO
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.OfflineRecordDTO
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.roomDataBase.daos.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.*
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.streams.toList


class OfflineDataRepoImpl @Inject constructor(
    private val accountTypeDao: AccountTypeDao,
    private val lineDao: LineDao,
    private val brickDao: BrickDao,
    private val divisionDao: DivisionDao,
    private val settingDao: SettingDao,
    private val vacationTypeDao: VacationTypeDao,
    private val idAndNameDao: IdAndNameDao,

    private val accountDao: AccountDao,
    private val doctorDao: DoctorDao,
    private val presentationDao: PresentationDao,
    private val slideDao: SlideDao,
    private val plannedVisitDao: PlannedVisitDao,
    private val plannedOWDao: PlannedOWDao,
    private val actualVisitDao: ActualVisitDao,
    private val officeWorkDao: OfficeWorkDao,

    private val offlineLogDao: OfflineLogDao,
    private val offlineLocDao: OfflineLocDao,

    private val newPlanDao: NewPlanDao,
    private val vacationDao: VacationDao,
): OfflineDataRepo {

    override suspend fun genericLoadAll(tableName: String): List<Any> {
        return when (tableName) {
            TablesNames.IdAndNameTable -> idAndNameDao.findAll()
            TablesNames.AccountTypeTable -> accountTypeDao.findAll()
            TablesNames.LineTable -> lineDao.findAll()
            TablesNames.BrickTable -> brickDao.findAll()
            TablesNames.DivisionTable -> divisionDao.findAll()
            TablesNames.SettingTable -> settingDao.findAll()
            TablesNames.VacationTypeTable -> vacationTypeDao.findAll()
            TablesNames.AccountTable -> accountDao.findAll()
            TablesNames.DoctorTable -> doctorDao.findAll()
            TablesNames.NewPlanTable -> newPlanDao.findAll()
            TablesNames.VacationTable -> vacationDao.findAll()
            TablesNames.ActualVisitTable -> actualVisitDao.findAll()
            TablesNames.PlannedVisitTable -> plannedVisitDao.findAll()
            TablesNames.PresentationTable -> presentationDao.findAll()
            TablesNames.SlideTable -> slideDao.findAll()
            TablesNames.OfflineLogTable -> offlineLogDao.findAll()
            TablesNames.OfflineLocTable -> offlineLocDao.findAll()
            else -> throw Exception("Entity not support exception")
        }
    }

    override suspend fun loadActualSettings(): List<Setting> {
        return settingDao.loadAll(
            listOf(
//                SettingEnum.METERS_TO_ACCEPT_DEVIATION.text,
                SettingEnum.ALLOW_ACTUAL_WITH_DEVIATION.text,
                SettingEnum.FIELD_NO_OF_DOCTORS.text,
                SettingEnum.IS_REQUIRED_MANAGER_MEMBER.text,
                SettingEnum.NO_OF_PRODUCT.text,
                SettingEnum.NO_OF_GIVEAWAYS.text,
                SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text,
                SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text,
                SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text,
                SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text,
            )
        )
    }

    override suspend fun loadMainActivitySettings(): List<Setting> {
        return settingDao.loadAll(
            listOf(
                SettingEnum.ACTUAL_DIRECT.text,
                SettingEnum.APP_CHECK_IN_AND_CHECKOUT.text,
                SettingEnum.APP_LOGOUT.text,
            )
        )
    }

    override suspend fun loadActualAccountTypes(divIds: List<Long>, brickIds: List<Long>): List<AccountType> {
        return if (brickIds.first() != -1L) {
            accountTypeDao.loadActualAccountTypes(divIds, brickIds)
        }
        else {
            accountTypeDao.loadActualAccountTypesWithoutBrick(divIds)
        }
    }

    override suspend fun loadAllAccountTypes(): List<AccountType> {
        return accountTypeDao.loadAllAccountTypes()
    }

    override suspend fun loadClassesData(
        divIds: List<Long>,
        brickIds: List<Long>,
        accTypeIds: List<Int>
    ): List<IdAndNameEntity> {
        return idAndNameDao.loadClassesByIdList(
            IdAndNameTablesNamesEnum.PRODUCT,
            divIds,
            brickIds,
            accTypeIds
        )
    }

    override suspend fun loadRelationalLines(): List<RelationalLine> {
        val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
        return lineDao.loadRelationalLines(today)
    }

    override suspend fun loadUserDivisions(): List<Division> {
        return divisionDao.findAll()
    }

    override suspend fun loadActualBricks(divIds: List<Long>): List<Brick> {
        return brickDao.loadActualBricks(divIds)
    }

    override suspend fun loadIdAndNameTablesByTAblesListForActualActivity(lineId: Long): List<IdAndNameEntity> {
        return idAndNameDao.loadByLineAndByTablesList(
            listOf(
                IdAndNameTablesNamesEnum.PRODUCT,
                IdAndNameTablesNamesEnum.FEEDBACK,
                IdAndNameTablesNamesEnum.GIVEAWAY,
                IdAndNameTablesNamesEnum.MANAGER
            ),
            lineId
        )
    }

    override suspend fun loadSlidesByPresentationId(presentationId: Long): List<Slide> {
        return slideDao.loadByPresentationId(presentationId)
    }

    override suspend fun loadOfficeWorkTypes(): List<IdAndNameEntity> {
        return idAndNameDao.loadAllByTablesList(
            listOf(
                IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE,
            )
        )
    }

    override suspend fun loadVacationTypes(): List<VacationType> {
        return vacationTypeDao.findAll()
    }

    override suspend fun loadProducts(): List<IdAndNameEntity> {
        return idAndNameDao.loadAllByTablesList(listOf(IdAndNameTablesNamesEnum.PRODUCT))
    }

    override suspend fun loadActualAccounts(
        lineId: Long,
        divId: Long,
        brickId: Long,
        accTypeId: Int
    ): List<Account> {
        return if (brickId != -1L)
            accountDao.loadActualAccounts(lineId, divId, brickId, accTypeId)
        else
            accountDao.loadActualAccountsWithoutBrick(lineId, divId, accTypeId)
    }

    override suspend fun loadActualDoctors(lineId: Long, accountId: Long, accTypeId: Int): List<Doctor> {
        return doctorDao.loadActualDoctors(lineId, accountId, accTypeId)
    }

    override suspend fun loadPresentations(): List<Presentation> {
        return presentationDao.loadPresentations()
    }

    override suspend fun loadUnSyncedActualVisitsData(): List<ActualVisit> {
        return actualVisitDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadUnSyncedOfficeWorksData(): List<OfficeWork> {
        return officeWorkDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadUnSyncedVacationsData(): List<Vacation> {
        return vacationDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadUnSyncedActualNewPlansData(): List<NewPlanEntity> {
        return newPlanDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadRelationalPlannedVisitsData(): List<RelationalPlannedVisit> {
        return plannedVisitDao.loadRelationalPlannedVisits()
    }

    override suspend fun loadTodayRelationalPlannedVisitsData(): List<RelationalPlannedVisit> {
        val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
        return plannedVisitDao.loadTodayRelationalPlannedVisits(today)
    }

    override suspend fun loadRelationalNewPlansData(): List<RelationalNewPlan> {
        return newPlanDao.loadRelationalNewPlans()
    }

    override suspend fun loadRelationalPlannedOfficeWorksData(): List<RelationalPlannedOfficeWork> {
        return plannedOWDao.loadRelationalPlannedOfficeWorks(IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE)
    }

    override suspend fun loadTodayRelationalPlannedOfficeWorksData(): List<RelationalPlannedOfficeWork> {
        val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
        return plannedOWDao.loadTodayRelationalPlannedOfficeWorks(today, IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE)
    }

    override suspend fun loadTodayPlannedVisitsAndOWCount(): Long {
        val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
        return plannedVisitDao.loadTodayPlannedVisitsAndOWCount(today)
    }

    override suspend fun markPlannedVisitAsDone(doneId: Long): Boolean {
        plannedVisitDao.markAsDone(true, doneId)
        return true
    }

    override suspend fun markPlannedOfficeWorkAsDone(doneId: Long): Boolean {
        plannedOWDao.markAsDone(true, doneId)
        return true
    }

    override suspend fun loadRelationalActualVisitsData(): List<RelationalActualVisit> {
        return actualVisitDao.loadRelationalActualVisits()
    }

    override suspend fun loadRelationalOfficeWorkReportsData(): List<RelationalOfficeWorkReport> {
        return officeWorkDao.loadRelationalOfficeWorkReports(IdAndNameTablesNamesEnum.OFFICE_WORK_TYPE)
    }

    override suspend fun loadAllAccountReportData(): List<AccountData> {
        return accountDao.loadAllAccountReportData()
    }

    override suspend fun updateAccountLocation(
        llFirst: String, lgFirst: String,
        id: Long, accTypeId: Int, lineId: Long, divId: Long, brickId: Long
    ) {
        accountDao.updateAccountLocation(llFirst, lgFirst, id, accTypeId, lineId, divId, brickId)
    }

    override suspend fun loadAllDoctorReportData(): List<DoctorData> {
        return doctorDao.loadAllDoctorReportData(IdAndNameTablesNamesEnum.SPECIALITY)
    }

    override suspend fun loadAllDoctorPlanningData(): List<DoctorPlanningData> {
        return doctorDao.loadAllDoctorPlanningData()
    }

    override suspend fun uploadedActualVisitData(actualVisitDTO: ActualVisitDTO) {
        return actualVisitDao.updateSyncedActualVisits(
            actualVisitDTO.visitId, actualVisitDTO.syncDate, actualVisitDTO.syncTime,
            (actualVisitDTO.visitId > 0), actualVisitDTO.offlineId
        )
    }

    override suspend fun uploadedOfficeWorkData(officeWorkDTO: OfficeWorkDTO) {
        return officeWorkDao.updateSyncedOfficeWorks(
            officeWorkDTO.owId, officeWorkDTO.syncDate, officeWorkDTO.syncTime,
            (officeWorkDTO.owId > 0), officeWorkDTO.offlineId
        )
    }

    override suspend fun uploadedVacationData(vacationDTO: VacationDTO) {
        return vacationDao.updateSyncedVacations(
            vacationDTO.vacationId, vacationDTO.syncDate, vacationDTO.syncTime,
            (vacationDTO.vacationId > 0), vacationDTO.offlineId
        )
    }

    override suspend fun uploadedNewPlanData(newPlanDTO: NewPlanDTO) {
        return newPlanDao.updateSyncedNewPlans(
            newPlanDTO.plannedId, "", "",
            (newPlanDTO.plannedId > 0), newPlanDTO.offlineId
        )
    }

    override suspend fun insertActualVisitWithValidation(actualVisit: ActualVisit): Long {
        return actualVisitDao.insertActualVisitWithValidation(
            actualVisit.onlineId, actualVisit.divisionId, actualVisit.brickId,
            actualVisit.accountTypeId, actualVisit.accountId, actualVisit.accountDoctorId,
            actualVisit.noOfDoctors, actualVisit.plannedVisitId, actualVisit.multiplicity,
            actualVisit.startDate, actualVisit.startTime, actualVisit.endDate,
            actualVisit.endTime, actualVisit.shift, actualVisit.userId, actualVisit.lineId,
            actualVisit.llStart, actualVisit.lgStart, actualVisit.llEnd, actualVisit.lgEnd,
            actualVisit.visitDuration, actualVisit.visitDeviation, actualVisit.isSynced,
            actualVisit.syncDate, actualVisit.syncTime, actualVisit.multipleListsInfo
        )
    }

    override suspend fun insertOfficeWorkWithValidation(officeWork: OfficeWork): Long {
        return officeWorkDao.insertOfficeWorkWithValidation(
            officeWork.onlineId, officeWork.owTypeId, officeWork.shift, officeWork.plannedOwId,
            officeWork.notes, officeWork.startDate, officeWork.startTime, officeWork.endDate,
            officeWork.endTime, officeWork.userId, officeWork.isSynced, officeWork.syncDate,
            officeWork.syncTime
        )
    }

    override suspend fun insertFullDayActualOfficeWorkWithValidation(officeWork: OfficeWork): Long {
        return officeWorkDao.insertFullDayOfficeWorkWithValidation(
            officeWork.onlineId, officeWork.owTypeId,
            ShiftEnum.AM_SHIFT, ShiftEnum.PM_SHIFT,
            officeWork.plannedOwId,
            officeWork.notes, officeWork.startDate, officeWork.startTime, officeWork.endDate,
            officeWork.endTime, officeWork.userId, officeWork.isSynced, officeWork.syncDate,
            officeWork.syncTime
        )
    }

    override suspend fun insertFullDayVacationWithValidation(vacation: Vacation): Long {
        return vacationDao.insertFullDayVacationWithValidation(
            vacation.onlineId, vacation.vacationTypeId, vacation.durationType, vacation.shift,
            vacation.dateFrom, vacation.dateTo ,vacation.note, vacation.userId,
            vacation.isApproved, vacation.isSynced, vacation.syncDate, vacation.syncTime,
            vacation.uriListsInfo
        )
    }

    override suspend fun insertHalfDayVacationWithValidation(vacation: Vacation): Long {
        return vacationDao.insertHalfDayVacationWithValidation(
            vacation.onlineId, vacation.vacationTypeId, vacation.durationType, vacation.shift,
            vacation.dateFrom, vacation.dateTo ,vacation.note, vacation.userId,
            vacation.isApproved, vacation.isSynced, vacation.syncDate, vacation.syncTime,
            vacation.uriListsInfo
        )
    }

    override suspend fun saveMasterData(masterDataPharmaResponse: MasterDataPharmaResponse) {
        idAndNameDao.insertAll(masterDataPharmaResponse.data.collectAllIdAndNameRoomObjects())
        accountTypeDao.insertAll(masterDataPharmaResponse.data.collectAccTypeRoomObjects())
        lineDao.insertAll(masterDataPharmaResponse.data.collectLineRoomObjects())
        divisionDao.insertAll(masterDataPharmaResponse.data.collectDivisionRoomObjects())
        brickDao.insertAll(masterDataPharmaResponse.data.collectBrickRoomObjects())
        settingDao.insertAll(masterDataPharmaResponse.data.collectSettingRoomObjects())
        vacationTypeDao.insertAll(masterDataPharmaResponse.data.collectVacationTypeRoomObjects())
    }

    override suspend fun saveAccountAndDoctorData(
        accountsAndDoctorsDetailsPharmaResponse: AccountsAndDoctorsDetailsPharmaResponse
    ) {
        accountDao.insertAll(accountsAndDoctorsDetailsPharmaResponse.data.collectAccountRoomObjects())
        doctorDao.insertAll(accountsAndDoctorsDetailsPharmaResponse.data.collectDoctorRoomObjects())
    }

    override suspend fun savePresentationAndSlideData(
        presentationsAndSlidesDetailsPharmaResponse: PresentationsAndSlidesDetailsPharmaResponse
    ) {
        presentationDao.insertAll(presentationsAndSlidesDetailsPharmaResponse.data.collectPresentationRoomObjects())
        slideDao.insertAll(presentationsAndSlidesDetailsPharmaResponse.data.collectSlideRoomObjects())
    }

    override suspend fun savePlannedVisitData(plannedVisitsPharmaResponse: PlannedVisitsPharmaResponse) {
        plannedVisitDao.insertAll(
            plannedVisitsPharmaResponse.data.stream().map { it.toRoomPlannedVisit() }.toList()
        )
    }

    override suspend fun savePlannedOWData(plannedOWsPharmaResponse: PlannedOWsPharmaResponse) {
        plannedOWDao.insertAll(
            plannedOWsPharmaResponse.data.stream().map { it.toRoomPlannedOW() }.toList()
        )
    }

    override suspend fun saveOfflineLog(offlineLog: OfflineLog) {
        offlineLogDao.insert(offlineLog)
    }

    override suspend fun saveOfflineLoc(offlineLoc: OfflineLoc) {
        offlineLocDao.insert(offlineLoc)
    }

    override suspend fun getLastOfflineLoc(): OfflineLoc? {
        return offlineLocDao.getLastRecord()
    }

    override suspend fun saveAllNewPlans(newPlanList: List<NewPlanEntity>) {
        newPlanDao.insertAll(newPlanList)
    }

    override suspend fun saveNewPlans(newPlanList: List<NewPlanEntity>): HashMap<Int, Long> {
        val planingMap = HashMap<Int, Long>()
        newPlanList.forEachIndexed { index, planningObj ->
            try {
                val job = CoroutineManager.getScope().launch {
                    planingMap[index] = newPlanDao.insertNewPlanWithValidation(
                        planningObj.onlineId, planningObj.divisionId, planningObj.accountTypeId.toInt(),
                        planningObj.accountId, planningObj.accountDoctorId, planningObj.members,
                        planningObj.visitDate, planningObj.visitTime, planningObj.shift,
                        planningObj.insertionDate, planningObj.userId, planningObj.lineId,
                        planningObj.isApproved, planningObj.relatedId, planningObj.isSynced,
                        planningObj.syncDate, planningObj.syncTime
                    )
                }
                job.join()
            } catch (e: Exception) {
                planingMap[index] = -2
                Log.d("OfflineDataRepoImpl", "saveNewPlans: failed $e")
            }
        }
        return planingMap
    }

    override suspend fun uploadedOfflineLogData(offlineLogDTO: OfflineRecordDTO) {
        return offlineLogDao.updateSyncedOfflineLogs(
            offlineLogDTO.onlineId, (offlineLogDTO.onlineId > 0), offlineLogDTO.offlineId
        )
    }

    override suspend fun uploadedOfflineLocData(offlineLocDTO: OfflineRecordDTO) {
        return offlineLocDao.updateSyncedOfflineLocations(
            offlineLocDTO.onlineId, (offlineLocDTO.onlineId > 0), offlineLocDTO.offlineId
        )
    }

    override suspend fun loadUnSyncedOfflineLogData(): List<OfflineLog> {
        return offlineLogDao.loadUnSyncedRecords(false)
    }

    override suspend fun loadUnSyncedOfflineLocData(): List<OfflineLoc> {
        return offlineLocDao.loadUnSyncedRecords(false)
    }

    override suspend fun clearMasterData() {
        idAndNameDao.deleteAll()
        accountTypeDao.deleteAll()
        lineDao.deleteAll()
        brickDao.deleteAll()
        divisionDao.deleteAll()
        settingDao.deleteAll()
        vacationTypeDao.deleteAll()
    }

    override suspend fun clearAccountAndDoctorData() {
        accountDao.deleteAll()
        doctorDao.deleteAll()
    }

    override suspend fun clearPresentationAndSlideData() {
        presentationDao.deleteAll()
        slideDao.deleteAll()
    }

    override suspend fun clearPlannedVisitData() {
        plannedVisitDao.deleteAll()
    }

    override suspend fun clearPlannedOWData() {
        plannedOWDao.deleteAll()
    }

}