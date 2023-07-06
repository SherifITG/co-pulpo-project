package com.itgates.ultra.pulpo.cira.repository

import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*

interface OnlineDataRepo {

    suspend fun authenticationAction(
        headers: Map<String, String>,
        FN: String,
        username: String,
        password: String
    ): LoginPharmaResponse

    suspend fun fetchMasterData(
        headers: Map<String, String>,
        FN: String,
        today: String,
        userId: String,
        lineId: String,
        divId: String
    ): MasterDataPharmaResponse

    suspend fun fetchAccountsAndDoctorsDetailsData(
        headers: Map<String, String>,
        FN: String,
        lineId: String,
        divId: String
    ): AccountsAndDoctorsDetailsPharmaResponse

    suspend fun fetchPresentationsAndSlidesDetailsData(
        headers: Map<String, String>,
        FN: String,
        lineId: String
    ): PresentationsAndSlidesDetailsPharmaResponse

    suspend fun fetchPlannedVisitsData(
        headers: Map<String, String>,
        FN: String,
        today: String,
        userId: String
    ): PlannedVisitsPharmaResponse

    suspend fun uploadActualVisitsData(
        headers: Map<String, String>,
        list: List<UploadedActualVisitModel>
    ): ActualVisitPharmaResponse

    suspend fun uploadNewPlansData(
        headers: Map<String, String>,
        list: List<UploadedNewPlanModel>
    ): NewPlanPharmaResponse
}