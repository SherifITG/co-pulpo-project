package com.itgates.co.pulpo.ultra.ui.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.itgates.co.pulpo.ultra.enumerations.CachingDataTackStatus
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import com.itgates.co.pulpo.ultra.viewModels.ServerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
open class BaseDataActivity: ComponentActivity() {
    protected val serverViewModel: ServerViewModel by viewModels()
    val cacheViewModel: CacheViewModel by viewModels()

    var masterDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var accountAndDoctorDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var presentationAndSlideDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var plannedVisitDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var plannedOWDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)

    val loadingStatusList = ArrayList<String>()
    private var numOfLoadingPoints = 1

    val openLoadingStateFlow = MutableStateFlow(false)
    protected val internetStateFlow = MutableStateFlow(false)
    protected val loadingStateFlow = MutableStateFlow(false)
    protected val titleFlow = MutableStateFlow("")
    protected val loadingAllStateFlow = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {}
    }

    protected open fun setDataObservers(doFunWhenAllDataEnd: () -> Unit = {}) {
        loadingAllStateFlow.observe(this@BaseDataActivity) {
            if (it == numOfLoadingPoints) {
                loadingStateFlow.value = false
                loadingAllStateFlow.value = 0

                doFunWhenAllDataEnd()
            }
        }

        masterDataTrack.observe(this@BaseDataActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearMasterData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    saveMasterData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- master")
                    endTrackSteps("Master Data retrieved successfully")
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    endTrackSteps("Error on fetching Master Data from the server")
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    endTrackSteps("Error on deleting Master caches")
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    endTrackSteps("Error on saving Master in the caches")
                }
                else -> {}
            }
        }

        accountAndDoctorDataTrack.observe(this@BaseDataActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearAccountAndDoctorData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    saveAccountAndDoctorData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- doctor")
                    endTrackSteps("Account & Doctor Data retrieved successfully")
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    endTrackSteps("Error on fetching Account & Doctor Data from the server")
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    endTrackSteps("Error on deleting Account & Doctor caches")
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    endTrackSteps("Error on saving Account & Doctor in the caches")
                }
                else -> {}
            }
        }

        presentationAndSlideDataTrack.observe(this@BaseDataActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearPresentationAndSlideData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    savePresentationAndSlideData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- slide")
                    endTrackSteps("Presentation Data retrieved successfully")
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    endTrackSteps("Error on fetching Presentation Data from the server")
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    endTrackSteps("Error on deleting Presentation caches")
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    endTrackSteps("Error on saving Presentation in the caches")
                }
                else -> {}
            }
        }

        plannedVisitDataTrack.observe(this@BaseDataActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearPlannedVisitData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    savePlannedVisitData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- planned")
                    endTrackSteps("Planned Visit Data retrieved successfully")
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    endTrackSteps("Error on fetching Planned Visit Data from the server")
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    endTrackSteps("Error on deleting Planned Visit caches")
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    endTrackSteps("Error on saving Planned Visit in the caches")
                }
                else -> {}
            }
        }

        plannedOWDataTrack.observe(this@BaseDataActivity) {
            when(it) {
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY -> {
                    clearPlannedOWData()
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY -> {
                    savePlannedOWData()
                }
                CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY -> {
                    println("done -- done -- done -- done -- done -- done -- done -- done -- planned ow")
                    endTrackSteps("Planned Office Work Data retrieved successfully")
                }
                CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR -> {
                    endTrackSteps("Error on fetching Planned Office Work Data from the server")
                }
                CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR -> {
                    endTrackSteps("Error on deleting Planned Office Work caches")
                }
                CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR -> {
                    endTrackSteps("Error on saving Planned Office Work in the caches")
                }
                else -> {}
            }
        }
    }

    protected fun updateAllData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            setLoadingAllStateFlow(true)
            titleFlow.value = "Loading All User Data"
            openLoadingStateFlow.value = true
            loadingStateFlow.value = true
            serverViewModel.fetchMasterData(this@BaseDataActivity)
            serverViewModel.fetchAccountsAndDoctorsData(this@BaseDataActivity)
            serverViewModel.fetchPlannedVisitData(this@BaseDataActivity)
            serverViewModel.fetchPlannedOWData(this@BaseDataActivity)
            serverViewModel.fetchPresentationsAndSlidesData(applicationContext, this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updateMasterData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            setLoadingAllStateFlow()
            titleFlow.value = "Loading Master Data"
            openLoadingStateFlow.value = true
            loadingStateFlow.value = true
            serverViewModel.fetchMasterData(this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updateAccountAndDoctorData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            setLoadingAllStateFlow()
            titleFlow.value = "Loading Account & Doctor Data"
            openLoadingStateFlow.value = true
            loadingStateFlow.value = true
            serverViewModel.fetchAccountsAndDoctorsData(this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updatePresentationAndSlideData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            setLoadingAllStateFlow()
            titleFlow.value = "Loading Presentation Data"
            openLoadingStateFlow.value = true
            loadingStateFlow.value = true
            serverViewModel.fetchPresentationsAndSlidesData(applicationContext, this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updatePlannedVisitData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            setLoadingAllStateFlow()
            titleFlow.value = "Loading Planned Visit Data"
            openLoadingStateFlow.value = true
            loadingStateFlow.value = true
            serverViewModel.fetchPlannedVisitData(this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updatePlannedOWData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            setLoadingAllStateFlow()
            titleFlow.value = "Loading Planned Office Work Data"
            openLoadingStateFlow.value = true
            loadingStateFlow.value = true
            serverViewModel.fetchPlannedOWData(this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun clearMasterData() {
        cacheViewModel.clearMasterData(this@BaseDataActivity)
    }

    protected fun clearAccountAndDoctorData() {
        cacheViewModel.clearAccountAndDoctorData(this@BaseDataActivity)
    }

    protected fun clearPresentationAndSlideData() {
        cacheViewModel.clearPresentationAndSlideData(this@BaseDataActivity)
    }

    protected fun clearPlannedVisitData() {
        cacheViewModel.clearPlannedVisitData(this@BaseDataActivity)
    }

    protected fun clearPlannedOWData() {
        cacheViewModel.clearPlannedOWData(this@BaseDataActivity)
    }

    protected fun saveMasterData() {
        cacheViewModel.saveMasterData(serverViewModel.masterData.value!!, this@BaseDataActivity)
    }

    protected fun saveAccountAndDoctorData() {
        cacheViewModel.saveAccountAndDoctorData(serverViewModel.accountAndDoctorData.value!!, this@BaseDataActivity)
    }

    protected fun savePresentationAndSlideData() {
        cacheViewModel.savePresentationAndSlideData(
            serverViewModel.presentationAndSlideData.value!!, this@BaseDataActivity
        )
    }

    protected fun savePlannedVisitData() {
        cacheViewModel.savePlannedVisitData(serverViewModel.plannedVisitData.value!!, this@BaseDataActivity)
    }

    protected fun savePlannedOWData() {
        cacheViewModel.savePlannedOWData(serverViewModel.plannedOWData.value!!, this@BaseDataActivity)
    }

    private fun setLoadingAllStateFlow(isAll: Boolean = false) {
        numOfLoadingPoints = if (isAll) 5 else 1
        loadingAllStateFlow.value = 0
    }

    private fun endTrackSteps(message: String) {
        if (!message.contains("Planned Office")) {
            loadingStatusList.add(message)
        }
        loadingAllStateFlow.value = loadingAllStateFlow.value!! + 1
    }
}