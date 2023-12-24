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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Utilities.checkOnlineState
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import com.itgates.co.pulpo.ultra.viewModels.ServerViewModel
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.LoginPharmaResponse
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.UserDetailsData
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.composeUI.LoginPage
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.network.models.requestModels.UploadedUserModel
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.UserPharmaResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val rememberMe = MutableLiveData(false)
    private val serverViewModel: ServerViewModel by viewModels()
    private val cacheViewModel: CacheViewModel by viewModels()
    private val internetStateFlow = MutableStateFlow(false)
    private val loadingStateFlow = MutableStateFlow(false)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        println("----------------------------------------000 ${TimeZone.getDefault().displayName}")
//        println("----------------------------------------000 ${TimeZone.getDefault().id}")
//        println("----------------------------------------000 ${TimeZone.getDefault()}")
//
//        val timeZoneId = "Asia/Riyadh"
//        val actualTime = Utilities.getActualTimeInTimeZone(timeZoneId)
//        println("Current time in $timeZoneId: $actualTime")
//
//        CoroutineManager.getScope().launch {
//            println("----------------------------------------000 ${Utilities.isDeviceClockAccurate()}")
//        }


        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name)) }
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
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            LoginPage(
                                internetStateFlow,
                                loadingStateFlow,
                                rememberMe
                            ) { username, password ->
                                if (username == "0" && password == "0") {
                                    Utilities.createCustomToast(applicationContext, "Invalid credentials")
                                }
                                else {
                                    this@LoginActivity.loginAction(username, password)
                                }
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
//                                StatisticsCircularProgressBar(percentage = (3.0/15).toFloat(), number = 15)
//                                val badgeSize = 15
//                                val a1 = 0
//                                StatisticsCircularProgressBar(
//                                    percentage = a1.toFloat() / 1,
//                                    number = 5,
//                                    fontSize = 15.sp,
//                                    radius = badgeSize.dp,
//                                    color = ITGatesDarkGreyColor,
//                                    secondaryColor = ITGatesVeryLightGreyColor,
//                                    strokeWidth = 6.dp
//                                )
                            }
//                            serverViewModel.getFile(applicationContext, "presentation.zip", 5L)

////                            val file = File(applicationContext.cacheDir, "pdf1.pdf")
////                            cacheViewModel.saveFileData(ItgFile(5, EmbeddedEntity("file"), "link", file))

//                            val isLoaded = remember {
//                                mutableStateOf(false)
//                            }
//                            var file = File(applicationContext.cacheDir, "2.pdf")
//                            cacheViewModel.loadFileData()
//                            cacheViewModel.fileData.observe(this@LoginActivity) {
//                                file = it.file
//                                isLoaded.value = true
//                            }
//
//                            if (isLoaded.value) {
//                                if (!file.exists()) {
//                                    val inputStream = applicationContext.assets.open("pdf1.pdf")
//                                    val outputStream = FileOutputStream(file)
//                                    inputStream.copyTo(outputStream)
//                                    inputStream.close()
//                                    outputStream.close()
//                                }
//                                PdfViewer(pdfFile = file)
//                            }

//                            val isLoaded = trial.collectAsState()
//                            if (isLoaded.value) {
//                                println("ooooooooooooooooooooooooooooooooooooooooo")
//                                val file = serverViewModel.fileData.value!!
//                                if (!file.exists()) {
//                                    val inputStream = applicationContext.assets.open("pdf1.pdf")
//                                    val outputStream = FileOutputStream(file)
//                                    inputStream.copyTo(outputStream)
//                                    inputStream.close()
//                                    outputStream.close()
//                                }
//                                PdfViewer(context = applicationContext, pdfFile = file)
//                            }


//                            Surface(modifier = Modifier
//                                .height(250.dp)
//                                .background(Color.Red)
//                                .padding(padding_16)
//                            ) {
//                                AndroidView(factory = {
//                                    WebView(it).apply {
//                                        layoutParams = ViewGroup.LayoutParams(
//                                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                                            ViewGroup.LayoutParams.WRAP_CONTENT
//                                        )
//                                        webViewClient = WebViewClient()
//                                    }
//                                }, update = {
//                                    val file2 = applicationContext.assets.open("html1.html")
//
//                                    var content = ""
//                                    file2.bufferedReader().use { fileContent ->
//                                        content = fileContent.readText()
//                                        println("${fileContent.readText()}")
//                                    }
//
//                                    it.settings.javaScriptEnabled = true
//                                    it.settings.allowFileAccess = true
//
//                                    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
//                                    it.loadData(content, "text/html", "UTF-8")
//                                })
//                            }

                        }
                    }
                }
            }
        }

        setObservers()
    }

    private fun setObservers() {
        serverViewModel.authenticationData.observeForever {
            loadingStateFlow.value = false
            dealWithLoginResponse(it)
        }

        serverViewModel.userData.observeForever {
            loadingStateFlow.value = false
            dealWithUserResponse(it)
        }
    }

    private fun loginAction(username: String, password: String) {
        if (checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.userLoginPharma(UploadedUserModel(username, password))
            cacheViewModel.saveOfflineLog(Utilities.createOfflineLog(
                applicationContext,
                getString(R.string.login_action),
                2, // must be in object file as constants lockups
                -1L
            ))
        }
        else {
            internetStateFlow.value = true
        }
    }

    private fun dealWithLoginResponse(loginDataResponse: LoginPharmaResponse) {
        if (loginDataResponse.access_token != null && loginDataResponse.access_token.isNotEmpty()) {
            CoroutineManager.getScope().launch {
                println("-------------------------------------------------- 0000 -----")
                println(loginDataResponse.access_token)
                // TODO save access token
//                accessToken = loginDataResponse.access_token
                cacheViewModel.getDataStoreService().setDataObj(PreferenceKeys.TOKEN, loginDataResponse.access_token)

                serverViewModel.fetchUserData(loginDataResponse.access_token)
            }
        }
        else {
            Utilities.createCustomToast(applicationContext, loginDataResponse.message)
            cacheViewModel.saveOfflineLog(Utilities.createOfflineLog(
                applicationContext,
                getString(R.string.login_action_failed),
                2, // must be in object file as constants lockups
                -1L
            ))
            loadingStateFlow.value = false
        }
    }

    private fun dealWithUserResponse(userDataResponse: UserPharmaResponse) {
        CoroutineManager.getScope().launch {
            val userData = userDataResponse.data
            val dataStoreService = cacheViewModel.getDataStoreService()
            val isNewUser = dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await().isEmpty()
                    || userData.id.toString() != dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await()

            cacheViewModel.saveOfflineLog(Utilities.createOfflineLog(
                applicationContext,
                getString(R.string.login_action_success),
                2, // must be in object file as constants lockups
                userData.id
            ))

            if (isNewUser) {
//                    "Are you sure you want to login with different user?"
//                    must ensure that all data deleted
            }

            dealWithAuthData(dataStoreService, userData)
//            Utilities.navigateToMainActivity(this@LoginActivity, isNewUser)
            if (isNewUser) {
                val intent = Intent(this@LoginActivity, NewUserDataActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            else
                Utilities.navigateToMainActivity(this@LoginActivity)
        }
    }

    private fun dealWithAuthData(dataStoreService: DataStoreService, userData: UserDetailsData) {
        // safe entrance [same user, or first user to app]
        dataStoreService.setDataObj(
            PreferenceKeys.LAST_LOGIN, GlobalFormats.getFullDate(Locale.getDefault(), Date())
        )
        dataStoreService.setDataObj(PreferenceKeys.IS_MANAGER, userData.IsManager.toString())
        dataStoreService.setDataObj(PreferenceKeys.USER_ID, userData.id.toString())
        dataStoreService.setDataObj(PreferenceKeys.CODE, userData.empCode ?: "0")
        dataStoreService.setDataObj(PreferenceKeys.USERNAME, userData.username)
        dataStoreService.setDataObj(PreferenceKeys.FULL_NAME, userData.fullName)
        dataStoreService.setDataObj(
            PreferenceKeys.URL,
            userData.imageUrl.ifEmpty { "https://utopia.pulposoft.net/img/avatars/male.png" }
        )
        dataStoreService.setDataObj(PreferenceKeys.REMEMBER_ME, rememberMe.value.toString())
    }
}