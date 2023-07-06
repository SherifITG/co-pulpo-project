package com.itgates.ultra.pulpo.cira.viewModels

import android.content.Context
import android.util.Log
import androidx.core.text.trimmedLength
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.DataStoreService
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.enumerations.CachingDataTackStatus
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedActualVisitModel
import com.itgates.ultra.pulpo.cira.network.models.requestModels.UploadedNewPlanModel
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import com.itgates.ultra.pulpo.cira.repository.OnlineDataRepoImpl
import com.itgates.ultra.pulpo.cira.ui.utils.BaseDataActivity
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private val _masterData = MutableLiveData<MasterDataPharmaResponse>()
    val masterData: LiveData<MasterDataPharmaResponse> get() = _masterData
    private val _accountAndDoctorData = MutableLiveData<AccountsAndDoctorsDetailsPharmaResponse>()
    val accountAndDoctorData: LiveData<AccountsAndDoctorsDetailsPharmaResponse> get() = _accountAndDoctorData
    private val _presentationAndSlideData = MutableLiveData<PresentationsAndSlidesDetailsPharmaResponse>()
    val presentationAndSlideData: LiveData<PresentationsAndSlidesDetailsPharmaResponse> get() = _presentationAndSlideData
    private val _plannedVisitData = MutableLiveData<PlannedVisitsPharmaResponse>()
    val plannedVisitData: LiveData<PlannedVisitsPharmaResponse> get() = _plannedVisitData
    private val _uploadedActualVisitData = MutableLiveData<ActualVisitPharmaResponse>()
    val uploadedActualVisitData: LiveData<ActualVisitPharmaResponse> get() = _uploadedActualVisitData
    private val _uploadedNewPlanData = MutableLiveData<NewPlanPharmaResponse>()
    val uploadedNewPlanData: LiveData<NewPlanPharmaResponse> get() = _uploadedNewPlanData

    private suspend fun getFile(context: Context, fileName: String, fileStructure: String, slideId: Long) {
        val job = CoroutineManager.getScope().launch {
            try {
                println("------------------------------ 33333333333333333333333333333333333333")
                val responseBody = onlineDataRepo.getFile(fileStructure.plus("/").plus(fileName))

                withContext(Dispatchers.IO) {
                    println("------------------------------ 4444444444444444444444444444444444")

                    val folderName = "mySlides" // Specify the folder name

                    var slidesFolder = File(context.cacheDir, folderName)
                    if (!slidesFolder.exists()) {
                        slidesFolder.mkdirs()
                    }
                    slidesFolder = File(slidesFolder, fileStructure)
                    if (!slidesFolder.exists()) {
                        slidesFolder.mkdirs()
                    }

                    var file = File(slidesFolder, "${slideId}_$fileName")

                    println("------------------------------ 666666666666666666666666666667")
                    val inputStream = responseBody.byteStream()
                    println("------------------------------ 666666666666666666666666666668")
                    val outputStream = FileOutputStream(file)
                    println("------------------------------ 666666666666666666666666666669")

                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                    outputStream.flush()
                    println("------------------------------ 555555555555555555555555555555555555")
                    println("------------------------------ $file")

                    if (fileName.endsWith(".zip")) {
                        val destinationFile = File(
                            slidesFolder, "${slideId}_extracted_${fileName.subSequence(0, fileName.length-4)}"
                        )
                        Utilities.extractZipFile(
                            file.absolutePath,
                            destinationFile.absolutePath
                        )
                        println("159159159159159159159159---------------------------159159159159159159159159")
                        println("159159159159159159159159--------------------------- ${destinationFile.absolutePath}")
                    }
                }
            } catch (e: Exception) {
                Log.d("ServerViewModel", "getFile failed $e")
            }
        }
        job.join()
        // todo return boolean
    }

    fun userLoginPharma(username: String, password: String) {
        CoroutineManager.getScope().launch {
            try {
                _authenticationData.value = onlineDataRepo.authenticationAction(
                    getHeaders(),
                    "CheckLogin",
                    username,
                    password
                )
            } catch (e: Exception) {
                _authenticationData.value = LoginPharmaResponse(Data = ArrayList(), Status = 404, Status_Message = "your internet is poor")
                Log.d("ServerViewModel", "userLoginPharma failed $e")
            }
        }
    }

    fun fetchMasterData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
                _masterData.value = onlineDataRepo.fetchMasterData(
                    getHeaders(),
                    "GetMasterData",
                    today,
                    dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await(),
                    dataStoreService.getDataObjAsync(PreferenceKeys.LINES_IDS).await(),
                    dataStoreService.getDataObjAsync(PreferenceKeys.DIVISIONS_IDS).await()
                )
                println("888888888888888888888888888888888888888888888888888 ${_masterData.value}")
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _masterData.value = MasterDataPharmaResponse(Data = OnlineMasterData(), Status = 404, Status_Message = "your internet is poor")
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
                Log.d("ServerViewModel", "fetchMasterData failed $e")
            }
        }
    }

    fun fetchAccountsAndDoctorsData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                _accountAndDoctorData.value = onlineDataRepo.fetchAccountsAndDoctorsDetailsData(
                    getHeaders(),
                    "GetAllAccountTypeTeamAndDoctorDetails",
                    dataStoreService.getDataObjAsync(PreferenceKeys.LINES_IDS).await(),
                    dataStoreService.getDataObjAsync(PreferenceKeys.DIVISIONS_IDS).await()
                )
                println("888888888888888888888888888888888888888888888888888 ${_accountAndDoctorData.value}")
                println("${_accountAndDoctorData.value!!.Data.accounts.size}")
                println("${_accountAndDoctorData.value!!.Data.doctors.size}")
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _accountAndDoctorData.value = AccountsAndDoctorsDetailsPharmaResponse(Data = OnlineAccountsAndDoctorsData(), Status = 404, Status_Message = "your internet is poor")
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
            try {
                val response = onlineDataRepo.fetchPresentationsAndSlidesDetailsData(
                    getHeaders(),
                    "GetPresentations", //"GetPresentationsAndSlides",
                    dataStoreService.getDataObjAsync(PreferenceKeys.LINES_IDS).await()
                )

                response.Data.Slides.forEach {
                    getFile(context, it.file_path, it.structure, it.id)
                }

                _presentationAndSlideData.value = response

                println("888888888888888888888888888888888888888888888888888 ${_presentationAndSlideData.value}")
                println("${_presentationAndSlideData.value!!.Data.Presentations.size}")
                println("${_presentationAndSlideData.value!!.Data.Slides.size}")
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _presentationAndSlideData.value = PresentationsAndSlidesDetailsPharmaResponse(Data = OnlinePresentationsAndSlidesData(), Status = 404, Status_Message = "your internet is poor")
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchPresentationsAndSlidesData: failed $e")
            }
        }
    }

    fun fetchPlannedVisitData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                val today = GlobalFormats.getDashedDate(Locale.getDefault(), Date())
                _plannedVisitData.value = onlineDataRepo.fetchPlannedVisitsData(
                    getHeaders(),
                    "GetUpcomingPlannedVisits",
                    today,
                    dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await()
                )
                println("888888888888888888888888888888888888888888888888888 ${_plannedVisitData.value}")
                println("${_plannedVisitData.value!!.Data.size}")
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_SUCCESSFULLY
                }
            } catch (e: Exception) {
                _plannedVisitData.value = PlannedVisitsPharmaResponse(
                    Data = ArrayList(),
                    Status = 404,
                    Status_Message = "your internet is poor"
                )
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.DATA_FETCHED_FROM_SERVER_WITH_ERROR
                }
                Log.d("ServerViewModel", "fetchPlannedVisitData: failed $e")
            }
        }
    }

    fun uploadActualVisitsData(uploadedList: List<UploadedActualVisitModel>) {
        CoroutineManager.getScope().launch {
            try {
                _uploadedActualVisitData.value = onlineDataRepo.uploadActualVisitsData(
                    getHeaders(),
                    uploadedList
                )
                println("server repo -> uploadActualVisitsData -> ${_uploadedActualVisitData.value}")
            } catch (e: Exception) {
                _uploadedActualVisitData.value = ActualVisitPharmaResponse(
                    Data = ArrayList(),
                    Status = 404,
                    Status_Message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadActualVisitsData: failed $e")
            }
        }
    }

    fun uploadNewPlansData(uploadedList: List<UploadedNewPlanModel>) {
        CoroutineManager.getScope().launch {
            try {
                _uploadedNewPlanData.value = onlineDataRepo.uploadNewPlansData(
                    getHeaders(),
                    uploadedList
                )
                println("server repo -> uploadNewPlansData -> ${_uploadedNewPlanData.value}")
            } catch (e: Exception) {
                _uploadedNewPlanData.value = NewPlanPharmaResponse(
                    Data = ArrayList(),
                    Status = 404,
                    Status_Message = "your internet is poor"
                )
                Log.d("ServerViewModel", "uploadNewPlansData: failed $e")
            }
        }
    }


    private fun getHeaders(): HashMap<String, String>  {
        val headers: HashMap<String, String> = HashMap()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        return headers
    }
}