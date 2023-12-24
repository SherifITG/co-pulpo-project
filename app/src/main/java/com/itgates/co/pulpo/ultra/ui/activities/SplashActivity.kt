package com.itgates.co.pulpo.ultra.ui.activities

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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.OnlineConfigurationData
import com.itgates.co.pulpo.ultra.onlineLogService.OnlineDebuggingManager
import com.itgates.co.pulpo.ultra.roomDataBase.converters.*
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.time.LocalDate
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
                                println("..............................................................")
                                println(dataStoreService.getDataObjAsync(PreferenceKeys.CONFIGS).await())
                                val configString = dataStoreService.getDataObjAsync(PreferenceKeys.CONFIGS).await()

                                // delay for the splash time
                                delay(700L)

                                if (configString.isEmpty()) {
                                    startActivity(Intent(this@SplashActivity, PINActivity::class.java))
                                }
                                else {
                                    NetworkConfiguration.configuration = Gson().fromJson(configString, OnlineConfigurationData::class.java)
                                    println("-------------------------------------------------------------------")
                                    println(NetworkConfiguration.configuration.link)
                                    if (dataStoreService.getDataObjAsync(PreferenceKeys.REMEMBER_ME).await().toBoolean()) {
                                        Utilities.navigateToMainActivity(this@SplashActivity)
                                    }
                                    else {
                                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                                    }
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

        val roomMultipleListsModule = RoomMultipleListsModule(
            listOf(
                RoomProductModule(
                    1L, "p1", 5L, 2,
                    "ok", "ok f", "ok m", "ok m",
                    1, true, "2027-06-20"
                ),
                RoomProductModule(
                    2L, "p2", 5L, 2,
                    "ok", "ok f", "ok m", "ok m",
                    1, true, "2027-06-20"
                )
            ),
            listOf(
                RoomGiveawayModule(1L, 2),
                RoomGiveawayModule(2L, 4),
            ),
            listOf(
                RoomManagerModule(1L, "Mohamed"),
                RoomManagerModule(2L, "Ahmed"),
            )

        )
        val converters = Converters()
        println("----------------------------------------------------------- ioioioioioioio ...")
        println(converters.fromMultipleListsToString(roomMultipleListsModule))
        println(Gson().toJson(listOf(1, 5, 7, 96, 5789)).contains(",5,"))
        println(Gson().toJson(listOf(1, 5, 7, 96, 5789)).contains(",8,"))
        println(Gson().toJson(listOf(1, 5, 7, 96, 5789)).contains("8"))

        println("---------------------------------------------------------------------------------")
        println(Utilities.getAndroidId(applicationContext))
        println(Utilities.getDeviceName())

        println("--------------------------------------------------------------------------------- START")
        println(Utilities.convertLocalDateToDate(LocalDate.now()))
        println(Utilities.convertLocalDateToDate(LocalDate.now()).time)
        println(LocalDate.now())
        println(Date())
        println(GlobalFormats.getFullDate(Locale.getDefault(), Utilities.convertLocalDateToDate(LocalDate.now())))
        println("----------------------------------------------------------------------------------- END")

        val instance = FirebaseCrashlytics.getInstance()
        instance.setUserId("1235")
        instance.log("item2")

        // Get the FirebaseAnalytics instance
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true)

        // Log a custom event
        val params = Bundle()
        params.putString("custom_param", "custom_value")
        mFirebaseAnalytics.logEvent("sherif", params)

        val crashlytics = Firebase.crashlytics
        crashlytics.recordException(Throwable("kill your fear"))

        OnlineDebuggingManager.enableFirebaseAnalytics(applicationContext)
    }
}