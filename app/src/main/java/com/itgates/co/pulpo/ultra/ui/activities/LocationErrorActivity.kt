package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Globals
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.composeUI.*
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.utilities.Utilities.checkLocationPermission
import com.itgates.co.pulpo.ultra.utilities.Utilities.hasLocationPermission

@AndroidEntryPoint
class LocationErrorActivity : ComponentActivity() {

    var isPermissionError = false

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name), goToHomeContext = this) }
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

                                    ButtonFactoryNoContent(text = "Give app permission", withPercentage = 0.8F) {
                                        applicationContext.checkLocationPermission(this@LocationErrorActivity)
                                    }

                                    Spacer(modifier = Modifier.height(padding_8))
                                    Box(modifier = Modifier.padding(horizontal = padding_36)) {
                                        MultiLineTextFactory(
                                            text = "If you clicked the 'Give app permission' Button and the permission popup dialog not appear this mean that you disabled the location permission 3 time before or do some thing else like this, you can now got to the setting and enable it manually"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(padding_4))
                                    ClickableTextWithFunction()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionError && applicationContext.hasLocationPermission()) {
            Utilities.navigateToMainActivity(applicationContext)
        }
    }

    override fun onRestart() {
        super.onRestart()
        println("---------------------------------onRestart")
    }

    @Composable
    fun ClickableTextWithFunction() {
        val color = remember {
            mutableStateOf(ITGatesPrimaryColor)
        }
        Text(
            text = "Go to setting",
            textDecoration =  TextDecoration.Underline,
            color = color.value,

            textAlign = TextAlign.Center,
            fontSize = defaultTextSize,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable {
                // Handle the click action here
                color.value = ITGatesSecondaryColor

                // Function to navigate to app settings
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        )
    }
}