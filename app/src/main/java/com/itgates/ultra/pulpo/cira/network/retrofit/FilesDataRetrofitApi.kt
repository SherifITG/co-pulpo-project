package com.itgates.ultra.pulpo.cira.network.retrofit

import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.*

interface FilesDataRetrofitApi {

    @GET("{fileName}")
    fun getFileAsync(@Path("fileName") fileName: String): Deferred<ResponseBody>
//    @GET("1.pdf")
//    fun getFileAsync(): Deferred<ResponseBody>

}