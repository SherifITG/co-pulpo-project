package com.itgates.co.pulpo.ultra.repository

import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedActualVisitModel
import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedNewPlanModel
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.network.retrofit.ConfigDataRetrofitApi
import com.itgates.co.pulpo.ultra.network.retrofit.DataRetrofitApi
import com.itgates.co.pulpo.ultra.network.retrofit.FilesDataRetrofitApi
import okhttp3.ResponseBody
import javax.inject.Inject

class ConfigDataRepoImpl @Inject constructor(
    private val configDataRetrofitApi: ConfigDataRetrofitApi,
): ConfigDataRepo {
    override suspend fun fetchCompanyConfigurationByPIN(
        headers: Map<String, String>,
        pin: String
    ): ConfigurationsResponse {
        return configDataRetrofitApi.fetchConfigurationsAsync(headers, pin).await()
    }
}