package com.itgates.co.pulpo.ultra.network.retrofit

import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.network.models.requestModels.*
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DataRetrofitApi {

    @POST("login{endPointPrefix}")
    fun pharmaAuthenticateUserAsync(
        @HeaderMap headers: Map<String, String>,
        @Body userData: UploadedUserModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<Response<LoginPharmaResponse>>

    @GET("user_data{endPointPrefix}")
    fun pharmaUserDataAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<UserPharmaResponse>

    @GET("master_data{endPointPrefix}")
    fun masterDataAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<MasterDataPharmaResponse>

    @GET("accounts_doctors{endPointPrefix}")
    fun accountsAndDoctorsDetailsAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<AccountsAndDoctorsDetailsPharmaResponse>

    @GET("presentations_slides{endPointPrefix}")
    fun presentationsAndSlidesDetailsAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<PresentationsAndSlidesDetailsPharmaResponse>

    @GET("planned_visits{endPointPrefix}")
    fun plannedVisitsDataAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<PlannedVisitsPharmaResponse>

    @GET("planned_ow{endPointPrefix}")
    fun plannedOWsDataAsync(
        @HeaderMap headers: Map<String, String>,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<PlannedOWsPharmaResponse>

    @POST("store_actual_visits{endPointPrefix}")
    fun uploadActualVisitAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedActualVisitsListModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<ActualVisitPharmaResponse>

    @POST("store_Office_Works{endPointPrefix}")
    fun uploadOfficeWorkAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedOfficeWorksListModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<OfficeWorkPharmaResponse>

    @Multipart
    @POST("store_vacations{endPointPrefix}")
    fun uploadVacationAsync(
        @HeaderMap headers: Map<String, String>,
        @Part("images") images : List<List<MultipartBody.Part>>,
        @Part("ids") ids : List<RequestBody>,
        @Part("offline_ids") offlineIds : List<RequestBody>,
        @Part("vacation_type_ids") vacationTypeIds : List<RequestBody>,
        @Part("duration_type_ids") durationTypeIds : List<RequestBody>,
        @Part("shift_ids") shiftIds : List<RequestBody>,
        @Part("long_dates_from") longDatesFrom : List<RequestBody>,
        @Part("long_dates_to") longDatesTo : List<RequestBody>,
        @Part("notes") notes : List<RequestBody>
    ): Deferred<VacationPharmaResponse>

    @POST("store_plan_visits{endPointPrefix}")
    fun uploadNewPlanAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedNewPlansListModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<NewPlanPharmaResponse>

    @POST("{endPointPrefix}")
    fun uploadOfflineLogAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedOfflineLogsListModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<OfflineLogPharmaResponse>

    @POST("{endPointPrefix}")
    fun uploadOfflineLocAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedOfflineLocsListModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<OfflineLocPharmaResponse>








    // ---------------------------------------------------------------------------------------------
    // Logging
    @POST("online_log{endPointPrefix}")
    fun uploadOnlineLogAsync(
        @HeaderMap headers: Map<String, String>,
        @Body uploadedListObj: UploadedOnlineLogsListModel,
        @Path("endPointPrefix") prefix: String = NetworkConfiguration.getEndPointsPrefix()
    ): Deferred<OnlineLogPharmaResponse>

}