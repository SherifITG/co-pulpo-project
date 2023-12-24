package com.itgates.co.pulpo.ultra.di

import android.app.Application
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.network.retrofit.DataRetrofitApi
import com.google.gson.GsonBuilder
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.network.retrofit.ConfigDataRetrofitApi
import com.itgates.co.pulpo.ultra.network.retrofit.FilesDataRetrofitApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRequestService(): DataRetrofitApi {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .readTimeout(35, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder()
//                    .header(
//                        "Authorization", ""
//                    )
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }.build()

        val retrofit =  Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl("${NetworkConfiguration.configuration.link}/api/")

        return retrofit
            .build()
            .create(DataRetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFilesRequestService(application: Application): FilesDataRetrofitApi {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .readTimeout(35, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder().header(
                    "Authorization", ""
                )
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }.build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(NetworkConfiguration.configuration.link)

        return retrofit
            .build()
            .create(FilesDataRetrofitApi::class.java)
    }

    @Singleton
    @Provides
    fun provideConfigurationRequestService(application: Application): ConfigDataRetrofitApi {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(35, TimeUnit.SECONDS)
            .writeTimeout(35, TimeUnit.SECONDS)
            .readTimeout(35, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder().header(
                    "Authorization", ""
                )
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }.build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(application.getString(R.string.CONFIGURATION_SERVER_URL))

        return retrofit
            .build()
            .create(ConfigDataRetrofitApi::class.java)
    }


}