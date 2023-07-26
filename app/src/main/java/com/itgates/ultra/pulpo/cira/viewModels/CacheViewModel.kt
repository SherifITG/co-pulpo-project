package com.itgates.ultra.pulpo.cira.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.DataStoreService
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.enumerations.CachingDataTackStatus
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.*
import com.itgates.ultra.pulpo.cira.repository.OfflineDataRepo
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Slide
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.relationalData.*
import com.itgates.ultra.pulpo.cira.ui.utils.BaseDataActivity
import com.itgates.ultra.pulpo.cira.utilities.FaultedArrayList
import com.itgates.ultra.pulpo.cira.utilities.FaultedHashMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val offlineDataRepo: OfflineDataRepo,
    private val dataStoreService: DataStoreService
) : ViewModel() { /** --------------------------------------- >> ( 'AndroidViewModel' ) << ------ */
    private val _settingData = MutableLiveData<List<Setting>>()
    val settingData: LiveData<List<Setting>> get() = _settingData
    private val _divisionData = MutableLiveData<List<Division>>()
    val divisionData: LiveData<List<Division>> get() = _divisionData
    private val _brickData = MutableLiveData<List<Brick>>()
    val brickData: LiveData<List<Brick>> get() = _brickData
    private val _accountTypeData = MutableLiveData<List<AccountType>>()
    val accountTypeData: LiveData<List<AccountType>> get() = _accountTypeData
    private val _allAccountTypeData = MutableLiveData<List<AccountType>>()
    val allAccountTypeData: LiveData<List<AccountType>> get() = _allAccountTypeData
    private val _classesData = MutableLiveData<List<Class>>()
    val classesData: LiveData<List<Class>> get() = _classesData
    private val _accountData = MutableLiveData<List<Account>>()
    val accountData: LiveData<List<Account>> get() = _accountData
    private val _doctorData = MutableLiveData<List<Doctor>>()
    val doctorData: LiveData<List<Doctor>> get() = _doctorData
    private val _presentationData = MutableLiveData<List<Presentation>>()
    val presentationData: LiveData<List<Presentation>> get() = _presentationData
    private val _idAndNameEntityData = MutableLiveData<List<IdAndNameEntity>>()
    val idAndNameEntityData: LiveData<List<IdAndNameEntity>> get() = _idAndNameEntityData
    private val _slideData = MutableLiveData<List<Slide>>()
    val slideData: LiveData<List<Slide>> get() = _slideData
    private val _unSyncedActualVisitData = MutableLiveData<List<ActualVisit>>()
    val unSyncedActualVisitData: LiveData<List<ActualVisit>> get() = _unSyncedActualVisitData
    private val _unSyncedNewPlanData = MutableLiveData<List<NewPlanEntity>>()
    val unSyncedNewPlanData: LiveData<List<NewPlanEntity>> get() = _unSyncedNewPlanData
    private val _relationalPlannedVisitData = MutableLiveData<List<RelationalPlannedVisit>>()
    val relationalPlannedVisitData: LiveData<List<RelationalPlannedVisit>> get() = _relationalPlannedVisitData
    private val _relationalNewPlanData = MutableLiveData<List<RelationalNewPlan>>()
    val relationalNewPlanData: LiveData<List<RelationalNewPlan>> get() = _relationalNewPlanData
    private val _relationalPlannedOfficeWorkData = MutableLiveData<List<RelationalPlannedOfficeWork>>()
    val relationalPlannedOfficeWorkData: LiveData<List<RelationalPlannedOfficeWork>> get() = _relationalPlannedOfficeWorkData
    private val _relationalActualVisitData = MutableLiveData<List<RelationalActualVisit>>()
    val relationalActualVisitData: LiveData<List<RelationalActualVisit>> get() = _relationalActualVisitData
    private val _relationalOfficeWorkData = MutableLiveData<List<RelationalOfficeWorkReport>>()
    val relationalOfficeWorkData: LiveData<List<RelationalOfficeWorkReport>> get() = _relationalOfficeWorkData
    private val _accountReportData = MutableLiveData<List<AccountData>>()
    val accountReportData: LiveData<List<AccountData>> get() = _accountReportData
    private val _doctorReportData = MutableLiveData<List<DoctorData>>()
    val doctorReportData: LiveData<List<DoctorData>> get() = _doctorReportData
    private val _doctorPlanningData = MutableLiveData<List<DoctorPlanningData>>()
    val doctorPlanningData: LiveData<List<DoctorPlanningData>> get() = _doctorPlanningData

    private val _offlineLogData = MutableLiveData<List<OfflineLog>>()
    val offlineLogData: LiveData<List<OfflineLog>> get() = _offlineLogData
    private val _offlineLocData = MutableLiveData<List<OfflineLoc>>()
    val offlineLocData: LiveData<List<OfflineLoc>> get() = _offlineLocData

    private val _actualVisitStatus = MutableLiveData<Long>()
    val actualVisitStatus: LiveData<Long> get() = _actualVisitStatus

    private val _planningMapStatus = MutableLiveData<HashMap<Int, Long>>()
    val planningMapStatus: LiveData<HashMap<Int, Long>> get() = _planningMapStatus

    private val _plannedVisitMarkedDone = MutableLiveData<Boolean>()
    val plannedVisitMarkedDone: LiveData<Boolean> get() = _plannedVisitMarkedDone

    fun getDataStoreService(): DataStoreService = this.dataStoreService

    fun loadActualSettings() {
        CoroutineManager.getScope().launch {
            try {
                _settingData.value = offlineDataRepo.loadActualSettings()
            } catch (e: Exception) {
                _settingData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualSettings: failed $e")
            }
        }
    }

    fun loadActualAccountTypes(divIds: List<Long>, brickIds: List<Long>) {
        CoroutineManager.getScope().launch {
            try {
                _accountTypeData.value = offlineDataRepo.loadActualAccountTypes(divIds, brickIds)
            } catch (e: Exception) {
                _accountTypeData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualAccountTypes: failed $e")
            }
        }
    }

    fun loadAllAccountTypes() {
        CoroutineManager.getScope().launch {
            try {
                _allAccountTypeData.value = offlineDataRepo.loadAllAccountTypes()
            } catch (e: Exception) {
                _allAccountTypeData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadAllAccountTypes: failed $e")
            }
        }
    }

    fun loadClassesData(divIds: List<Long>, brickIds: List<Long>, accTypeTables: List<String>) {
        CoroutineManager.getScope().launch {
            try {
                _classesData.value = offlineDataRepo.loadClassesData(divIds, brickIds, accTypeTables)
            } catch (e: Exception) {
                _classesData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadClassesData: failed $e")
            }
        }
    }

    fun loadActualUserDivisions() {
        CoroutineManager.getScope().launch {
            try {
                _divisionData.value = offlineDataRepo.loadActualUserDivisions(
                    dataStoreService.getDivisionsOrLinesList(PreferenceKeys.DIVISIONS_IDS)
                )
            } catch (e: Exception) {
                _divisionData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualUserDivisions: failed $e")
            }
        }
    }

    fun loadActualBricks(divIds: List<Long>) {
        CoroutineManager.getScope().launch {
            try {
                _brickData.value = offlineDataRepo.loadActualBricks(divIds)
            } catch (e: Exception) {
                _brickData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualBricks: failed $e")
            }
        }
    }

    fun loadIdAndNameTablesByTAblesListForActualActivity() {
        CoroutineManager.getScope().launch {
            try {
                _idAndNameEntityData.value = offlineDataRepo.loadIdAndNameTablesByTAblesListForActualActivity()
            } catch (e: Exception) {
                _idAndNameEntityData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadIdAndNameTablesByTAblesListForActualActivity: failed $e")
            }
        }
    }

    fun loadSlidesByPresentationId(presentationId: Long) {
        CoroutineManager.getScope().launch {
            try {
                _slideData.value = offlineDataRepo.loadSlidesByPresentationId(presentationId)
            } catch (e: Exception) {
                _slideData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadByPresentationId: failed $e")
            }
        }
    }

    fun loadOfficeWorkTypes() {
        CoroutineManager.getScope().launch {
            try {
                _idAndNameEntityData.value = offlineDataRepo.loadOfficeWorkTypes()
            } catch (e: Exception) {
                _idAndNameEntityData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadOfficeWorkTypes: failed $e")
            }
        }
    }

    fun loadProductsData() {
        CoroutineManager.getScope().launch {
            try {
                _idAndNameEntityData.value = offlineDataRepo.loadProducts()
            } catch (e: Exception) {
                _idAndNameEntityData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadProductsData: failed $e")
            }
        }
    }

    fun loadActualAccounts(divId: Long, brickId: Long, table: String) {
        CoroutineManager.getScope().launch {
            try {
                _accountData.value = offlineDataRepo.loadActualAccounts(divId, brickId, table)
            } catch (e: Exception) {
                _accountData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualAccounts: failed $e")
            }
        }
    }

    fun loadActualDoctors(accountId: Long, table: String) {
        CoroutineManager.getScope().launch {
            try {
                _doctorData.value = offlineDataRepo.loadActualDoctors(accountId, table)
            } catch (e: Exception) {
                _doctorData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualDoctors: failed $e")
            }
        }
    }

    fun loadPresentations() {
        CoroutineManager.getScope().launch {
            try {
                _presentationData.value = offlineDataRepo.loadPresentations()
            } catch (e: Exception) {
                _presentationData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadPresentations: failed $e")
            }
        }
    }

    fun loadUnSyncedActualVisits() {
        CoroutineManager.getScope().launch {
            try {
                _unSyncedActualVisitData.value = offlineDataRepo.loadUnSyncedActualVisitsData()
            } catch (e: Exception) {
                _unSyncedActualVisitData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedActualVisits: failed $e")
            }
        }
    }

    fun loadUnSyncedNewPlans() {
        CoroutineManager.getScope().launch {
            try {
                _unSyncedNewPlanData.value = offlineDataRepo.loadUnSyncedActualNewPlansData()
            } catch (e: Exception) {
                _unSyncedNewPlanData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedActualNewPlans: failed $e")
            }
        }
    }

    fun loadRelationalPlannedVisits(isTodayFilter: Boolean) {
        CoroutineManager.getScope().launch {
            try {
                _relationalPlannedVisitData.value = if (isTodayFilter) {
                    offlineDataRepo.loadTodayRelationalPlannedVisitsData()
                }
                else {
                    offlineDataRepo.loadRelationalPlannedVisitsData()
                }
            } catch (e: Exception) {
                _relationalPlannedVisitData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadRelationalPlannedVisits: failed $e")
            }
        }
    }

    fun loadRelationalNewPlans() {
        CoroutineManager.getScope().launch {
            try {
                _relationalNewPlanData.value = offlineDataRepo.loadRelationalNewPlansData()
            } catch (e: Exception) {
                _relationalNewPlanData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadRelationalNewPlans: failed $e")
            }
        }
    }

    fun loadRelationalPlannedOfficeWorks(isTodayFilter: Boolean) {
        CoroutineManager.getScope().launch {
            try {
                _relationalPlannedOfficeWorkData.value = if (isTodayFilter) {
                    offlineDataRepo.loadRelationalPlannedOfficeWorksData()
                }
                else {
                    offlineDataRepo.loadRelationalPlannedOfficeWorksData()
                }
            } catch (e: Exception) {
                _relationalPlannedOfficeWorkData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadRelationalPlannedOfficeWorks: failed $e")
            }
        }
    }

    fun markPlannedVisitAsDone(doneId: Long) {
        CoroutineManager.getScope().launch {
            try {
                _plannedVisitMarkedDone.value = offlineDataRepo.markPlannedVisitAsDone(doneId)
            } catch (e: Exception) {
                _plannedVisitMarkedDone.value = false
                Log.d("CacheViewModel", "markPlannedVisitAsDone: failed $e")
            }
        }
    }

    fun loadRelationalActualVisits() {
        CoroutineManager.getScope().launch {
            try {
                _relationalActualVisitData.value = offlineDataRepo.loadRelationalActualVisitsData()
            } catch (e: Exception) {
                _relationalActualVisitData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadRelationalActualVisits: failed $e")
            }
        }
    }

    fun loadRelationalOfficeWorkReportsData() {
        CoroutineManager.getScope().launch {
            try {
                _relationalOfficeWorkData.value = offlineDataRepo.loadRelationalOfficeWorkReportsData()
            } catch (e: Exception) {
                _relationalOfficeWorkData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadRelationalOfficeWorkReportsData: failed $e")
            }
        }
    }

    fun loadAllAccountReportData() {
        CoroutineManager.getScope().launch {
            try {
                _accountReportData.value = offlineDataRepo.loadAllAccountReportData()
            } catch (e: Exception) {
                _accountReportData.value = FaultedArrayList()
                Log.d("CacheViewModel4", "loadAllAccountReportData: failed $e")
            }
        }
    }

    fun updateAccountLocation(llFirst: String, lgFirst: String, id: Long, table: String) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.updateAccountLocation(llFirst, lgFirst, id, table)
            } catch (e: Exception) {
                Log.d("CacheViewModel", "updateAccountLocation: failed $e")
            }
        }
    }

    fun loadAllDoctorReportData() {
        CoroutineManager.getScope().launch {
            try {
                _doctorReportData.value = offlineDataRepo.loadAllDoctorReportData()
            } catch (e: Exception) {
                _doctorReportData.value = FaultedArrayList()
                Log.d("CacheViewModel3", "loadAllDoctorReportData: failed $e")
            }
        }
    }

    fun loadAllDoctorPlanningData() {
        CoroutineManager.getScope().launch {
            try {
                _doctorPlanningData.value = offlineDataRepo.loadAllDoctorPlanningData()
            } catch (e: Exception) {
                _doctorPlanningData.value = FaultedArrayList()
                Log.d("CacheViewModel6", "loadAllDoctorPlanningData: failed $e")
            }
        }
    }

    fun uploadedActualVisitData(actualVisitDTO: ActualVisitDTO) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.uploadedActualVisitData(actualVisitDTO)
                Log.d("CacheViewModel", "uploadedActualVisitData: success $actualVisitDTO")
            } catch (e: Exception) {
                Log.d("CacheViewModel", "uploadedActualVisitData: failed $e")
            }
        }
    }

    fun uploadedNewPlanData(newPlanDTO: NewPlanDTO) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.uploadedNewPlanData(newPlanDTO)
                Log.d("CacheViewModel", "uploadedNewPlanData: success $newPlanDTO")
            } catch (e: Exception) {
                Log.d("CacheViewModel", "uploadedNewPlanData: failed $e")
            }
        }
    }

    fun insertActualVisitWithValidation(actualVisit: ActualVisit) {
        CoroutineManager.getScope().launch {
            try {
                _actualVisitStatus.value = offlineDataRepo.insertActualVisitWithValidation(actualVisit)
            } catch (e: Exception) {
                _actualVisitStatus.value = -2
                Log.d("CacheViewModel", "insertActualVisitWithValidation: failed $e")
            }
        }
    }

    fun saveMasterData(
        masterDataPharmaResponse: MasterDataPharmaResponse,
        withTrack: BaseDataActivity? = null
    ) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.saveMasterData(masterDataPharmaResponse)
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR
                }
                Log.d("CacheViewModel", "saveMasterData: failed $e")
            }
        }
    }

    fun saveAccountAndDoctorData(
        accountsAndDoctorsDetailsPharmaResponse: AccountsAndDoctorsDetailsPharmaResponse,
        withTrack: BaseDataActivity? = null
    ) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.saveAccountAndDoctorData(accountsAndDoctorsDetailsPharmaResponse)
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR
                }
                Log.d("CacheViewModel", "saveAccountAndDoctorData: failed $e")
            }
        }
    }

    fun savePresentationAndSlideData(
        presentationsAndSlidesDetailsPharmaResponse: PresentationsAndSlidesDetailsPharmaResponse,
        withTrack: BaseDataActivity? = null
    ) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.savePresentationAndSlideData(presentationsAndSlidesDetailsPharmaResponse)
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR
                }
                Log.d("CacheViewModel", "savePresentationAndSlideData: failed $e")
            }
        }
    }

    fun savePlannedVisitData(
        plannedVisitsPharmaResponse: PlannedVisitsPharmaResponse,
        withTrack: BaseDataActivity? = null
    ) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.savePlannedVisitData(plannedVisitsPharmaResponse)
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR
                }
                Log.d("CacheViewModel", "saveAllMasterData: failed $e")
            }
        }
    }

    fun saveOfflineLog(offlineLog: OfflineLog) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.saveOfflineLog(offlineLog)
            } catch (e: Exception) {
                Log.d("CacheViewModel", "saveOfflineLog: failed $e")
            }
        }
    }

    fun saveOfflineLoc(offlineLoc: OfflineLoc) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.saveOfflineLoc(offlineLoc)
            } catch (e: Exception) {
                Log.d("CacheViewModel", "saveOfflineLoc: failed $e")
            }
        }
    }

    fun saveNewPlans(newPlanList: List<NewPlanEntity>) {
        CoroutineManager.getScope().launch {
            try {
                _planningMapStatus.value = offlineDataRepo.saveNewPlans(newPlanList)
            } catch (e: Exception) {
                _planningMapStatus.value = FaultedHashMap()
                Log.d("CacheViewModel", "saveNewPlans: failed $e")
            }
        }
    }

    fun uploadedOfflineLogs(offlineLogDTO: OfflineRecordDTO) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.uploadedOfflineLogData(offlineLogDTO)
            } catch (e: Exception) {
                Log.d("CacheViewModel", "uploadedOfflineLogs: failed $e")
            }
        }
    }

    fun uploadedOfflineLocations(offlineLocDTO: OfflineRecordDTO) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.uploadedOfflineLocData(offlineLocDTO)
            } catch (e: Exception) {
                Log.d("CacheViewModel", "uploadedOfflineLocations: failed $e")
            }
        }
    }

    fun loadUnSyncedOfflineLogs() {
        CoroutineManager.getScope().launch {
            try {
                _offlineLogData.value = offlineDataRepo.loadUnSyncedOfflineLogData()
            } catch (e: Exception) {
                _offlineLogData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedOfflineLogs: failed $e")
            }
        }
    }

    fun loadUnSyncedOfflineLocations() {
        CoroutineManager.getScope().launch {
            try {
                _offlineLocData.value = offlineDataRepo.loadUnSyncedOfflineLocData()
            } catch (e: Exception) {
                _offlineLocData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedOfflineLocations: failed $e")
            }
        }
    }

    fun clearMasterData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.clearMasterData()
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.masterDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR
                }
                Log.d("CacheViewModel", "clearMasterData: failed $e")
            }
        }
    }

    fun clearAccountAndDoctorData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.clearAccountAndDoctorData()
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.accountAndDoctorDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR
                }
                Log.d("CacheViewModel", "clearAccountAndDoctorData: failed $e")
            }
        }
    }

    fun clearPresentationAndSlideData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.clearPresentationAndSlideData()
                // delete the file dir

                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.presentationAndSlideDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR
                }
                Log.d("CacheViewModel", "clearPresentationAndSlideData: failed $e")
            }
        }
    }

    fun clearPlannedVisitData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.clearPlannedVisitData()
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.plannedVisitDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR
                }
                Log.d("CacheViewModel", "clearPlannedVisitData: failed $e")
            }
        }
    }
}