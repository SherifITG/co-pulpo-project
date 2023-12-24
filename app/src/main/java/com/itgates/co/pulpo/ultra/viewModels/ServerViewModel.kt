package com.itgates.co.pulpo.ultra.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.enumerations.CachingDataTackStatus
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.network.models.requestModels.*
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.repository.OnlineDataRepoImpl
import com.itgates.co.pulpo.ultra.ui.utils.BaseDataActivity
import com.itgates.co.pulpo.ultra.utilities.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ServerViewModel @Inject constructor(
    private val onlineDataRepo: OnlineDataRepoImpl,
    private val dataStoreService: DataStoreService
) : ViewModel() {
    private val _authenticationData = MutableLiveData<LoginPharmaResponse>()
    val authenticationData: LiveData<LoginPharmaResponse> get() = _authenticationData
    private val _userData = MutableLiveData<UserPharmaResponse>()
    val userData: LiveData<UserPharmaResponse> get() = _userData
    private val _masterData = MutableLiveData<MasterDataPharmaResponse>()
    val masterData: LiveData<MasterDataPharmaResponse> get() = _masterData
    private val _accountAndDoctorData = MutableLiveData<AccountsAndDoctorsDetailsPharmaResponse>()
    val accountAndDoctorData: LiveData<AccountsAndDoctorsDetailsPharmaResponse> get() = _accountAndDoctorData
    private val _presentationAndSlideData = MutableLiveData<PresentationsAndSlidesDetailsPharmaResponse>()
    val presentationAndSlideData: LiveData<PresentationsAndSlidesDetailsPharmaResponse> get() = _presentationAndSlideData
    private val _plannedVisitData = MutableLiveData<PlannedVisitsPharmaResponse>()
    val plannedVisitData: LiveData<PlannedVisitsPharmaResponse> get() = _plannedVisitData
    private val _plannedOWData = MutableLiveData<PlannedOWsPharmaResponse>()
    val plannedOWData: LiveData<PlannedOWsPharmaResponse> get() = _plannedOWData
    private val _uploadedActualVisitData = MutableLiveData<ActualVisitPharmaResponse>()
    val uploadedActualVisitData: LiveData<ActualVisitPharmaResponse> get() = _uploadedActualVisitData
    private val _uploadedOfficeWorkData = MutableLiveData<OfficeWorkPharmaResponse>()
    val uploadedOfficeWorkData: LiveData<OfficeWorkPharmaResponse> get() = _uploadedOfficeWorkData
    private val _uploadedVacationData = MutableLiveData<VacationPharmaResponse>()
    val uploadedVacationData: LiveData<VacationPharmaResponse> get() = _uploadedVacationData
    private val _uploadedNewPlanData = MutableLiveData<NewPlanPharmaResponse>()
    val uploadedNewPlanData: LiveData<NewPlanPharmaResponse> get() = _uploadedNewPlanData
    private val _uploadedOfflineLogData = MutableLiveData<OfflineLogPharmaResponse>()
    val uploadedOfflineLogData: LiveData<OfflineLogPharmaResponse> get() = _uploadedOfflineLogData
    private val _uploadedOfflineLocData = MutableLiveData<OfflineLocPharmaResponse>()
    val uploadedOfflineLocData: LiveData<OfflineLocPharmaResponse> get() = _uploadedOfflineLocData

    fun getDataStoreService(): DataStoreService = this.dataStoreService

    private suspend fun fetchFile(context: Context, slidePath: String, fileStructure: String, slideId: Long): Boolean {
        var returnValue = false
        val job = CoroutineManager.getScope().launch {
            try {
                println("------------------------------ 33333333333333333333333333333333333333")
                println("------------------------------ $slidePath")
                val responseBody = onlineDataRepo.getFile(slidePath)

                withContext(Dispatchers.IO) {
                    println("------------------------------ 4444444444444444444444444444444444")

                    val slidesFolderName = "mySlides" // Specify the folder name

                    var slidesFolder = File(context.cacheDir, slidesFolderName)
                    if (!slidesFolder.exists()) {
                        slidesFolder.mkdirs()
                    }
                    slidesFolder = File(slidesFolder, fileStructure)
                    if (!slidesFolder.exists()) {
                        slidesFolder.mkdirs()
                    }

                    val extension = slidePath.split('.').last()
                    val file = File(slidesFolder, "${slideId}_slide.$extension")

                    println("------------------------------ 666666666666666666666666666667")
                    val inputStream = responseBody.byteStream()
                    println("------------------------------ 666666666666666666666666666668")
                    val outputStream = FileOutputStream(file)
                    println("------------------------------ 666666666666666666666666666669")

                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                        println(" -- $bytesRead")
                    }
                    outputStream.flush()
                    println("------------------------------ 555555555555555555555555555555555555")
                    println("------------------------------ $file")
                    println("------------------------------ ${file.totalSpace}")

                    if (extension == ".zip") {
                        val destinationFile = File(
                            slidesFolder, "${slideId}_extracted_slide"
                        )
                        Utilities.extractZipFile(
                            file.absolutePath,
                            destinationFile.absolutePath
                        )
                        println("159159159159159159159159---------------------------159159159159159159159159")
                        println("159159159159159159159159--------------------------- ${destinationFile.absolutePath}")
                    }
                }
                returnValue = true
            } catch (e: Exception) {
                Log.d("ServerViewModel", "getFile failed $e")
                returnValue = false
            }
        }
        job.join()
        // todo return boolean
        return returnValue
    }

    fun userLoginPharma(userModel: UploadedUserModel) {
        CoroutineManager.getScope().launch {
            try {
                val response = onlineDataRepo.authenticationAction(
                    getHeaders(),
                    userModel
                )

                if (response.isSuccessful) {
                    _authenticationData.value = response.body()
                }
                else {
                    _authenticationData.value = Gson().fromJson(
                        JSONObject(response.errorBody()!!.string())
                            .put("status", response.code())
                            .put("access_token", "")
                            .toString(),
                        LoginPharmaResponse::class.java
                    )
                }
            } catch (e: Exception) {
                _authenticationData.value = LoginPharmaResponse(access_token = "", status = 404, message = "your internet is poor")
                Log.d("ServerViewModel", "userLoginPharma failed $e")
            }
        }
    }

    fun fetchUserData(accessToken: String) {
        CoroutineManager.getScope().launch {
            try {
                _userData.value = onlineDataRepo.fetchUserData(getHeaders(accessToken))
                println("")
            } catch (e: Exception) {
                _userData.value = UserPharmaResponse(
                    data = UserDetailsData(0, "0", "", "", "", 0),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "fetchUserData failed $e")
            }
        }
    }

    fun fetchMasterData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _masterData.value = onlineDataRepo.fetchMasterData(getHeaders(accessToken))
                println("888888888888888888888888888888888888888888888888888 ${_masterData.value}")
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _masterData.value = MasterDataPharmaResponse(data = OnlineMasterData(), status = 404, message = "your internet is poor")
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchMasterData failed $e")
            }
        }
    }

    fun fetchAccountsAndDoctorsData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _accountAndDoctorData.value = onlineDataRepo.fetchAccountsAndDoctorsDetailsData(
                    getHeaders(accessToken)
                )
                println("888888888888888888888888888888888888888888888888888 ${_accountAndDoctorData.value}")
                println("${_accountAndDoctorData.value!!.data.accounts.size}")
                println("${_accountAndDoctorData.value!!.data.doctors.size}")
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _accountAndDoctorData.value = AccountsAndDoctorsDetailsPharmaResponse(data = OnlineAccountsAndDoctorsData(), status = 404, message = "your internet is poor")
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchAccountsAndDoctorsData: failed $e")
            }
        }
    }

    fun fetchPresentationsAndSlidesData(context: Context, withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                val response = onlineDataRepo.fetchPresentationsAndSlidesDetailsData(
                    getHeaders(accessToken)
                )

                response.data.slides.forEach {
                    if (!fetchFile(context, it.slide_path, "presentation_${it.presentation_id}", it.id))
                        throw Exception("Error When fetching the file from the server")
                }

                _presentationAndSlideData.value = response

                println("888888888888888888888888888888888888888888888888888 ${_presentationAndSlideData.value}")
                println("${_presentationAndSlideData.value!!.data.presentations.size}")
                println("${_presentationAndSlideData.value!!.data.slides.size}")
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _presentationAndSlideData.value = PresentationsAndSlidesDetailsPharmaResponse(data = OnlinePresentationsAndSlidesData(), status = 404, message = "your internet is poor")
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchPresentationsAndSlidesData: failed $e")
                Firebase.crashlytics.recordException(e)
            }
        }
    }

    fun fetchPlannedVisitData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _plannedVisitData.value = onlineDataRepo.fetchPlannedVisitsData(
                    getHeaders(accessToken)
                )
                println("888888888888888888888888888888888888888888888888888 ${_plannedVisitData.value}")
                println("888888888888888888888888888888888888888888888888888 ${_plannedVisitData.value?.data?.size}")
                println("${_plannedVisitData.value!!.data.size}")
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _plannedVisitData.value = PlannedVisitsPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchPlannedVisitData: failed $e")
            }
        }
    }

    fun fetchPlannedOWData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _plannedOWData.value = onlineDataRepo.fetchPlannedOWsData(
                    getHeaders(accessToken)
                )
                println("888888888888888888888888888888888888888888888888888 ${_plannedOWData.value}")
                println("${_plannedOWData.value!!.data.size}")
                if (withTrack != null) {
                    withTrack.plannedOWDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _plannedOWData.value = PlannedOWsPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                if (withTrack != null) {
                    withTrack.plannedOWDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchPlannedOWData: failed $e")
            }
        }
    }

    fun uploadActualVisitsData(uploadedListObj: UploadedActualVisitsListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _uploadedActualVisitData.value = onlineDataRepo.uploadActualVisitsData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadActualVisitsData -> ${_uploadedActualVisitData.value}")
            } catch (e: Exception) {
                _uploadedActualVisitData.value = ActualVisitPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadActualVisitsData: failed $e")
            }
        }
    }

    fun uploadOfficeWorksData(uploadedListObj: UploadedOfficeWorksListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _uploadedOfficeWorkData.value = onlineDataRepo.uploadOfficeWorksData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadOfficeWorksData -> ${_uploadedActualVisitData.value}")
            } catch (e: Exception) {
                _uploadedOfficeWorkData.value = OfficeWorkPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadOfficeWorksData: failed $e")
            }
        }
    }

    fun uploadVacationsData(uploadedListObj: UploadedVacationsListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _uploadedVacationData.value = onlineDataRepo.uploadVacationsData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadVacationsData -> ${_uploadedVacationData.value}")
            } catch (e: Exception) {
                _uploadedVacationData.value = VacationPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadVacationsData: failed $e")
            }
        }
    }

    fun uploadNewPlansData(uploadedListObj: UploadedNewPlansListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _uploadedNewPlanData.value = onlineDataRepo.uploadNewPlansData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadNewPlansData -> ${_uploadedNewPlanData.value}")
            } catch (e: Exception) {
                _uploadedNewPlanData.value = NewPlanPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadNewPlansData: failed $e")
            }
        }
    }

    fun uploadOfflineLogData(uploadedListObj: UploadedOfflineLogsListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _uploadedOfflineLogData.value = onlineDataRepo.uploadOfflineLogData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadOfflineLogData -> ${_uploadedOfflineLogData.value}")
            } catch (e: Exception) {
                _uploadedOfflineLogData.value = OfflineLogPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadOfflineLogData: failed $e")
            }
        }
    }

    fun uploadOfflineLocData(uploadedListObj: UploadedOfflineLocsListModel) {
        CoroutineManager.getScope().launch {
            val accessToken = dataStoreService.getDataObjAsync(PreferenceKeys.TOKEN).await()
            try {
                _uploadedOfflineLocData.value = onlineDataRepo.uploadOfflineLocData(
                    getHeaders(accessToken),
                    uploadedListObj
                )
                println("server repo -> uploadOfflineLocData -> ${_uploadedOfflineLogData.value}")
            } catch (e: Exception) {
                _uploadedOfflineLocData.value = OfflineLocPharmaResponse(
                    data = ArrayList(),
                    status = 404,
                    message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadOfflineLocData: failed $e")
            }
        }
    }


    private fun getHeaders(authorizationToken: String? = null): HashMap<String, String>  {
        val headers: HashMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["company"] = NetworkConfiguration.configuration.name.lowercase(Locale.ROOT)
        if (authorizationToken != null) {
            headers["Authorization"] = "Bearer $authorizationToken"
        }

        return headers
    }
}