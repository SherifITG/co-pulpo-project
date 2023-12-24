package com.itgates.co.pulpo.ultra.viewModels

import android.util.Log
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedOnlineLogsListModel
import com.itgates.co.pulpo.ultra.repository.OnlineDataRepo
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class OnlineLogViewModel @Inject constructor(
    private val onlineDataRepo: OnlineDataRepo,
    private val dataStoreService: DataStoreService
) {

    fun uploadOnlineDebuggingData(uploadedListObj: UploadedOnlineLogsListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                val response = onlineDataRepo.uploadOnlineLogData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadOnlineLogData -> $response")
            } catch (e: Exception) {
//                _uploadedOfflineLogData.value = OfflineLogPharmaResponse(
//                    data = ArrayList(),
//                    status = 404,
//                    message = "your internet is poor"
//                )
                Log.d("ServerViewModel", "uploadOnlineLogData: failed $e")
            }
        }
    }

    private fun getHeaders(authorizationToken: String? = null): HashMap<String, String> {
        val headers: HashMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["company"] = NetworkConfiguration.configuration.name.lowercase(Locale.ROOT)
        if (authorizationToken != null) {
            headers["Authorization"] = "Bearer $authorizationToken"
        }

        return headers
    }

}