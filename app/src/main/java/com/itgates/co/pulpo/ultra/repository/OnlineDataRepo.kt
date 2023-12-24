package com.itgates.co.pulpo.ultra.repository

import android.content.Context
import com.itgates.co.pulpo.ultra.network.models.requestModels.*
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import okhttp3.ResponseBody
import retrofit2.Response

interface OnlineDataRepo {

    suspend fun getFile(fileName: String): ResponseBody

    suspend fun authenticationAction(
        headers: Map<String, String>,
        userModel: UploadedUserModel
    ): Response<LoginPharmaResponse>

    suspend fun fetchUserData(
        headers: Map<String, String>
    ): UserPharmaResponse

    suspend fun fetchMasterData(
        headers: Map<String, String>
    ): MasterDataPharmaResponse

    suspend fun fetchAccountsAndDoctorsDetailsData(
        headers: Map<String, String>
    ): AccountsAndDoctorsDetailsPharmaResponse

    suspend fun fetchPresentationsAndSlidesDetailsData(
        headers: Map<String, String>
    ): PresentationsAndSlidesDetailsPharmaResponse

    suspend fun fetchPlannedVisitsData(
        headers: Map<String, String>
    ): PlannedVisitsPharmaResponse

    suspend fun fetchPlannedOWsData(
        headers: Map<String, String>
    ): PlannedOWsPharmaResponse

    suspend fun uploadActualVisitsData(
        headers: Map<String, String>,
        uploadedListObj: UploadedActualVisitsListModel
    ): ActualVisitPharmaResponse

    suspend fun uploadOfficeWorksData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOfficeWorksListModel
    ): OfficeWorkPharmaResponse

    suspend fun uploadVacationsData(
        headers: Map<String, String>,
        uploadedListObj: UploadedVacationsListModel
    ): VacationPharmaResponse

    suspend fun uploadNewPlansData(
        headers: Map<String, String>,
        uploadedListObj: UploadedNewPlansListModel
    ): NewPlanPharmaResponse

    suspend fun uploadOfflineLogData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOfflineLogsListModel
    ): OfflineLogPharmaResponse

    suspend fun uploadOfflineLocData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOfflineLocsListModel
    ): OfflineLocPharmaResponse

    suspend fun uploadOnlineLogData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOnlineLogsListModel
    ): OnlineLogPharmaResponse
}