package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import java.util.*

@AndroidEntryPoint
class MapActivity : ComponentActivity() {
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
    }

    @Composable
    fun ComposeMapView() {
        val city = LatLng(PassedValues.mapActivity_ll, PassedValues.mapActivity_lg)
//        val city = LatLng(31.5, 30.0)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(city, 10F)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (PassedValues.mapActivity_docName == "NO_DOCTOR_ACCOUNT_ONLY") {
                Marker(
                    state = MarkerState(position = city),
                    title = PassedValues.mapActivity_accName + " : ${PassedValues.mapActivity_ll} , ${PassedValues.mapActivity_lg}",
                )
            }
            else {
                Marker(
                    state = MarkerState(position = city),
                    title = PassedValues.mapActivity_accName + " : ${PassedValues.mapActivity_ll} , ${PassedValues.mapActivity_lg}",
                    snippet = PassedValues.mapActivity_docName
                )
            }
//            Marker(
//                state = MarkerState(position = city),
//                title = "fire",
//                snippet = "f"
//            )
        }
    }
}