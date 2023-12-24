package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.itgates.co.pulpo.ultra.ui.activities.*
import com.itgates.co.pulpo.ultra.AppController
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.connectivityObserver.ConnectivityObserver
import com.itgates.co.pulpo.ultra.connectivityObserver.NetworkConnectivityObserver
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.locationPackage.DefaultLocationClient
import com.itgates.co.pulpo.ultra.locationPackage.LocationClient
import com.itgates.co.pulpo.ultra.ui.composeUI.*
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.utilities.Globals
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.itgates.co.pulpo.ultra.BuildConfig
import com.itgates.co.pulpo.ultra.enumerations.DataStatus
import com.itgates.co.pulpo.ultra.locationService.LocationService
import com.itgates.co.pulpo.ultra.network.models.requestModels.*
import com.itgates.co.pulpo.ultra.onlineLogService.OnlineDebuggingManager
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Setting
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.co.pulpo.ultra.ui.utils.MainGridController
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import com.itgates.co.pulpo.ultra.viewModels.ServerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.sqrt
import kotlin.streams.toList

@AndroidEntryPoint
//class MainActivity : BaseDataActivity() {
class MainActivity : ComponentActivity() {
    val cacheViewModel: CacheViewModel by viewModels()
    private val serverViewModel: ServerViewModel by viewModels()

    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var locationClient: LocationClient

    private var name = MutableStateFlow("...")
    private var code = MutableStateFlow("...")
    private var url = MutableStateFlow("...")
    private var lastLogin = MutableStateFlow("...")
    private var isManager = MutableStateFlow("...")
    private var checkInTripStarted = MutableStateFlow(false)

    private val infoVisibleStateFlow = MutableStateFlow(false) // translate to common or dialog ui file
    private val infoTextStateFlow = MutableStateFlow("") // translate to common or dialog ui file

    // for planned badge
    private var plannedBadgeCount = MutableStateFlow(0L)

    private val dataStateFlow = MutableStateFlow(DataStatus.LOADING)
    private var actualSetting = Setting(
        1L,
        EmbeddedEntity(SettingEnum.ACTUAL_DIRECT.text),
        "1"
    )
    private var appCheckInAndCheckOutSetting = Setting(
        0L,
        EmbeddedEntity(SettingEnum.APP_CHECK_IN_AND_CHECKOUT.text),
        "0"
    )
    private var appLogoutSetting = Setting(
        0L,
        EmbeddedEntity(SettingEnum.APP_LOGOUT.text),
        "0"
    )

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
                            SwitchUI()
                            DeviationOrInfoMessageDialog(
                                infoVisibleStateFlow,
                                infoTextStateFlow,
                                "Disabled Info Dialog",
                                isInfo = true,
                                onSaveAction = {},
                                onViewMapAction = {}
                            )
                        }
                    }
                }
            }
        }

        mainActivityObservers()
        loadMainActivitySettings()
        loadTodayPlannedVisitsAndOWCount()
        loadRelationalLines()

        runLocationService()
    }

    private fun runLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun mainActivityObservers() {
        setObservers()
        observeOnNetwork()
        observeOnLocation()
        observeOnUploadEvent()
    }

    private fun loadTodayPlannedVisitsAndOWCount() {
        cacheViewModel.loadTodayPlannedVisitsAndOWCount()
    }

    private fun loadRelationalLines() {
        cacheViewModel.loadRelationalLines()
    }

    private fun getUserData() {
        CoroutineManager.getScope().launch {
            name.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.FULL_NAME).await()
            code.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.CODE).await()
            url.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.URL).await()
            lastLogin.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.LAST_LOGIN).await()
            isManager.value = cacheViewModel.getDataStoreService().getDataObjAsync(PreferenceKeys.IS_MANAGER).await()
            checkInTripStarted.value = cacheViewModel.getDataStoreService().getDataBooleanObjAsync(PreferenceKeys.CHECK_IN_TRIP_START).await()
        }
    }

    private fun setObservers() {
        cacheViewModel.unSyncedActualVisitData.observeForever { actualList ->
            println("+++++++++++++++++++++++++++++ actual +++ ${Date().time} $actualList")
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
                println("***************************** ${Date().time} $filteredList")
                serverViewModel.uploadActualVisitsData(
                    UploadedActualVisitsListModel(
                        filteredList.stream().map { UploadedActualVisitModel(it) }.toList()
                    )
                )
            }
        }

        serverViewModel.uploadedActualVisitData.observeForever { response ->
            if (response.data.isNotEmpty()) {
                response.data.forEach {
                    if (it.visitId > 0) {
                        cacheViewModel.uploadedActualVisitData(it)
                    }
                }
            }
        }

        cacheViewModel.unSyncedOfficeWorkData.observeForever { owList ->
            println("+++++++++++++++++++++++++++++ ow +++ ${Date().time} $owList")
            OnlineDebuggingManager.sendFirebaseLog(name.value, "+++++++++++++++++++++++++++++ ow +++ ${Date().time} $owList")
            // filter blocked ids
            val filteredList = owList.stream().filter {
                !Globals.isOWIdInJustUploadedList(it.id)
            }.toList()

            // calculate blocked ids
            val filteredIdsList = filteredList.stream().map { it.id }.toList()

            // blocking for 2 second
            Globals.addOWIdToJustUploadedList(filteredIdsList)

            // upload the remained actual list
            if (filteredList.isNotEmpty()) {
                println("***************************** ${Date().time} $filteredList")
                serverViewModel.uploadOfficeWorksData(
                    UploadedOfficeWorksListModel(
                        filteredList.stream().map { UploadedOfficeWorkModel(it) }.toList()
                    )
                )
            }
        }

        serverViewModel.uploadedOfficeWorkData.observeForever { response ->
            if (response.data.isNotEmpty()) {
                response.data.forEach {
                    if (it.owId > 0) {
                        cacheViewModel.uploadedOfficeWorkData(it)
                    }
                }
            }
        }

        cacheViewModel.unSyncedVacationData.observeForever { vacationList ->
            println("+++++++++++++++++++++++++++++ vacation +++ ${Date().time} $vacationList")
            // filter blocked ids
            val filteredList = vacationList.stream().filter {
                !Globals.isVacationIdInJustUploadedList(it.id)
            }.toList()

            // calculate blocked ids
            val filteredIdsList = filteredList.stream().map { it.id }.toList()

            // blocking for 2 second
            Globals.addVacationIdToJustUploadedList(filteredIdsList)

            // upload the remained actual list
            if (filteredList.isNotEmpty()) {
                println("***************************** ${Date().time} $filteredList")
                serverViewModel.uploadVacationsData(
                    UploadedVacationsListModel(
                        filteredList.stream().map { UploadedVacationModel(it) }.toList()
                    )
                )
            }
        }

        serverViewModel.uploadedVacationData.observeForever { response ->
            if (response.data.isNotEmpty()) {
                response.data.forEach {
                    if (it.vacationId > 0) {
                        cacheViewModel.uploadedVacationData(it)
                    }
                }
            }
        }

        cacheViewModel.unSyncedNewPlanData.observeForever { newPlanList ->
            println("+++++++++++++++++++++++++++++ new plan +++ ${Date().time} $newPlanList")
            // filter blocked ids
            val filteredList = newPlanList.stream().filter {
                !Globals.isNewPlanIdInJustUploadedList(it.id)
            }.toList()

            // calculate blocked ids
            val filteredIdsList = filteredList.stream().map { it.id }.toList()

            // blocking for 2 second
            Globals.addNewPlanIdToJustUploadedList(filteredIdsList)

            // upload the remained actual list
            if (filteredList.isNotEmpty()) {
                println("***************************** new plan *** ${Date().time} $filteredList")
                serverViewModel.uploadNewPlansData(
                    UploadedNewPlansListModel(
                        filteredList.stream().map { UploadedNewPlanModel(it) }.toList()
                    )
                )
            }
        }

        serverViewModel.uploadedNewPlanData.observeForever { response ->
            if (response.data.isNotEmpty()) {
                response.data.forEach {
                    cacheViewModel.uploadedNewPlanData(it)
                }
            }
        }

        cacheViewModel.unSyncedOfflineLogData.observeForever { offlineLogList ->
            println("+++++++++++++++++++++++++++++ offline log +++ ${Date().time} $offlineLogList")
            // filter blocked ids
            val filteredList = offlineLogList.stream().filter {
                !Globals.isOfflineLogIdInJustUploadedList(it.id)
            }.toList()

            // calculate blocked ids
            val filteredIdsList = filteredList.stream().map { it.id }.toList()

            // blocking for 2 second
            Globals.addOfflineLogIdToJustUploadedList(filteredIdsList)

            // upload the remained actual list
            if (filteredList.isNotEmpty()) {
                println("***************************** offline log *** ${Date().time} $filteredList")
                serverViewModel.uploadOfflineLogData(
                    UploadedOfflineLogsListModel(
                        filteredList.stream().map { UploadedOfflineLogModel(it) }.toList()
                    )
                )
            }
        }

        serverViewModel.uploadedOfflineLogData.observeForever { response ->
            if (response.data.isNotEmpty()) {
                response.data.forEach {
                    cacheViewModel.uploadedOfflineLogs(it)
                }
            }
        }

        cacheViewModel.unSyncedOfflineLocData.observeForever { offlineLocList ->
            println("+++++++++++++++++++++++++++++ offline loc +++ ${Date().time} $offlineLocList")
            // filter blocked ids
            val filteredList = offlineLocList.stream().filter {
                !Globals.isOfflineLocIdInJustUploadedList(it.id)
            }.toList()

            // calculate blocked ids
            val filteredIdsList = filteredList.stream().map { it.id }.toList()

            // blocking for 2 second
            Globals.addOfflineLocIdToJustUploadedList(filteredIdsList)

            // upload the remained actual list
            if (filteredList.isNotEmpty()) {
                println("***************************** offline loc *** ${Date().time} $filteredList")
                serverViewModel.uploadOfflineLocData(
                    UploadedOfflineLocsListModel(
                        filteredList.stream().map { UploadedOfflineLocModel(it) }.toList()
                    )
                )
            }
        }

        serverViewModel.uploadedOfflineLocData.observeForever { response ->
            if (response.data.isNotEmpty()) {
                response.data.forEach {
                    cacheViewModel.uploadedOfflineLocations(it)
                }
            }
        }

        cacheViewModel.settingData.observe(this@MainActivity) {
            if (it.isNotEmpty()) {
                it.forEach { setting ->
                    when(setting.embedded.name) {
                        SettingEnum.SEASON.text -> Globals.seasonSetting = setting
                        SettingEnum.TIME_ZONE.text -> Globals.timeZoneSetting = setting
                        SettingEnum.ACTUAL_DIRECT.text -> actualSetting = setting
                        SettingEnum.APP_CHECK_IN_AND_CHECKOUT.text -> appCheckInAndCheckOutSetting = setting
                        SettingEnum.APP_LOGOUT.text -> appLogoutSetting = setting
                    }
                }

                dataStateFlow.value = if (dataStateFlow.value == DataStatus.DONE) DataStatus.REFRESH else DataStatus.DONE
            }
        }

        cacheViewModel.todayPlannedVisitsAndOWCount.observe(this@MainActivity) {
            plannedBadgeCount.value = it
        }

        cacheViewModel.lineData.observe(this@MainActivity) {
            Globals.lineData = it
            println("---------------------------------------------------------------- lines ------")
            it.forEach { line ->
                println("${line.line.id} : ${line.unplannedCoverage}")
            }
            dataStateFlow.value = if (dataStateFlow.value == DataStatus.DONE) DataStatus.REFRESH else DataStatus.DONE
        }
    }

    private fun observeOnNetwork() {
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach {
            if (it == ConnectivityObserver.ConnectivityStatus.AVAILABLE) {
                Globals.triggerActualEndEvent()
                Globals.triggerOWEndEvent()
                Globals.triggerVacationEndEvent()
                Globals.triggerNewPlanEndEvent()
                Globals.triggerOfflineLogsEvent()
                Globals.triggerOfflineLocationsEvent()
            }
        }.launchIn(lifecycleScope)
    }

    private fun observeOnUploadEvent() {
        Globals.actualUploadingFlow.onEach {
            cacheViewModel.loadUnSyncedActualVisits()
        }.launchIn(lifecycleScope)

        Globals.owUploadingFlow.onEach {
            cacheViewModel.loadUnSyncedOfficeWorks()
        }.launchIn(lifecycleScope)

        Globals.vacationUploadingFlow.onEach {
            cacheViewModel.loadUnSyncedVacations()
        }.launchIn(lifecycleScope)

        Globals.newPlanUploadingFlow.onEach {
            cacheViewModel.loadUnSyncedNewPlans()
        }.launchIn(lifecycleScope)

        Globals.offlineLogsUploadingFlow.onEach {
            cacheViewModel.loadUnSyncedOfflineLogs()
        }.launchIn(lifecycleScope)

        Globals.offlineLocationsUploadingFlow.onEach {
            cacheViewModel.loadUnSyncedOfflineLocations()
        }.launchIn(lifecycleScope)
    }

    private fun observeOnLocation() {
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        locationClient.getLocationUpdates(5000, cacheViewModel.getDataStoreService()).onEach {
            println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++= $it")
            println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2 += ${it.location}")
            println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ 2 += ${it.location?.accuracy}")
            println("++++++++++++++++++++++++++++++++++++ 2 += ${GlobalFormats.getFullDate(Locale.getDefault(), Date())}")
            Globals.trustedLocationInfo = it

            val currentActivity = (application as AppController).currentActivity
            if (it.location == null) {
                if (currentActivity?.javaClass?.simpleName != getString(R.string.title_location_error)) {
                    currentActivity?.startActivity(Intent(currentActivity, LocationErrorActivity::class.java))
                }
            }
            else {
                cacheViewModel.getDataStoreService().setDataObj(PreferenceKeys.CACHE_LOCATION, Gson().toJson(it))
                if (currentActivity?.javaClass?.simpleName == getString(R.string.title_location_error)) {
                    Utilities.navigateToMainActivity(currentActivity.applicationContext)
                }
            }

        }.launchIn(lifecycleScope)
    }

    fun startActualActivity() {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
        }
        else {
            if (Utilities.isValidTimeProvided(applicationContext)) {
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

    fun startOfficeWorkActivity() {
        val locationInfo = Globals.trustedLocationInfo
        if (locationInfo?.location == null) {
            Utilities.createCustomToast(
                applicationContext,
                Globals.trustedLocationInfo?.errorMessage?: "Some error with location"
            )
        }
        else {
            if (Utilities.isValidTimeProvided(applicationContext)) {
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

    private fun showMainInfoDialog(totallyDisabled: Boolean) {
        infoTextStateFlow.value = if (totallyDisabled)
            "Unplanned visit is disabled"
        else
            getString(R.string.main_info_message_text)
        infoVisibleStateFlow.value = true
    }

    private fun loadMainActivitySettings() {
        cacheViewModel.loadMainActivitySettings()
    }

    private fun userLogOut() {
        // deleting cached user preference
        cacheViewModel.getDataStoreService().setDataObj(PreferenceKeys.TOKEN, "")
        cacheViewModel.getDataStoreService().setDataObj(PreferenceKeys.REMEMBER_ME, "")

        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun checkInOrOut() {
        cacheViewModel.getDataStoreService().setDataBooleanObj(PreferenceKeys.CHECK_IN_TRIP_START, !checkInTripStarted.value)
        checkInTripStarted.value = !checkInTripStarted.value
    }

    private fun requestCheckInOrOut(value: Boolean) {
//        cacheViewModel.getDataStoreService().setDataBooleanObj(PreferenceKeys.CHECK_IN_TRIP_START, value)
//        checkInTripStarted.value = value
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        println("$hasFocus 000000000000000000000000000000000000000000000000000000 5 changed 000")
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onPause() {
        super.onPause()
        println("000000000000000000000000000000000000000000000000000000 pause 000")
    }

    override fun onResume() {
        super.onResume()
        println("000000000000000000000000000000000000000000000000000000 resume 000")

        loadMainActivitySettings()
        loadTodayPlannedVisitsAndOWCount()
        loadRelationalLines()
    }

    @Composable
    fun SwitchUI() {
        val status = dataStateFlow.collectAsState()
        when(status.value) {
            DataStatus.LOADING -> {}
            DataStatus.DONE -> { MainScreen() }
            DataStatus.REFRESH -> { MainScreen() }
            DataStatus.ERROR -> {}
            DataStatus.NO_DATA -> {}
        }
    }

    @Composable
    fun MainScreen() {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val firstBoxWidth = screenWidth.times(0.16F)
        val firstBoxCornerRadius = screenWidth.times(0.9F)
        // -----------------------------------------------------------------
        val isUnplannedDisabled = actualSetting.id == 0L || actualSetting.value == "0"
        val isUnplannedExpired = Globals.isAllLinesUnplannedLimitComplete()
        // -----------------------------------------------------------------

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
                        .background(ITGatesWhiteColor)
                        .padding(padding_8),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (appLogoutSetting.value == "1") {
                        Row(
                            modifier = Modifier
                                .clip(ITGatesCircularCornerShape)
                                .clickable { userLogOut() }
                                .padding(padding_8),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.padding(padding_4))
                            TextFactory(text = "logout", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.padding(padding_4))
                            Icon(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1F),
                                painter = painterResource(R.drawable.ide_logout),
                                contentDescription = "Icon",
                                tint = ITGatesPrimaryColor
                            )
                            Spacer(modifier = Modifier.padding(padding_4))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ITGatesWhiteColor)
            ) {
                Row {
                    Box(modifier = Modifier.weight(0.9F)) {
                        Box(
                            modifier = Modifier
                                .clip(ITGatesEndCornerShape)
                                .background(ITGatesPrimaryColor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = padding_20, horizontal = padding_20),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Box(
                                    modifier = Modifier.size(padding_55),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CustomLottieAnimationView(if (BuildConfig.DEBUG) R.raw.orange_bage else R.raw.green_badge)
                                    WhiteTextFactory(text = BuildConfig.VERSION_NAME, fontWeight = FontWeight.Bold)
                                }
                            }
                            Column(
                                modifier = Modifier.padding(vertical = padding_24, horizontal = padding_36),
                                verticalArrangement = Arrangement.spacedBy(padding_4)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    CoilImage(url.collectAsState().value)
                                }
                                if (appCheckInAndCheckOutSetting.value == "1") {
                                    Spacer(modifier = Modifier.height(padding_8))
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.TopStart
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .clip(ITGatesCircularCornerShape)
                                                .clickable { checkInOrOut() }
                                                .border(
                                                    padding_2,
                                                    ITGatesWhiteColor,
                                                    ITGatesCircularCornerShape
                                                )
                                                .padding(padding_8),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Spacer(modifier = Modifier.padding(padding_4))
                                            WhiteTextFactory(
                                                text = "Check ${if (checkInTripStarted.collectAsState().value) "out" else "in"}",
                                            )
                                            Spacer(modifier = Modifier.padding(padding_4))
                                            Icon(
                                                modifier = Modifier.size(padding_25),
                                                painter = painterResource(R.drawable.circle_icon),
                                                contentDescription = "Location Icon",
                                                tint = if (checkInTripStarted.collectAsState().value) ITGatesGreenColor else ITGatesGreyColor
                                            )
                                            Spacer(modifier = Modifier.padding(padding_4))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(padding_8))
                                }
                                WhiteTextFactory(text = "Name: ${name.collectAsState().value}")
                                WhiteTextFactory(text = "Code: ${code.collectAsState().value}")
                                WhiteTextFactory(text = "Last Login: ${lastLogin.collectAsState().value}")
                                WhiteTextFactory(text = "Type: ${if (isManager.collectAsState().value == "0") "Medical Rep" else "Manager"}")
                            }
                        }
                    }
                    Box(modifier = Modifier.weight(0.1F))
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
                    val mainGridController = MainGridController(
                        this@MainActivity,
                        LocalConfiguration.current.screenWidthDp
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(mainGridController.columnsNum),
                        modifier = Modifier
                            .padding(top = padding_36, bottom = padding_12)
                            .padding(horizontal = mainGridController.spaceWidth.dp),
                        verticalArrangement = Arrangement.spacedBy(padding_16),
                        horizontalArrangement = Arrangement.spacedBy(mainGridController.spaceWidth.dp)
                    ) {
                        items(mainGridController.features) { feature ->
                            Box(modifier = Modifier.width(mainGridController.featureWidth.dp)) {
                                if (feature.name.isNotEmpty()) {
                                    val featureModifier = if (feature.isActualFeature() && isUnplannedDisabled) {
                                        Modifier
                                            .fillMaxWidth()
                                            .clip(ITGatesCardCornerShape)
                                            .clickable { showMainInfoDialog(true) }
                                            .padding(padding_8)
                                            .alpha(0.5F)
                                    }
                                    else if (feature.isActualFeature() && isUnplannedExpired) {
                                        Modifier
                                            .fillMaxWidth()
                                            .clip(ITGatesCardCornerShape)
                                            .clickable { showMainInfoDialog(false) }
                                            .padding(padding_8)
                                            .alpha(0.5F)
                                    }
                                    else {
                                        Modifier
                                            .fillMaxWidth()
                                            .clip(ITGatesCardCornerShape)
                                            .clickable { feature.action() }
                                            .padding(padding_8)
                                    }
                                    Column(
                                        modifier = featureModifier,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1F),
                                            painter = painterResource(feature.icon),
                                            contentDescription = "Icon",
                                            tint = ITGatesPrimaryColor
                                        )
                                        MultiLineTextFactory(text = feature.name)
                                    }
                                    if (feature.isPlannedFeature()) {
                                        // calculations
                                        val iconRadius = ((screenWidth / 4F) - padding_16) / 2F
                                        val badgeRadius = iconRadius * (1 - (1 / sqrt(2F))) + padding_4
                                        val plannedNum = plannedBadgeCount.collectAsState()
                                        val plannedNumString = if (plannedNum.value > 100) "100+" else "${plannedNum.value}"
                                        val plannedNumLength = plannedNumString.length
                                        val textSize = 22 - ((4 - (0.5 * plannedNumLength/3.0)) * plannedNumLength)
                                        if (plannedNum.value > 0) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(padding_6),
                                                contentAlignment = Alignment.TopEnd
                                            ) {
                                                Box(
                                                    modifier = Modifier.size((badgeRadius * 2)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        modifier = Modifier.size((badgeRadius * 2)),
                                                        painter = painterResource(R.drawable.circle_icon),
                                                        contentDescription = "Icon",
                                                        tint = ITGatesOrangeColor
                                                    )
                                                    if (plannedNum.value > 100)
                                                        WhiteTextFactory(text = "100+", size = textSize.sp, fontWeight = FontWeight.Bold)
                                                    else
                                                        WhiteTextFactory(text = "${plannedNum.value}", size = textSize.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }

                                    }
                                    if (feature.isActualFeature()) {
                                        if (isUnplannedDisabled || isUnplannedExpired) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(padding_6),
                                                contentAlignment = Alignment.TopEnd
                                            ) {
                                                // calculations
                                                val iconRadius = ((screenWidth / 4F) - padding_16) / 2F
                                                val badgeRadius = iconRadius * (1 - (1 / sqrt(2F))) + padding_4
                                                Box(
                                                    modifier = Modifier.size((badgeRadius * 2)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CustomLottieAnimationView(resId = R.raw.orange_info_lottie)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    /*
                    LazyColumn(
                        modifier = Modifier.padding(vertical = padding_36),
                        verticalArrangement = Arrangement.spacedBy(padding_16)
                    ) {
                        val iconSize = 4F
                        val spaceSize = 1F
                        item {
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
                                            val intent =
                                                Intent(
                                                    this@MainActivity,
                                                    PlanningActivity::class.java
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
                                        painter = painterResource(R.drawable.new_planning_visits_icon),
                                        contentDescription = "Icon",
                                        tint = ITGatesPrimaryColor
                                    )
                                    MultiLineTextFactory(text = "Planning")
                                }
                                Spacer(modifier = Modifier.weight(spaceSize))
                                Box(modifier = Modifier.weight(iconSize)) {
                                    Column(
                                        modifier = Modifier
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
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(padding_6),
                                        contentAlignment = Alignment.TopEnd
                                    ) {
                                        // calculations
                                        val iconRadius = ((screenWidth / 4F) - padding_16) / 2F
                                        val badgeRadius = iconRadius * (1 - (1 / sqrt(2F))) + padding_4
                                        val plannedNum = plannedBadgeCount.collectAsState()
                                        val plannedNumString = if (plannedNum.value > 100) "100+" else "${plannedNum.value}"
                                        val plannedNumLength = plannedNumString.length
                                        val textSize = 22 - ((4 - (0.5 * plannedNumLength/3.0)) * plannedNumLength)
                                        if (plannedNum.value > 0) {
                                            Box(
                                                modifier = Modifier.size((badgeRadius * 2)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    modifier = Modifier.size((badgeRadius * 2)),
                                                    painter = painterResource(R.drawable.circle_icon),
                                                    contentDescription = "Icon",
                                                    tint = ITGatesOrangeColor
                                                )
                                                if (plannedNum.value > 100)
                                                    WhiteTextFactory(text = "100+", size = textSize.sp, fontWeight = FontWeight.Bold)
                                                else
                                                    WhiteTextFactory(text = "${plannedNum.value}", size = textSize.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                    }
                                }
                                Spacer(modifier = Modifier.weight(spaceSize))
                                Box(modifier = Modifier.weight(iconSize)) {
                                    // -----------------------------------------------------------------
                                    val isUnplannedDisabled = actualSetting.id == 0L || actualSetting.value == "0"
                                    val isUnplannedExpired = Globals.isAllLinesUnplannedLimitComplete()
                                    val actualModifier = if (isUnplannedDisabled) {
                                        Modifier
                                            .clip(ITGatesCardCornerShape)
                                            .clickable { showMainInfoDialog(true) }
                                            .padding(padding_8)
                                            .alpha(0.5F)
                                    }
                                    else if (isUnplannedExpired) {
                                        Modifier
                                            .clip(ITGatesCardCornerShape)
                                            .clickable { showMainInfoDialog(false) }
                                            .padding(padding_8)
                                            .alpha(0.5F)
                                    }
                                    else {
                                        Modifier
                                            .clip(ITGatesCardCornerShape)
                                            .clickable { startActualActivity() }
                                            .padding(padding_8)
                                    }
                                    // -----------------------------------------------------------------
                                    Column(
                                        modifier = actualModifier,
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
                                    if (isUnplannedDisabled || isUnplannedExpired) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(padding_6),
                                            contentAlignment = Alignment.TopEnd
                                        ) {
                                            // calculations
                                            val iconRadius = ((screenWidth / 4F) - padding_16) / 2F
                                            val badgeRadius = iconRadius * (1 - (1 / sqrt(2F))) + padding_4
                                            Box(
                                                modifier = Modifier.size((badgeRadius * 2)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CustomLottieAnimationView(resId = R.raw.orange_info_lottie)
                                            }

                                        }
                                    }

                                }
                                Spacer(modifier = Modifier.weight(spaceSize))
                            }
                        }
                        item {
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
                                            val intent = Intent(
                                                this@MainActivity,
                                                MyLocationMapActivity::class.java
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
                                        painter = painterResource(R.drawable.my_location_icon),
                                        contentDescription = "Icon",
                                        tint = ITGatesPrimaryColor
                                    )
                                    MultiLineTextFactory(text = "Your Location")
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
                                                Intent(
                                                    this@MainActivity,
                                                    ReportsActivity::class.java
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
                                        painter = painterResource(R.drawable.main_report_icon),
                                        contentDescription = "Icon",
                                        tint = ITGatesPrimaryColor
                                    )
                                    MultiLineTextFactory(text = "Reports")
                                }
                                Spacer(modifier = Modifier.weight(spaceSize))
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Spacer(modifier = Modifier.weight(spaceSize))
                                Box(modifier = Modifier
                                    .weight(iconSize)
                                    .aspectRatio(1F))
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
                                Box(modifier = Modifier
                                    .weight(iconSize)
                                    .aspectRatio(1F))
                                Spacer(modifier = Modifier.weight(spaceSize))
                            }
                        }
                    }
                    * */
                }
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    fun CoilImage(url: String) {
        println("............................................................................ 33 ....")
        println(url)
        Box(
            modifier = Modifier.size(padding_60),
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberImagePainter(
                data = url,
                builder = {
                    placeholder(R.drawable.ic_launcher_background) // TODO CHANGE ICONS
                    error(R.drawable.ic_launcher_foreground) // TODO CHANGE ICONS
                    crossfade(1000)
                    transformations(
                        CircleCropTransformation()
                    )
                }
            )

            val painterStatus = painter.state
            Image(painter = painter, contentDescription = "Image", modifier = Modifier.fillMaxSize())
            if (painterStatus is ImagePainter.State.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}