package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.Utilities.checkOnlineState
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import com.itgates.ultra.pulpo.cira.viewModels.ServerViewModel
import com.itgates.ultra.pulpo.cira.dataStore.DataStoreService
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.LoginPharmaResponse
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.UserDetailsData
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.ItgFile
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.composeUI.LoginPage
import com.itgates.ultra.pulpo.cira.ui.composeUI.PdfViewer
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val rememberMe = MutableLiveData(false)
    private val serverViewModel: ServerViewModel by viewModels()
    private val cacheViewModel: CacheViewModel by viewModels()
    private val internetStateFlow = MutableStateFlow(false)
    private val loadingStateFlow = MutableStateFlow(false)
    private val trial = MutableStateFlow(false)

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        /** delete TODO */
        serverViewModel.fileData.observeForever {
            trial.value = true
            cacheViewModel.saveFileData(ItgFile(5, EmbeddedEntity("file"), "link", it))
        }
    }

    private fun loginAction(username: String, password: String) {
        if (checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.userLoginPharma(username, password)
        }
        else {
            loadingStateFlow.value = true
        }
    }

    private fun dealWithLoginResponse(loginDataResponse: LoginPharmaResponse) {
        CoroutineManager.getScope().launch {
            if (loginDataResponse.Data.isNotEmpty()) {
                val userData = loginDataResponse.Data[0]
                val dataStoreService = cacheViewModel.getDataStoreService()
                val isNewUser = dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await().isEmpty()
                        || userData.UserId != dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await()

                if (isNewUser) {
//                    "Are you sure you want to login with different user?"
//                    must ensur that all data deleted
                }

                dealWithAuthData(dataStoreService, userData)
                Utilities.navigateToMainActivity(this@LoginActivity, isNewUser)
            }
            else {
                Utilities.createCustomToast(applicationContext, "There is error with login, Please try again")
            }
        }
    }

    private fun dealWithAuthData(dataStoreService: DataStoreService, userData: UserDetailsData) {
        // safe entrance [same user, or first user to app]
        dataStoreService.setDataObj(
            PreferenceKeys.LAST_LOGIN, GlobalFormats.getFullDate(Locale.getDefault(), Date())
        )
        dataStoreService.setDataObj(PreferenceKeys.IS_MANAGER, userData.IsManager)
        dataStoreService.setDataObj(PreferenceKeys.USER_ID, userData.UserId)
        dataStoreService.setDataObj(PreferenceKeys.DIVISIONS_NAME, userData.DivisionName)
        dataStoreService.setDataObj(PreferenceKeys.LINE_NAME, userData.DefaultLineName)
        dataStoreService.setDataObj(PreferenceKeys.CODE, userData.Code)
        dataStoreService.setDataObj(PreferenceKeys.USERNAME, userData.Username)
        dataStoreService.setDataObj(PreferenceKeys.NAME, userData.Name)
        dataStoreService.setDataObj(PreferenceKeys.REMEMBER_ME, rememberMe.value.toString())

        dataStoreService.saveDivisionsOrLinesText(PreferenceKeys.LINES_IDS, userData.LineIds)
        dataStoreService.saveDivisionsOrLinesText(PreferenceKeys.DIVISIONS_IDS, userData.DivIds)
    }
}