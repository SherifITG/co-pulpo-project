package com.itgates.ultra.pulpo.cira.network.retrofit

import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface DataRetrofitApi {

    @POST("index.php")
    fun pharmaAuthenticateUserAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("FN") FN: String,
        @Query("username") CheckLogin: String,
        @Query("password") password: String
    ): Deferred<LoginPharmaResponse>

    @GET("index.php")
    fun masterDataAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("FN") FN: String,
        @Query("today") today: String,
        @Query("userId") userId: String,
        @Query("lineId") lineId: String,
        @Query("divId") divId: String
    ): Deferred<MasterDataPharmaResponse>

    @GET("index.php")
    fun accountsAndDoctorsDetailsAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("FN") FN: String,
        @Query("lineId") lineId: String,
        @Query("divId") divId: String
    ): Deferred<AccountsAndDoctorsDetailsPharmaResponse>

    @GET("index.php")
    fun presentationsAndSlidesDetailsAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("FN") FN: String,
        @Query("teamId") lineId: String
    ): Deferred<PresentationsAndSlidesDetailsPharmaResponse>

    @GET("index.php")
    fun plannedVisitsDataAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("FN") FN: String,
        @Query("today") today: String,
        @Query("userId") userId: String
    ): Deferred<PlannedVisitsPharmaResponse>

    @POST("_visit.php")
    fun uploadActualVisitAsync(
        @HeaderMap headers: Map<String, String>,
        @Body List: List<UploadedActualVisitModel>
    ): Deferred<ActualVisitPharmaResponse>

    @POST("_planned.php")
    fun uploadNewPlanAsync(
        @HeaderMap headers: Map<String, String>,
        @Body List: List<UploadedNewPlanModel>
    ): Deferred<NewPlanPharmaResponse>

}