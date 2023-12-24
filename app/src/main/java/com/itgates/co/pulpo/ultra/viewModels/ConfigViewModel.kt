package com.itgates.co.pulpo.ultra.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.repository.ConfigDataRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val configDataRepo: ConfigDataRepoImpl,
    private val dataStoreService: DataStoreService
) : ViewModel() {
    private val _configData = MutableLiveData<ConfigurationsResponse>()
    val configData: LiveData<ConfigurationsResponse> get() = _configData

    fun getDataStoreService(): DataStoreService = this.dataStoreService

    fun fetchCompanyConfigurationByPIN(pin: String) {
        CoroutineManager.getScope().launch {
            try {
                _configData.value = configDataRepo.fetchCompanyConfigurationByPIN(
                    getHeaders(),
                    pin
                )
            } catch (e: Exception) {
                _configData.value = ConfigurationsResponse(
                    data = listOf(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "fetchCompanyConfigurationByPIN failed $e")
            }
        }
    }


    private fun getHeaders(): HashMap<String, String>  {
        val headers: HashMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        return headers
    }
}