package com.itgates.co.pulpo.ultra.network.models.responseModels.responses

import com.google.gson.annotations.SerializedName
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*

data class LoginPharmaResponse(
    val access_token: String?,
    val message: String,
    val status: Int
)

data class UserPharmaResponse(
    @SerializedName("data") val data: UserDetailsData,
    val message: String,
    val status: Int
)

data class MasterDataPharmaResponse(
    @SerializedName("data") val data: OnlineMasterData,
    val message: String,
    val status: Int
)

data class AccountsAndDoctorsDetailsPharmaResponse(
    @SerializedName("data") val data: OnlineAccountsAndDoctorsData,
    val message: String,
    val status: Int
)

data class PresentationsAndSlidesDetailsPharmaResponse(
    @SerializedName("data") val data: OnlinePresentationsAndSlidesData,
    val message: String,
    val status: Int
)

data class PlannedVisitsPharmaResponse(
    @SerializedName("data") val data: ArrayList<OnlinePlannedVisitData>,
    val message: String,
    val status: Int
)

data class PlannedOWsPharmaResponse(
    @SerializedName("data") val data: ArrayList<OnlinePlannedOWData>,
    val message: String,
    val status: Int
)

data class ActualVisitPharmaResponse(
    @SerializedName("data") val data: List<ActualVisitDTO>,
    val message: String,
    val status: Int
)

data class OfficeWorkPharmaResponse(
    @SerializedName("data") val data: List<OfficeWorkDTO>,
    val message: String,
    val status: Int
)

data class VacationPharmaResponse(
    @SerializedName("data") val data: List<VacationDTO>,
    val message: String,
    val status: Int
)

data class NewPlanPharmaResponse(
    @SerializedName("data") val data: List<NewPlanDTO>,
    val message: String,
    val status: Int
)

data class OfflineLogPharmaResponse(
    @SerializedName("data") val data: List<OfflineRecordDTO>,
    val message: String,
    val status: Int
)

data class OfflineLocPharmaResponse(
    @SerializedName("data") val data: List<OfflineRecordDTO>,
    val message: String,
    val status: Int
)

data class OnlineLogPharmaResponse(
    @SerializedName("data") val data: Int,
    val message: String,
    val status: Int
)

data class ConfigurationsResponse(
    @SerializedName("data") val data: List<OnlineConfigurationData>,
    val message: String,
    val status: Int
)