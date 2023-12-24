package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.itgates.co.pulpo.ultra.ui.activities.*
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.composeUI.*
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.ui.utils.BaseDataActivity
import com.itgates.co.pulpo.ultra.utilities.Utilities
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NewUserDataActivity : BaseDataActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                        Column {
                            MyScreen()
                            NoInternetDialog(internetStateFlow)
                            CustomLoadingDialog(
                                openLoadingStateFlow,
                                loadingStateFlow,
                                titleFlow,
                                this@NewUserDataActivity
                            ) {
                                Utilities.navigateToMainActivity(this@NewUserDataActivity)
                            }
                        }
                    }
                }
            }
        }

        setDataObservers()
//        loadNewUserDataIfNeeded()
        updateAllData()
    }

//    private fun loadNewUserDataIfNeeded() {
//        CoroutineManager.getScope().launch(Dispatchers.Main) {
//            loadingAllStateFlow.value = 1 // fired
//            loadingStateFlow.value = true
//            delay(1500)
//            updateAccountAndDoctorData()
//            updatePlannedVisitData()
//            updatePlannedOWData()
//            updateMasterData()
//        }
//    }

    @Composable
    fun MyScreen() {
        Column {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator()
            }
        }
    }
}