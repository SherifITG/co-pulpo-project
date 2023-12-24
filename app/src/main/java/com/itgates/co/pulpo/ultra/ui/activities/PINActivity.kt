package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Utilities.checkOnlineState
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.ConfigurationsResponse
import com.itgates.co.pulpo.ultra.ui.composeUI.*
import com.itgates.co.pulpo.ultra.viewModels.ConfigViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PINActivity : ComponentActivity() {
    private val configViewModel: ConfigViewModel by viewModels()
    private val internetStateFlow = MutableStateFlow(false)
    private val loadingStateFlow = MutableStateFlow(false)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name), isPinActivity = true) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = padding_16)
                            .padding(horizontal = padding_16)
                        ,
                        color = MaterialTheme.colors.background
                    ) {
                        PINPage(
                            internetStateFlow,
                            loadingStateFlow
                        ) { pin ->
                            if (pin == "0") {
                                Utilities.createCustomToast(applicationContext, "Invalid PIN")
                            }
                            else {
                                this@PINActivity.pinAction(pin)
                            }
                        }
                    }
                }
            }
        }

        setObservers()
    }

    private fun setObservers() {
        configViewModel.configData.observeForever {
            loadingStateFlow.value = false
            dealWithConfigResponse(it)
        }
    }

    private fun pinAction(pin: String) {
        if (checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            configViewModel.fetchCompanyConfigurationByPIN(pin)
        }
        else {
            internetStateFlow.value = true
        }
    }

    private fun dealWithConfigResponse(configurationsResponse: ConfigurationsResponse) {
        CoroutineManager.getScope().launch {
            if (configurationsResponse.data.isNotEmpty()) {
//                if (configurationsResponse.Data[0].system == "G") {
//                    Utilities.createCustomToast(
//                        applicationContext,
//                        "Pulpo Ultra system not support this pin, Please try again",
//                        R.drawable.polpo5
//                    )
//                    return@launch
//                }
                NetworkConfiguration.configuration = configurationsResponse.data[0]

                println("-------------------------------------------------------------------------")
                println(NetworkConfiguration.convertToString())

                NetworkConfiguration.configuration.adjustThePath()
                val dataStoreService = configViewModel.getDataStoreService()

                println("-------------------------------------------------------------------------")
                println(NetworkConfiguration.convertToString())

                dataStoreService.setDataObj(
                    PreferenceKeys.CONFIGS, NetworkConfiguration.convertToString()
                )
                println("---------------------------------------------------------")
                println(configurationsResponse.data)

                println(dataStoreService.getDataObjAsync(PreferenceKeys.REMEMBER_ME).await())
                dataStoreService.setDataObj(PreferenceKeys.REMEMBER_ME, false.toString())
//                if (dataStoreService.getDataObjAsync(PreferenceKeys.REMEMBER_ME).await().toBoolean()) {
//                    Utilities.navigateToMainActivity(this@PINActivity)
//                }
//                else {
//                    startActivity(Intent(this@PINActivity, LoginActivity::class.java))
//                }
                startActivity(Intent(this@PINActivity, LoginActivity::class.java))
                finish()
            }
            else {
                Utilities.createCustomToast(
                    applicationContext,
                    "Pulpo Ultra system not support this pin, Please try again",
                    R.drawable.polpo5
                )
            }
        }
    }

    @Composable
    fun PINPage(
        internetStateFlow: MutableStateFlow<Boolean>,
        loadingStateFlow: MutableStateFlow<Boolean>,
        confirmAction: (pin: String) -> Unit
    ) {
        val pinValue = remember { mutableStateOf("") }
        val pinHasError = remember { mutableStateOf(false) }

        Box {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomLottieAnimationView(R.raw.pin_lottie)
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(padding_16)
            ) {
                Spacer(modifier = Modifier.height(padding_250))
                CustomDefaultOutlinedTextField(myValue = pinValue, myHint = "PIN", hasError = pinHasError)
                ButtonFactory(text = "Confirm", withPercentage = 0.6F) {
                    val isPinEmpty = pinValue.value.trim().isEmpty()
                    if (isPinEmpty) {
                        pinHasError.value = true
                        return@ButtonFactory
                    }
                    confirmAction(pinValue.value)
                }
                NoInternetDialog(internetStateFlow)
                LoadingDialog(loadingStateFlow, "Loading")
            }
        }
    }

}