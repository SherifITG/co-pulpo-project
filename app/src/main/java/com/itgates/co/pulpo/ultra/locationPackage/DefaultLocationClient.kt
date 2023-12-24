package com.itgates.co.pulpo.ultra.locationPackage

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.ui.activities.MainActivity
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.utilities.Utilities.hasLocationPermission
import com.google.android.gms.location.*
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.Date
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService

class DefaultLocationClient (
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
): LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long, dataStoreService: DataStoreService): Flow<LocationClient.LocationInfo> {
        return callbackFlow {
            // Create a location request object
            val request = LocationRequest.Builder(interval).build()

            if (!context.hasLocationPermission()) {
                launch {
                    send(
                        LocationClient.LocationInfo(
                            location = null,
                            errorMessage = context.getString(R.string.missing_location_permission),
                            Date()
                        )
                    )
                }
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGpsEnabled && !isNetworkEnabled) {
                launch {
                    send(
                        LocationClient.LocationInfo(
                            location = null,
                            errorMessage = context.getString(R.string.gps_is_disabled),
                            Date()
                        )
                    )
                }
            }

//            if (context.hasLocationPermission() && (isGpsEnabled || isNetworkEnabled)) {
            if (context.hasLocationPermission() && isGpsEnabled && isNetworkEnabled) {
                locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).let {location ->
                    if (location == null) {
                        /* Caching the location
                        val cacheLocationText = dataStoreService.getDataObjAsync(PreferenceKeys.CACHE_LOCATION).await()
                        if (cacheLocationText.isNotEmpty()) {
                            val cacheLocation = Gson().fromJson(cacheLocationText, Location::class.java)
                            send(
                                LocationClient.LocationInfo(
                                    location = cacheLocation,
                                    errorMessage = context.getString(R.string.fine_location_text),
                                    Date()
                                )
                            )
                        } else {
                            send(
                                LocationClient.LocationInfo(
                                    location = null,
                                    errorMessage = context.getString(R.string.no_location_provider_or_cached),
                                    Date()
                                )
                            )
                        }
                        */
                        send(
                            LocationClient.LocationInfo(
                                location = null,
                                errorMessage = context.getString(R.string.no_location_provided),
                                Date()
                            )
                        )
                    }
                    else {
                        launch {
                            send(
                                LocationClient.LocationInfo(
                                    location = location,
                                    errorMessage = context.getString(R.string.fine_location_text),
                                    Date()
                                )
                            )
                        }
                    }

                }
            }
            // TODO later check "Some error with location" case at hte start

            // Create a location callback object
            val locationCallback = object : LocationCallback() {
                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    println(".'.'.'.'.'.'.'.'.'..'.'.'.'.'.'.'.'.'.'.'.'.'.'.'.'. :: onLocationAvailability")
                    // Check if location data is available
                    if (!locationAvailability.isLocationAvailable) {
                        // Location data is unavailable
                        if (!context.hasLocationPermission()) {
                            launch {
                                send(
                                    LocationClient.LocationInfo(
                                        location = null,
                                        errorMessage = context.getString(R.string.missing_location_permission),
                                        Date()
                                    )
                                )
                            }
                            return
                        }

                        val locationManager2 = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        val isGpsEnabled2 = locationManager2.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        val isNetworkEnabled2 = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        if (!isGpsEnabled2 && !isNetworkEnabled2) {
                            launch {
                                send(
                                    LocationClient.LocationInfo(
                                        location = null,
                                        errorMessage = context.getString(R.string.gps_is_disabled),
                                        Date()
                                    )
                                )
                            }
                            return
                        }
                    }
                }

                override fun onLocationResult(locationResult: LocationResult) {
                    // Process the location result
                    // Do something with the location
                    locationResult.locations.lastOrNull()?.let { location ->
                        if (Utilities.isFromMockLocation(location)) {
                            launch {
                                send(
                                    LocationClient.LocationInfo(
                                        location = null,
                                        errorMessage = context.getString(R.string.from_lock_location_app),
                                        Date()
                                    )
                                )
                            }
                            return
                        }

                        if (Utilities.isFromMockLocation(location)) {
                            launch {
                                send(
                                    LocationClient.LocationInfo(
                                        location = null,
                                        errorMessage = context.getString(R.string.lock_location_app_running),
                                        Date()
                                    )
                                )
                            }
                            return
                        }

                        launch {
                            send(
                                LocationClient.LocationInfo(
                                    location = location,
                                    errorMessage = context.getString(R.string.fine_location_text),
                                    Date()
                                )
                            )
                        }
                    }
                }
            }

            // Request location updates
            locationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                locationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}