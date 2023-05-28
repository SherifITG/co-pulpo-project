package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import com.itgates.ultra.pulpo.cira.ui.activities.*
import com.itgates.ultra.pulpo.cira.AppController
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.connectivityObserver.ConnectivityObserver
import com.itgates.ultra.pulpo.cira.connectivityObserver.NetworkConnectivityObserver
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.locationPackage.DefaultLocationClient
import com.itgates.ultra.pulpo.cira.locationPackage.LocationClient
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.ui.composeUI.*
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.Globals
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.itgates.ultra.pulpo.cira.enumerations.CachingDataTackStatus
import com.itgates.ultra.pulpo.cira.ui.utils.BaseDataActivity
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.mainActivity_isNewUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import kotlin.streams.toList

@AndroidEntryPoint
class MainActivity : BaseDataActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var locationClient: LocationClient

    private var name = MutableStateFlow("...")
    private var code = MutableStateFlow("...")
    private var divName = MutableStateFlow("...")
    private var lineName = MutableStateFlow("...")
    private var lastLogin = MutableStateFlow("...")

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // User Data
        getUserData()
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
//                            MyScreen2()

                            NoInternetDialog(internetStateFlow)
                            LoadingDialog(loadingStateFlow, "Loading New User Data")
                        }
                    }
                }
            }
        }

        setDataObservers()
        loadNewUserDataIfNeeded()
    }

    private fun mainActivityObservers() {
        setObservers()
        observeOnNetwork()
        observeOnLocation()
        observeOnUploadEvent()
    }

    override fun setDataObservers() {
        loadingAllStateFlow.observe(this@MainActivity) {
            if (it == 4) {
                loadingAllStateFlow.value = 0
                loadingStateFlow.value = false

                mainActivityObservers()
            }
        }

        masterDataTrack.observe(this@MainActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearMasterData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    saveMasterData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- master")
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                else -> {}
            }
        }

        accountAndDoctorDataTrack.observe(this@MainActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearAccountAndDoctorData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    saveAccountAndDoctorData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- doctor")
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                else -> {}
            }
        }

        presentationAndSlideDataTrack.observe(this@MainActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearPresentationAndSlideData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    savePresentationAndSlideData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- slide")
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                else -> {}
            }
        }

        plannedVisitDataTrack.observe(this@MainActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearPlannedVisitData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    savePlannedVisitData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- planned")
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
                }
                else -> {}
            }
        }
    }

    private fun loadNewUserDataIfNeeded() {
        if (mainActivity_isNewUser) {
            CoroutineManager.getScope().launch(Dispatchers.Main) {
                loadingAllStateFlow.value = 1 // fired
                loadingStateFlow.value = true
                delay(1500)
                updateAccountAndDoctorData()
                updatePlannedVisitData()
                updateMasterData()
            }
        }
        else {
            mainActivityObservers()
        }
    }

    private fun getUserData() {
        CoroutineManager.getScope().launch {
            if (mainActivity_isNewUser) {
                delay(1500)
            }
            name.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.NAME).await()
            code.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.CODE).await()
            divName.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.DIVISIONS_NAME).await()
            lineName.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.LINE_NAME).await()
            lastLogin.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.LAST_LOGIN).await()
        }
    }

    private fun setObservers() {
        cacheViewModel.unSyncedActualVisitData.observeForever { actualList ->
            CoroutineManager.getScope().launch {
                cacheViewModel.getDataStoreService().setDataObj(
                    stringPreferencesKey("custom_logs"),
                    cacheViewModel.getDataStoreService()
                        .getDataObjAsync(stringPreferencesKey("custom_logs")).await()
                            + "\nun filtered " + Gson().toJson(actualList)
                            + "\n" + GlobalFormats.getFullDate(Locale.getDefault(), Date())

                )
            }
            println("+++++++++++++++++++++++++++++ ${Date().time} $actualList")
            // filter blocked ids
            val filteredList = actualList.stream().filter {
                !Globals.isActualIdInJustUploadedList(it.id)
            }.toList()

            // calculate blocked ids
            val filteredIdsList = filteredList.stream().map { it.id }.toList()

            // blocking for 2 second
            Globals.addActualIdToJustUploadedList(filteredIdsList)

            // upload the remained actual list

            if (filteredList.isNotEmpty()) {
//                CoroutineManager.getScope().launch {
//                    cacheViewModel.getDataStoreService().setDataObj(
//                        stringPreferencesKey("custom_logs"),
//                        cacheViewModel.getDataStoreService()
//                            .getDataObjAsync(stringPreferencesKey("custom_logs")).await()
//                                + "\nfiltered " + Gson().toJson(filteredList)
//                                + "\n" + GlobalFormats.getFullDate(Locale.getDefault(), Date())
//
//                    )
//                }
                println("***************************** ${Date().time} $filteredList")
                serverViewModel.uploadActualVisitsData(
                    filteredList.stream().map { UploadedActualVisitModel(it) }.toList()
                )
            }
        }

        serverViewModel.uploadedActualVisitData.observeForever { response ->
            CoroutineManager.getScope().launch {
                cacheViewModel.getDataStoreService().setDataObj(
                    stringPreferencesKey("custom_logs"),
                    cacheViewModel.getDataStoreService()
                        .getDataObjAsync(stringPreferencesKey("custom_logs")).await()
                            + "\nresponse " + Gson().toJson(response)
                            + "\n" + GlobalFormats.getFullDate(Locale.getDefault(), Date())

                )
            }
            if (response.Data.isNotEmpty()) {
                response.Data.forEach {
                    cacheViewModel.uploadedActualVisitData(it)
                }
            }
        }
    }

    private fun observeOnNetwork() {
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach {
            if (it == ConnectivityObserver.ConnectivityStatus.AVAILABLE) {
                Globals.triggerActualEndEvent()
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeOnUploadEvent() {
        Globals.uploadingFlow.onEach {
            cacheViewModel.loadUnSyncedActualVisits()
        }.launchIn(lifecycleScope)
    }

    private fun observeOnLocation() {
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        locationClient.getLocationUpdates(5000, this@MainActivity).onEach {
            println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++= $it")
            println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2 += ${it.location}")
            println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2 += ${it.location?.accuracy}")
            println("++++++++++++++++++++++++++++++++++++ 2 += ${GlobalFormats.getFullDate(Locale.getDefault(), Date())}")
            Globals.trustedLocationInfo = it
            cacheViewModel.getDataStoreService().setDataObj(PreferenceKeys.CACHE_LOCATION, Gson().toJson(it))

            val currentActivity = (application as AppController).currentActivity
            if (it.location == null) {
                if (currentActivity?.javaClass?.simpleName != getString(R.string.title_location_error)) {
                    currentActivity?.startActivity(Intent(currentActivity, LocationErrorActivity::class.java))
                }
            }
            else {
                if (currentActivity?.javaClass?.simpleName == getString(R.string.title_location_error)) {
                    Utilities.navigateToMainActivity(currentActivity.applicationContext)
                }
            }

        }.launchIn(lifecycleScope)
    }

    private fun startActualActivity() {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
        }
        else {
            if (Utilities.isAutomaticTimeEnabled(applicationContext)) {
                PassedValues.actualActivity_isPlannedVisit = false
                PassedValues.actualActivity_PlannedVisitObj = null
                PassedValues.actualActivity_startDate = Date()
                PassedValues.actualActivity_startLocation = locationInfo.location
                startActivity(Intent(this@MainActivity, ActualActivity::class.java))
            }
            else {
                Utilities.createCustomToast(
                    applicationContext,
                    "Enable the automatic time option"
                )
            }
        }
    }

    private fun startOfficeWorkActivity() {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
        }
        else {
            if (Utilities.isAutomaticTimeEnabled(applicationContext)) {
                PassedValues.officeWorkActivity_isPlannedOfficeWork = false
                PassedValues.officeWorkActivity_PlannedOfficeWorkObj = null
                PassedValues.officeWorkActivity_startDate = Date()
                PassedValues.officeWorkActivity_startLocation = locationInfo.location
                startActivity(Intent(this@MainActivity, OfficeWorkActivity::class.java))
            }
            else {
                Utilities.createCustomToast(
                    applicationContext,
                    "Enable the automatic time option"
                )
            }
        }
    }

    @Composable
    fun MyScreen() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val firstBoxWidth = screenWidth.times(0.16F)
        val firstBoxCornerRadius = screenWidth.times(0.9F)

        Column {
            Box(
                modifier = Modifier
                    .height(firstBoxWidth)
                    .fillMaxWidth()
                    .background(ITGatesPrimaryColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = firstBoxCornerRadius))
                        .background(ITGatesWhiteColor),
                    contentAlignment = Alignment.Center
                ) {
//                    ButtonFactory(text = "export data") {
//                        CoroutineManager.getScope().launch {
//                            println("##############################################################################")
//                            println(
//                                cacheViewModel.getDataStoreService()
//                                    .getDataObjAsync(stringPreferencesKey("custom_logs"))
//                                    .await()
//                            )
//                            println("##############################################################################")
//                        }
//                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ITGatesWhiteColor)
            ) {
                Box(
                    modifier = Modifier
                        .clip(ITGatesEndCornerShape)
                        .background(ITGatesPrimaryColor)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = padding_24, horizontal = padding_36),
                        verticalArrangement = Arrangement.spacedBy(padding_4)
                    ) {
                        WhiteTextFactory(text = "Name: ${name.collectAsState().value}")
                        WhiteTextFactory(text = "Code: ${code.collectAsState().value}")
                        WhiteTextFactory(text = "Division: ${divName.collectAsState().value}")
                        WhiteTextFactory(text = "Line: ${lineName.collectAsState().value}")
                        WhiteTextFactory(text = "Last Login: ${lastLogin.collectAsState().value}")
                    }
                }
            }
            Box {
                Box(
                    modifier = Modifier
                        .height(firstBoxWidth)
                        .fillMaxWidth()
                        .background(ITGatesPrimaryColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = firstBoxCornerRadius))
                            .background(ITGatesWhiteColor)
                    ) {

                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.padding(vertical = padding_36),
                        verticalArrangement = Arrangement.spacedBy(padding_16)
                    ) {
                        val iconSize = 4F
                        val spaceSize = 1F
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Spacer(modifier = Modifier.weight(spaceSize))
                            Column(
                                modifier = Modifier
                                    .weight(iconSize)
                                    .clip(ITGatesCardCornerShape)
                                    .clickable {
                                        PassedValues.plannedActivity_isToday = true
                                        val intent = Intent(
                                            this@MainActivity,
                                            PlannedVisitActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                    .padding(padding_8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1F),
                                    painter = painterResource(R.drawable.report_planned_visits_icon),
                                    contentDescription = "Icon",
                                    tint = ITGatesPrimaryColor
                                )
                                MultiLineTextFactory(text = "Planned\nVisit")
                            }
                            Spacer(modifier = Modifier.weight(spaceSize))
                            Column(
                                modifier = Modifier
                                    .weight(iconSize)
                                    .clip(ITGatesCardCornerShape)
                                    .clickable { startActualActivity() }
                                    .padding(padding_8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1F),
                                    painter = painterResource(R.drawable.report_actual_visits_icon),
                                    contentDescription = "Icon",
                                    tint = ITGatesPrimaryColor
                                )
                                MultiLineTextFactory(text = "Unplanned\nVisit")
                            }
                            Spacer(modifier = Modifier.weight(spaceSize))
                            Column(
                                modifier = Modifier
                                    .weight(iconSize)
                                    .clip(ITGatesCardCornerShape)
                                    .clickable {
                                        val intent = Intent(
                                            this@MainActivity,
                                            DataCenterActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                    .padding(padding_8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1F),
                                    painter = painterResource(R.drawable.main_data_icon),
                                    contentDescription = "Icon",
                                    tint = ITGatesPrimaryColor
                                )
                                MultiLineTextFactory(text = "Data\nCenter")
                            }
                            Spacer(modifier = Modifier.weight(spaceSize))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Spacer(modifier = Modifier.weight(spaceSize))
                            Column(
                                modifier = Modifier
                                    .weight(iconSize)
                                    .clip(ITGatesCardCornerShape)
                                    .clickable { startOfficeWorkActivity() }
                                    .padding(padding_8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1F),
                                    painter = painterResource(R.drawable.main_officework_icon),
                                    contentDescription = "Icon",
                                    tint = ITGatesPrimaryColor
                                )
                                MultiLineTextFactory(text = "Office Work")
                            }
                            Spacer(modifier = Modifier.weight(spaceSize))
                            Column(
                                modifier = Modifier
                                    .weight(iconSize)
                                    .clip(ITGatesCardCornerShape)
                                    .clickable {
                                        val intent =
                                            Intent(this@MainActivity, ReportsActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .padding(padding_8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1F),
                                    painter = painterResource(R.drawable.main_report_icon),
                                    contentDescription = "Icon",
                                    tint = ITGatesPrimaryColor
                                )
                                MultiLineTextFactory(text = "Reports")
                            }
                            Spacer(modifier = Modifier.weight(spaceSize))
//                            Box(modifier = Modifier
//                                .weight(iconSize)
//                                .aspectRatio(1F))

                            Column(
                                modifier = Modifier
                                    .weight(iconSize)
                                    .clip(ITGatesCardCornerShape)
                                    .clickable {
                                        val intent =
                                            Intent(this@MainActivity, PlanningActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .padding(padding_8),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1F),
                                    painter = painterResource(R.drawable.main_report_icon),
                                    contentDescription = "Icon",
                                    tint = ITGatesPrimaryColor
                                )
                                MultiLineTextFactory(text = "Reports")
                            }
                            Spacer(modifier = Modifier.weight(spaceSize))
                        }
                    }
                }
            }

        }
    }

    @Composable
    fun MyScreen2() {
        Column {
//            ButtonFactory(text = "Location", withPercentage = 0.7F) {
//                applicationContext.checkLocationPermission(this@MainActivity)
//            }
        }
    }
}