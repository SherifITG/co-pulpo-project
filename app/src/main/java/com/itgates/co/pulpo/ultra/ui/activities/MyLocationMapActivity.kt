package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.locationPackage.DefaultLocationClient
import com.itgates.co.pulpo.ultra.locationPackage.LocationClient
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Globals
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class MyLocationMapActivity : ComponentActivity() {
    private var locationChanged = MutableStateFlow(0)
    private lateinit var locationClient: LocationClient
    private val cacheViewModel: CacheViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulpoUltraTheme {
                Scaffold {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Box {
                            ComposeMapView()
                        }
                    }
                }
            }
        }
        observeOnLocation()
    }

    private fun observeOnLocation() {
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        locationClient.getLocationUpdates(5000, cacheViewModel.getDataStoreService()).onEach {
            locationChanged.value = (locationChanged.value.plus(1)).rem(5)
            println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooo ${locationChanged.value}")
        }.launchIn(lifecycleScope)
    }

    private fun getBitmapDescriptorFromLayout(context: Context, layoutResourceId: Int): BitmapDescriptor {
        val view = LayoutInflater.from(context).inflate(layoutResourceId, null)
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @Composable
    fun ComposeMapView() {
        val isLocationChanged = locationChanged.collectAsState()
        when (isLocationChanged.value) {
            in 0..5 -> {
                val locationInfo = Globals.trustedLocationInfo
                if (locationInfo == null) {
                    // loading
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    val myLocation = LatLng(locationInfo.location?.latitude?: 0.0, locationInfo.location?.longitude?: 0.0)
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(myLocation, 10F)
                    }

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = myLocation),
                            title = null,
                            snippet = null,
                            icon = getBitmapDescriptorFromLayout(LocalContext.current, R.layout.custom_marker )
                        )
                    }
                }
            }
        }
    }
}