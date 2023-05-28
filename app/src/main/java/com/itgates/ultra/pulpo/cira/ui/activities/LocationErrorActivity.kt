package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.composeUI.TextFactory
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.Globals
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.ui.composeUI.ButtonFactory
import com.itgates.ultra.pulpo.cira.ui.composeUI.ButtonFactoryNoContent
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.utilities.Utilities.checkLocationPermission
import com.itgates.ultra.pulpo.cira.utilities.Utilities.hasLocationPermission

@AndroidEntryPoint
class LocationErrorActivity : ComponentActivity() {

    var isPermissionError = false

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedHandler()
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name)) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier
                                    .fillMaxWidth(0.45F)
                                    .aspectRatio(1F),
                                painter = painterResource(R.drawable.location_off_icon),
                                contentDescription = "Nav Icon",
                                tint = ITGatesPrimaryColor
                            )
                            Spacer(modifier = Modifier.height(padding_30))
                            Globals.trustedLocationInfo?.errorMessage?.let { TextFactory(text = it) }
                            Spacer(modifier = Modifier.height(padding_30))
                            Globals.trustedLocationInfo?.errorMessage?.let {
                                if (it == "Missing location permission") {
                                    isPermissionError = true

                                    if (Utilities.shouldShowPermissionRationale(this@LocationErrorActivity)) {
                                        ButtonFactoryNoContent(text = "Give app permission", withPercentage = 0.8F) {
                                            applicationContext.checkLocationPermission(this@LocationErrorActivity)
                                        }
                                    }
                                    else {
                                        ButtonFactoryNoContent(text = "Give app permission", withPercentage = 0.8F) {
                                            // Function to navigate to app settings
                                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            val uri = Uri.fromParts("package", packageName, null)
                                            intent.data = uri
                                            startActivity(intent)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onBackPressedHandler() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                println("$isPermissionError  ${applicationContext.hasLocationPermission()}")
                if (isPermissionError && applicationContext.hasLocationPermission()) {
                    Utilities.navigateToMainActivity(applicationContext)
                }
                else {
                    finishAffinity()
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

}