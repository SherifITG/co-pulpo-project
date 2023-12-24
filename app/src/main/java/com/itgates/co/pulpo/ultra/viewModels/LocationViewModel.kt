package com.itgates.co.pulpo.ultra.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.repository.OfflineDataRepo
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationViewModel @Inject constructor(
    private val offlineDataRepo: OfflineDataRepo,
    private val dataStoreService: DataStoreService
) {

    private val _lastOfflineLoc = MutableLiveData<OfflineLoc>()
    val lastOfflineLoc: LiveData<OfflineLoc> get() = _lastOfflineLoc

    fun saveOfflineLoc(offlineLoc: OfflineLoc) {
        CoroutineManager.getScope().launch {
            try {
                if (offlineLoc.userIdf == 0L)
                    offlineLoc.userIdf = dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await().toLong()
                offlineDataRepo.saveOfflineLoc(offlineLoc)
                Log.d(
                    "CacheViewModel",
                    "saveOfflineLoc: success (${offlineLoc.ll}, ${offlineLoc.lg})"
                )
            } catch (e: Exception) {
                Log.d("CacheViewModel", "saveOfflineLoc: failed $e")
            }
        }
    }

    fun getLastOfflineLoc() {
        CoroutineManager.getScope().launch {
            val dummyOfflineLoc = OfflineLoc(
                -1L, -1L, -1, -1, "-1L",
                "---", "---", "---", "---",
                false, "---", -1L
            )
            try {
                _lastOfflineLoc.value = offlineDataRepo.getLastOfflineLoc() ?: dummyOfflineLoc
            } catch (e: Exception) {
                _lastOfflineLoc.value = dummyOfflineLoc
                Log.d("CacheViewModel", "getLastOfflineLoc: failed $e")
            }
        }
    }
}