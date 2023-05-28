package com.itgates.ultra.pulpo.cira.di

import android.app.Application
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.network.retrofit.DataRetrofitApi
import com.google.gson.GsonBuilder
import com.itgates.ultra.pulpo.cira.network.retrofit.FilesDataRetrofitApi
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
    fun provideRetrofit(application: Application): Retrofit.Builder {
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

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(application.getString(R.string.BASE_URL_PHARMA))
    }

    @Singleton
    @Provides
    fun provideRequestService(retrofit: Retrofit.Builder): DataRetrofitApi {
        return retrofit
            .build()
            .create(DataRetrofitApi::class.java)
    }

    // ---------------------------------------------------------------------------

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
            .baseUrl(application.getString(R.string.FILES_BASE_URL_PHARMA))

        return retrofit
            .build()
            .create(FilesDataRetrofitApi::class.java)
    }


}