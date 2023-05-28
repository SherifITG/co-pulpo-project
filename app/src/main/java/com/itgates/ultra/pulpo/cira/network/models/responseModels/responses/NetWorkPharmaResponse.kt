package com.itgates.ultra.pulpo.cira.network.models.responseModels.responses

import com.google.gson.annotations.SerializedName
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*

data class LoginPharmaResponse(
    @SerializedName("Data") val Data: ArrayList<UserDetailsData>,
    val Status_Message: String,
    val Status: Int
)

data class MasterDataPharmaResponse(
    @SerializedName("Data") val Data: OnlineMasterData,
    val Status_Message: String,
    val Status: Int
)

data class AccountsAndDoctorsDetailsPharmaResponse(
    @SerializedName("Data") val Data: OnlineAccountsAndDoctorsData,
    val Status_Message: String,
    val Status: Int
)

data class PresentationsAndSlidesDetailsPharmaResponse(
    @SerializedName("Data") val Data: OnlinePresentationsAndSlidesData,
    val Status_Message: String,
    val Status: Int
)

data class PlannedVisitsPharmaResponse(
    @SerializedName("Data") val Data: ArrayList<OnlinePlannedVisitData>,
    val Status_Message: String,
    val Status: Int
)

data class ActualVisitPharmaResponse(
    @SerializedName("Data") val Data: List<ActualVisitDTO>,
    val Status_Message: String,
    val Status: Int
)