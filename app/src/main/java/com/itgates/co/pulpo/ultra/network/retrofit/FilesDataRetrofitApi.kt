package com.itgates.co.pulpo.ultra.network.retrofit

import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface FilesDataRetrofitApi {

    @GET("{fileName}")
    fun getFileAsync(@Path("fileName") fileName: String): Deferred<ResponseBody>
//    @GET("1.pdf")
//    fun getFileAsync(): Deferred<ResponseBody>

}