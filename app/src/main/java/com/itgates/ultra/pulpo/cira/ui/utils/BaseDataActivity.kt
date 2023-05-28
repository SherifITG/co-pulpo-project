package com.itgates.ultra.pulpo.cira.ui.utils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.itgates.ultra.pulpo.cira.enumerations.CachingDataTackStatus
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import com.itgates.ultra.pulpo.cira.viewModels.ServerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
open class BaseDataActivity: ComponentActivity() {

    protected val serverViewModel: ServerViewModel by viewModels()
    public val cacheViewModel: CacheViewModel by viewModels()

    var masterDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var accountAndDoctorDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var presentationAndSlideDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)
    var plannedVisitDataTrack = MutableLiveData(CachingDataTackStatus.DATA_TRACK_GET_FIRED)

    protected val internetStateFlow = MutableStateFlow(false)
    protected val loadingStateFlow = MutableStateFlow(false)
    protected val loadingAllStateFlow = MutableLiveData(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {}
    }

    protected open fun setDataObservers() {
        loadingAllStateFlow.observe(this@BaseDataActivity) {
            if (it == 5) {
                loadingAllStateFlow.value = 0
                loadingStateFlow.value = false
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

    protected fun updateMasterData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.fetchMasterData(this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updateAccountAndDoctorData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.fetchAccountsAndDoctorsData(this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updatePresentationAndSlideData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.fetchPresentationsAndSlidesData(applicationContext, this@BaseDataActivity)
        }
        else {
            internetStateFlow.value = true
        }
    }

    protected fun updatePlannedVisitData() {
        if (Utilities.checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.fetchPlannedVisitData(this@BaseDataActivity)
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
}