package com.itgates.co.pulpo.ultra.repository

import android.content.Context
import com.google.gson.Gson
import com.itgates.co.pulpo.ultra.network.models.requestModels.*
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.network.retrofit.DataRetrofitApi
import com.itgates.co.pulpo.ultra.network.retrofit.FilesDataRetrofitApi
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class OnlineDataRepoImpl @Inject constructor(
    private val dataRetrofitApi: DataRetrofitApi,
    private val filesDataRetrofitApi: FilesDataRetrofitApi,
): OnlineDataRepo {

    override suspend fun getFile(fileName: String): ResponseBody {
        return filesDataRetrofitApi.getFileAsync(fileName).await()
    }

    override suspend fun authenticationAction(
        headers: Map<String, String>,
        userModel: UploadedUserModel
    ): Response<LoginPharmaResponse> {
        return dataRetrofitApi.pharmaAuthenticateUserAsync(headers, userModel).await()
    }

    override suspend fun fetchUserData(
        headers: Map<String, String>
    ): UserPharmaResponse {
        return dataRetrofitApi.pharmaUserDataAsync(headers).await()
    }

    override suspend fun fetchMasterData(
        headers: Map<String, String>
    ): MasterDataPharmaResponse {
        return dataRetrofitApi.masterDataAsync(headers).await()
    }

    override suspend fun fetchAccountsAndDoctorsDetailsData(
        headers: Map<String, String>
    ): AccountsAndDoctorsDetailsPharmaResponse {
        return dataRetrofitApi.accountsAndDoctorsDetailsAsync(headers).await()
    }

    override suspend fun fetchPresentationsAndSlidesDetailsData(
        headers: Map<String, String>
    ): PresentationsAndSlidesDetailsPharmaResponse {
        return dataRetrofitApi.presentationsAndSlidesDetailsAsync(headers).await()
    }

    override suspend fun fetchPlannedVisitsData(
        headers: Map<String, String>
    ): PlannedVisitsPharmaResponse {
        return dataRetrofitApi.plannedVisitsDataAsync(headers).await()
    }

    override suspend fun fetchPlannedOWsData(
        headers: Map<String, String>
    ): PlannedOWsPharmaResponse {
        return dataRetrofitApi.plannedOWsDataAsync(headers).await()
    }

    override suspend fun uploadActualVisitsData(
        headers: Map<String, String>,
        uploadedListObj: UploadedActualVisitsListModel
    ): ActualVisitPharmaResponse {
        println("Uploaded List : ${uploadedListObj.visits}")
        println("Uploaded List : ${Gson().toJson(uploadedListObj.visits)}")
        return dataRetrofitApi.uploadActualVisitAsync(headers, uploadedListObj).await()
    }

    override suspend fun uploadOfficeWorksData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOfficeWorksListModel
    ): OfficeWorkPharmaResponse {
        println("Uploaded List : $uploadedListObj")
        println("Uploaded List : ${Gson().toJson(uploadedListObj)}")
        return dataRetrofitApi.uploadOfficeWorkAsync(headers, uploadedListObj).await()
    }

    override suspend fun uploadVacationsData(
        headers: Map<String, String>,
        uploadedListObj: UploadedVacationsListModel
    ): VacationPharmaResponse {
        println("Uploaded List : $uploadedListObj")
        println("Uploaded List : ${Gson().toJson(uploadedListObj)}")
        return dataRetrofitApi.uploadVacationAsync(
            headers,
            uploadedListObj.getUploadedTwoDimensionalFileListPart(),
            uploadedListObj.getUploadedIdListPart(),
            uploadedListObj.getUploadedOfflineIdListPart(),
            uploadedListObj.getUploadedVacationTypeIdListPart(),
            uploadedListObj.getUploadedDurationTypeIdListPart(),
            uploadedListObj.getUploadedShiftIdListPart(),
            uploadedListObj.getUploadedDateFromListPart(),
            uploadedListObj.getUploadedDateToListPart(),
            uploadedListObj.getUploadedNoteListPart()
        ).await()
    }

    override suspend fun uploadNewPlansData(
        headers: Map<String, String>,
        uploadedListObj: UploadedNewPlansListModel
    ): NewPlanPharmaResponse {
        println("Uploaded List : ${uploadedListObj.plans}")
        return dataRetrofitApi.uploadNewPlanAsync(headers, uploadedListObj).await()
    }

    override suspend fun uploadOfflineLogData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOfflineLogsListModel
    ): OfflineLogPharmaResponse {
        println("Uploaded List : ${uploadedListObj.logs}")
        return dataRetrofitApi.uploadOfflineLogAsync(headers, uploadedListObj).await()
    }

    override suspend fun uploadOfflineLocData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOfflineLocsListModel
    ): OfflineLocPharmaResponse {
        println("Uploaded List : ${uploadedListObj.logs}")
        return dataRetrofitApi.uploadOfflineLocAsync(headers, uploadedListObj).await()
    }

    override suspend fun uploadOnlineLogData(
        headers: Map<String, String>,
        uploadedListObj: UploadedOnlineLogsListModel
    ): OnlineLogPharmaResponse {
        println("Uploaded List : ${uploadedListObj.logs}")
        return dataRetrofitApi.uploadOnlineLogAsync(headers, uploadedListObj).await()
    }
}