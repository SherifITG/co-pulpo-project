package com.itgates.co.pulpo.ultra.locationPackage

import android.location.Location
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.ui.activities.MainActivity
import kotlinx.coroutines.flow.Flow
import java.util.*

interface LocationClient {
    fun getLocationUpdates(interval: Long, dataStoreService: DataStoreService): Flow<LocationInfo>

    data class LocationInfo(
        val location: Location?,
        val errorMessage: String,
        val date: Date,
    )
}