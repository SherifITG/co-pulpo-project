package com.itgates.co.pulpo.ultra.network.retrofit

import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface ConfigDataRetrofitApi {

    @GET("config.php")
    fun fetchConfigurationsAsync(
        @HeaderMap headers: Map<String, String>,
        @Query("pin") pin: String,
    ): Deferred<ConfigurationsResponse>

}