package com.itgates.ultra.pulpo.cira.repository

import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import com.itgates.ultra.pulpo.cira.network.retrofit.DataRetrofitApi
import com.itgates.ultra.pulpo.cira.network.retrofit.FilesDataRetrofitApi
import okhttp3.ResponseBody
import javax.inject.Inject


class OnlineDataRepoImpl @Inject constructor(
    private val dataRetrofitApi: DataRetrofitApi,
    private val filesDataRetrofitApi: FilesDataRetrofitApi
): OnlineDataRepo {

    suspend fun getFile(fileName: String): ResponseBody {
        return filesDataRetrofitApi.getFileAsync(fileName).await()
    }

    override suspend fun authenticationAction(headers: Map<String, String>,
                                    FN: String,
                                    username: String,
                                    password: String): LoginPharmaResponse {
        return dataRetrofitApi.pharmaAuthenticateUserAsync(headers, FN, username, password).await()
    }

    override suspend fun fetchMasterData(
        headers: Map<String, String>,
        FN: String,
        today: String,
        userId: String,
        lineId: String,
        divId: String
    ): MasterDataPharmaResponse {
        return dataRetrofitApi.masterDataAsync(headers, FN, today, userId, lineId, divId).await()
    }

    override suspend fun fetchAccountsAndDoctorsDetailsData(
        headers: Map<String, String>,
        FN: String,
        lineId: String,
        divId: String
    ): AccountsAndDoctorsDetailsPharmaResponse {
        println("OnlineDataRepoImpl ===================== $lineId line")
        println("OnlineDataRepoImpl ===================== $FN fn")
        println("OnlineDataRepoImpl ===================== $divId div")
        return dataRetrofitApi.accountsAndDoctorsDetailsAsync(headers, FN, lineId, divId).await()
    }

    override suspend fun fetchPresentationsAndSlidesDetailsData(
        headers: Map<String, String>,
        FN: String,
        lineId: String
    ): PresentationsAndSlidesDetailsPharmaResponse {
        return dataRetrofitApi.presentationsAndSlidesDetailsAsync(headers, FN, lineId).await()
    }

    override suspend fun fetchPlannedVisitsData(
        headers: Map<String, String>,
        FN: String,
        today: String,
        userId: String
    ): PlannedVisitsPharmaResponse {
        return dataRetrofitApi.plannedVisitsDataAsync(headers, FN, today, userId).await()
    }

    override suspend fun uploadActualVisitsData(
        headers: Map<String, String>,
        list: List<UploadedActualVisitModel>
    ): ActualVisitPharmaResponse {
        println("Uploaded List : $list")
        return dataRetrofitApi.uploadActualVisitAsync(headers, list).await()
//        return try{
//            dataRetrofitApi.uploadActualVisitAsync(headers, list).await()
//        }catch (ex:Exception){
//            ActualVisitPharmaResponse(listOf(), "", "")
//        }
    }

    override suspend fun uploadNewPlansData(
        headers: Map<String, String>,
        list: List<UploadedNewPlanModel>
    ): NewPlanPharmaResponse {
        println("Uploaded List : $list")
        return dataRetrofitApi.uploadNewPlanAsync(headers, list).await()
//        return try{
//            dataRetrofitApi.uploadActualVisitAsync(headers, list).await()
//        }catch (ex:Exception){
//            ActualVisitPharmaResponse(listOf(), "", "")
//        }
    }
}