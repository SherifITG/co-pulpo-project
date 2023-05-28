package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import kotlinx.coroutines.delay
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
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
                            LaunchedEffect(key1 = true) {
                                val dataStoreService = cacheViewModel.getDataStoreService()
                                delay(700L)

                                println(dataStoreService.getDataObjAsync(PreferenceKeys.REMEMBER_ME).await())
                                if (dataStoreService.getDataObjAsync(PreferenceKeys.REMEMBER_ME).await().toBoolean()) {
                                    Utilities.navigateToMainActivity(this@SplashActivity)
                                }
                                else {
                                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                }
                                finish()

                            }

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Box {
                                    Icon(
                                        modifier = Modifier
                                            .fillMaxWidth(0.45F)
                                            .aspectRatio(1F),
                                        painter = painterResource(R.drawable.polpo5),
                                        contentDescription = "App Bar Icon",
                                        tint = ITGatesPrimaryColor
                                    )
                                    Icon(
                                        modifier = Modifier
                                            .fillMaxWidth(0.45F)
                                            .aspectRatio(1F),
                                        painter = painterResource(R.drawable.polpo6),
                                        contentDescription = "App Bar Icon",
                                        tint = ITGatesGreyColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}