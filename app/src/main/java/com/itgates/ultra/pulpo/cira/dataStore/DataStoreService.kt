package com.itgates.ultra.pulpo.cira.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.itgates.ultra.pulpo.cira.CoroutineManager
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

    fun getDataObjAsync(key: Preferences.Key<String>): Deferred<String> = CoroutineManager.getScope().async {
        val dataPreferenceKey = dataStoreRepository.data.first()
        dataPreferenceKey[key] ?: ""
    }



    suspend fun getDivisionsOrLinesList(key: Preferences.Key<String>): List<Long> {
        return convertDivisionsOrLinesTextToList(getDataObjAsync(key).await())
    }

    fun saveDivisionsOrLinesText(key: Preferences.Key<String>, value: String) {
        setDataObj(key, updateDivisionsOrLinesText(value))
    }

    private fun convertDivisionsOrLinesTextToList(divisionsText: String): List<Long> =
        divisionsText.trim().split(",").stream().map { it.trim().toLong() }.toList()

    private fun updateDivisionsOrLinesText(divisionsText: String): String =
        divisionsText.trim().replace("-", ",")
}