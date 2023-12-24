package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.itgates.co.pulpo.ultra.enumerations.DataStatus
import com.itgates.co.pulpo.ultra.onlineLogService.OnlineDebuggingManager
import com.itgates.co.pulpo.ultra.onlineLogService.OnlineDebuggingService
import com.itgates.co.pulpo.ultra.roomDataBase.converters.*
import com.itgates.co.pulpo.ultra.ui.composeUI.ButtonFactory
import com.itgates.co.pulpo.ultra.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*

@AndroidEntryPoint
class OnlineDebuggingActivity : ComponentActivity() {

    private val dataStateFlow = MutableStateFlow(DataStatus.DONE)

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
                        val status = dataStateFlow.collectAsState()
                        when(status.value) {
                            DataStatus.LOADING -> {}
                            DataStatus.DONE -> { DebuggingScreen() }
                            DataStatus.REFRESH -> { DebuggingScreen() }
                            DataStatus.ERROR -> {}
                            DataStatus.NO_DATA -> {}
                        }
                    }
                }
            }
        }
    }

    private fun runOnlineLogService() {
        OnlineDebuggingManager.log("tag", "message")
        val serviceIntent = Intent(this, OnlineDebuggingService::class.java)
        startService(serviceIntent)
//        ContextCompat.startForegroundService(this, serviceIntent)
    }

    @Composable
    private fun DebuggingScreen() {
        val isDebuggingEnabled = OnlineDebuggingManager.isEnabled
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isDebuggingEnabled || OnlineDebuggingManager.isReadyToEnable()) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ButtonFactory(text = if (isDebuggingEnabled) "Disable Debugging" else "Enable Debugging") {
                        if (isDebuggingEnabled) {
                            OnlineDebuggingManager.disable()
                        }
                        else {
                            OnlineDebuggingManager.enable()
                            runOnlineLogService()
                        }
                        dataStateFlow.value = if (dataStateFlow.value == DataStatus.DONE)
                            DataStatus.REFRESH
                        else
                            DataStatus.DONE
                    }
                }
            }
            else {
                CircularProgressIndicator()
            }
        }
    }
}