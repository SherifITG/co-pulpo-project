package com.itgates.ultra.pulpo.cira.locationPackage

import android.location.Location
import com.itgates.ultra.pulpo.cira.ui.activities.MainActivity
import kotlinx.coroutines.flow.Flow
import java.util.*

interface LocationClient {
    fun getLocationUpdates(interval: Long, activity: MainActivity): Flow<LocationInfo>

    data class LocationInfo(
        val location: Location?,
        val errorMessage: String,
        val date: Date,
    )
}