package com.itgates.co.pulpo.ultra.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.itgates.co.pulpo.ultra.CoroutineManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.streams.toList

class DataStoreService @Inject constructor(private val dataStoreRepository: DataStore<Preferences>) {
    fun setDataObj(key: Preferences.Key<String>, value: String) {
        CoroutineManager.getScope().launch {
            dataStoreRepository.edit { settings ->
                settings[key] = value
            }
        }
    }

    fun setDataBooleanObj(key: Preferences.Key<Boolean>, value: Boolean) {
        CoroutineManager.getScope().launch {
            dataStoreRepository.edit { settings ->
                settings[key] = value
            }
        }
    }

    fun getDataObjAsync(key: Preferences.Key<String>): Deferred<String> = CoroutineManager.getScope().async {
        val dataPreferenceKey = dataStoreRepository.data.first()
        dataPreferenceKey[key] ?: ""
    }

    fun getDataBooleanObjAsync(key: Preferences.Key<Boolean>): Deferred<Boolean> = CoroutineManager.getScope().async {
        val dataPreferenceKey = dataStoreRepository.data.first()
        dataPreferenceKey[key] ?: false
    }
}