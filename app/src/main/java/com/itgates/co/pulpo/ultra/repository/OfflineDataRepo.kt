package com.itgates.co.pulpo.ultra.repository

import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.ActualVisitDTO
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.OfflineRecordDTO
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.*

interface OfflineDataRepo {
    suspend fun genericLoadAll(tableName: String): List<Any>
    suspend fun loadActualSettings(): List<Setting>
    suspend fun loadMainActivitySettings(): List<Setting>
    suspend fun loadActualAccountTypes(divIds: List<Long>, brickIds: List<Long>): List<AccountType>
    suspend fun loadAllAccountTypes(): List<AccountType>
    suspend fun loadClassesData(
        divIds: List<Long>,
        brickIds: List<Long>,
        accTypeIds: List<Int>
    ): List<IdAndNameEntity>
    suspend fun loadRelationalLines(): List<RelationalLine>
    suspend fun loadUserDivisions(): List<Division>
    suspend fun loadActualBricks(divIds: List<Long>): List<Brick>
    suspend fun loadIdAndNameTablesByTAblesListForActualActivity(lineId: Long): List<IdAndNameEntity>
    suspend fun loadSlidesByPresentationId(presentationId: Long): List<Slide>
    suspend fun loadOfficeWorkTypes(): List<IdAndNameEntity>
    suspend fun loadVacationTypes(): List<VacationType>
    suspend fun loadProducts(): List<IdAndNameEntity>
    suspend fun loadActualAccounts(
        lineId: Long,
        divId: Long,
        brickId: Long,
        accTypeId: Int
    ): List<Account>
    suspend fun loadActualDoctors(lineId: Long, accountId: Long, accTypeId: Int): List<Doctor>
    suspend fun loadPresentations(): List<Presentation>

    suspend fun loadUnSyncedActualVisitsData(): List<ActualVisit>
    suspend fun loadUnSyncedOfficeWorksData(): List<OfficeWork>
    suspend fun loadUnSyncedVacationsData(): List<Vacation>
    suspend fun loadUnSyncedActualNewPlansData(): List<NewPlanEntity>

    suspend fun loadRelationalPlannedVisitsData(): List<RelationalPlannedVisit>
    suspend fun loadTodayRelationalPlannedVisitsData(): List<RelationalPlannedVisit>
    suspend fun loadRelationalNewPlansData(): List<RelationalNewPlan>
    suspend fun loadRelationalPlannedOfficeWorksData(): List<RelationalPlannedOfficeWork>
    suspend fun loadTodayRelationalPlannedOfficeWorksData(): List<RelationalPlannedOfficeWork>
    suspend fun loadTodayPlannedVisitsAndOWCount(): Long
    suspend fun markPlannedVisitAsDone(doneId: Long): Boolean
    suspend fun markPlannedOfficeWorkAsDone(doneId: Long): Boolean
    suspend fun loadRelationalActualVisitsData(): List<RelationalActualVisit>
    suspend fun loadRelationalOfficeWorkReportsData(): List<RelationalOfficeWorkReport>
    suspend fun loadAllAccountReportData(): List<AccountData>
    suspend fun updateAccountLocation(
        llFirst: String, lgFirst: String,
        id: Long, accTypeId: Int, lineId: Long, divId: Long, brickId: Long
    )
    suspend fun loadAllDoctorReportData(): List<DoctorData>
    suspend fun loadAllDoctorPlanningData(): List<DoctorPlanningData>

    suspend fun uploadedActualVisitData(actualVisitDTO: ActualVisitDTO)
    suspend fun uploadedOfficeWorkData(officeWorkDTO: OfficeWorkDTO)
    suspend fun uploadedVacationData(vacationDTO: VacationDTO)
    suspend fun uploadedNewPlanData(newPlanDTO: NewPlanDTO)
    suspend fun insertActualVisitWithValidation(actualVisit: ActualVisit): Long
    suspend fun insertOfficeWorkWithValidation(officeWork: OfficeWork): Long
    suspend fun insertFullDayActualOfficeWorkWithValidation(officeWork: OfficeWork): Long
    suspend fun insertFullDayVacationWithValidation(vacation: Vacation): Long
    suspend fun insertHalfDayVacationWithValidation(vacation: Vacation): Long

    suspend fun saveMasterData(masterDataPharmaResponse: MasterDataPharmaResponse)
    suspend fun saveAccountAndDoctorData(
        accountsAndDoctorsDetailsPharmaResponse: AccountsAndDoctorsDetailsPharmaResponse
    )
    suspend fun savePresentationAndSlideData(
        presentationsAndSlidesDetailsPharmaResponse: PresentationsAndSlidesDetailsPharmaResponse
    )
    suspend fun savePlannedVisitData(plannedVisitsPharmaResponse: PlannedVisitsPharmaResponse)
    suspend fun savePlannedOWData(plannedOWsPharmaResponse: PlannedOWsPharmaResponse)

    suspend fun saveOfflineLog(offlineLog: OfflineLog)
    suspend fun saveOfflineLoc(offlineLoc: OfflineLoc)

    suspend fun getLastOfflineLoc(): OfflineLoc?

    suspend fun saveAllNewPlans(newPlanList: List<NewPlanEntity>)
    suspend fun saveNewPlans(newPlanList: List<NewPlanEntity>): HashMap<Int, Long>

    suspend fun uploadedOfflineLogData(offlineLogDTO: OfflineRecordDTO)
    suspend fun uploadedOfflineLocData(offlineLocDTO: OfflineRecordDTO)

    suspend fun loadUnSyncedOfflineLogData(): List<OfflineLog>
    suspend fun loadUnSyncedOfflineLocData(): List<OfflineLoc>


    suspend fun clearMasterData()
    suspend fun clearAccountAndDoctorData()
    suspend fun clearPresentationAndSlideData()
    suspend fun clearPlannedVisitData()
    suspend fun clearPlannedOWData()
}