package com.itgates.co.pulpo.ultra.repository

import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedActualVisitModel
import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedNewPlanModel
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*

interface ConfigDataRepo {
    suspend fun fetchCompanyConfigurationByPIN(
        headers: Map<String, String>,
        pin: String
    ): ConfigurationsResponse
}