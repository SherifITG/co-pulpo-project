package com.itgates.ultra.pulpo.cira.repository

import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.ActualVisitDTO
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.OfflineRecordDTO
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.*

interface OfflineDataRepo {
    suspend fun saveFileData(itgFile: ItgFile)
    suspend fun loadFileData(): ItgFile

    suspend fun loadActualSettings(): List<Setting>
    suspend fun loadActualAccountTypes(divIds: List<Long>, brickIds: List<Long>): List<AccountType>
    suspend fun loadAllAccountTypes(): List<AccountType>
    suspend fun loadClassesData(
        divIds: List<Long>,
        brickIds: List<Long>,
        accTypeTables: List<String>
    ): List<Class>
    suspend fun loadActualUserDivisions(ids: List<Long>): List<Division>
    suspend fun loadActualBricks(divIds: List<Long>): List<Brick>
    suspend fun loadIdAndNameTablesByTAblesListForActualActivity(): List<IdAndNameEntity>
    suspend fun loadSlidesByPresentationId(presentationId: Long): List<Slide>
    suspend fun loadOfficeWorkTypes(): List<IdAndNameEntity>
    suspend fun loadProducts(): List<IdAndNameEntity>
    suspend fun loadActualAccounts(divId: Long, brickIds: List<Long>, table: String): List<Account>
    suspend fun loadActualDoctors(accountId: Long, table: String): List<Doctor>
    suspend fun loadPresentations(): List<Presentation>

    suspend fun loadUnSyncedActualVisitsData(): List<ActualVisit>

    suspend fun loadRelationalPlannedVisitsData(): List<RelationalPlannedVisit>
    suspend fun loadRelationalNewPlansData(): List<RelationalNewPlan>
    suspend fun loadTodayRelationalPlannedVisitsData(): List<RelationalPlannedVisit>
    suspend fun loadRelationalPlannedOfficeWorksData(): List<RelationalPlannedOfficeWork>
    suspend fun markPlannedVisitAsDone(doneId: Long): Boolean
    suspend fun loadRelationalActualVisitsData(): List<RelationalActualVisit>
    suspend fun loadRelationalOfficeWorkReportsData(): List<RelationalOfficeWorkReport>
    suspend fun loadAllAccountReportData(): List<AccountData>
    suspend fun updateAccountLocation(llFirst: String, lgFirst: String, id: Long)
    suspend fun loadAllDoctorReportData(): List<DoctorData>
    suspend fun loadAllDoctorPlanningData(): List<DoctorPlanningData>

    suspend fun uploadedActualVisitData(actualVisitDTO: ActualVisitDTO)
    suspend fun insertActualVisitWithValidation(actualVisit: ActualVisit): Long

    suspend fun saveMasterData(masterDataPharmaResponse: MasterDataPharmaResponse)
    suspend fun saveAccountAndDoctorData(
        accountsAndDoctorsDetailsPharmaResponse: AccountsAndDoctorsDetailsPharmaResponse
    )
    suspend fun savePresentationAndSlideData(
        presentationsAndSlidesDetailsPharmaResponse: PresentationsAndSlidesDetailsPharmaResponse
    )
    suspend fun savePlannedVisitData(plannedVisitsPharmaResponse: PlannedVisitsPharmaResponse)

    suspend fun saveOfflineLog(offlineLog: OfflineLog)
    suspend fun saveOfflineLoc(offlineLoc: OfflineLoc)

    suspend fun saveNewPlans(newPlanList: List<NewPlanEntity>)

    suspend fun uploadedOfflineLogData(offlineLogDTO: OfflineRecordDTO)
    suspend fun uploadedOfflineLocData(offlineLocDTO: OfflineRecordDTO)

    suspend fun loadUnSyncedOfflineLogData(): List<OfflineLog>
    suspend fun loadUnSyncedOfflineLocData(): List<OfflineLoc>


    suspend fun clearMasterData()
    suspend fun clearAccountAndDoctorData()
    suspend fun clearPresentationAndSlideData()
    suspend fun clearPlannedVisitData()
}