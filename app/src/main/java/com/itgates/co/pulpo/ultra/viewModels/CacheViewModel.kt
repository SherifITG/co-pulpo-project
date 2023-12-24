package com.itgates.co.pulpo.ultra.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.DataStoreService
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.enumerations.CachingDataTackStatus
import com.itgates.co.pulpo.ultra.network.models.responseModels.responses.*
import com.itgates.co.pulpo.ultra.repository.OfflineDataRepo
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Slide
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.*
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.DurationEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.ShiftEnum
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.relationalData.*
import com.itgates.co.pulpo.ultra.ui.utils.BaseDataActivity
import com.itgates.co.pulpo.ultra.utilities.FaultedArrayList
import com.itgates.co.pulpo.ultra.utilities.FaultedHashMap
import com.itgates.co.pulpo.ultra.utilities.Utilities
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class CacheViewModel @Inject constructor(
    private val offlineDataRepo: OfflineDataRepo,
    private val dataStoreService: DataStoreService
) : ViewModel() { /** --------------------------------------- >> ( 'AndroidViewModel' ) << ------ */
    private val _genericData = MutableLiveData<List<Any>>()
    val genericData: LiveData<List<Any>> get() = _genericData
    private val _settingData = MutableLiveData<List<Setting>>()
    val settingData: LiveData<List<Setting>> get() = _settingData
    private val _lineData = MutableLiveData<List<RelationalLine>>()
    val lineData: LiveData<List<RelationalLine>> get() = _lineData
    private val _divisionData = MutableLiveData<List<Division>>()
    val divisionData: LiveData<List<Division>> get() = _divisionData
    private val _brickData = MutableLiveData<List<Brick>>()
    val brickData: LiveData<List<Brick>> get() = _brickData
    private val _accountTypeData = MutableLiveData<List<AccountType>>()
    val accountTypeData: LiveData<List<AccountType>> get() = _accountTypeData
    private val _allAccountTypeData = MutableLiveData<List<AccountType>>()
    val allAccountTypeData: LiveData<List<AccountType>> get() = _allAccountTypeData
    private val _classesData = MutableLiveData<List<IdAndNameEntity>>()
    val classesData: LiveData<List<IdAndNameEntity>> get() = _classesData
    private val _accountData = MutableLiveData<List<Account>>()
    val accountData: LiveData<List<Account>> get() = _accountData
    private val _doctorData = MutableLiveData<List<Doctor>>()
    val doctorData: LiveData<List<Doctor>> get() = _doctorData
    private val _presentationData = MutableLiveData<List<Presentation>>()
    val presentationData: LiveData<List<Presentation>> get() = _presentationData
    private val _idAndNameEntityData = MutableLiveData<List<IdAndNameEntity>>()
    val idAndNameEntityData: LiveData<List<IdAndNameEntity>> get() = _idAndNameEntityData
    private val _vacationTypeData = MutableLiveData<List<VacationType>>()
    val vacationTypeData: LiveData<List<VacationType>> get() = _vacationTypeData
    private val _slideData = MutableLiveData<List<Slide>>()
    val slideData: LiveData<List<Slide>> get() = _slideData
    private val _unSyncedActualVisitData = MutableLiveData<List<ActualVisit>>()
    val unSyncedActualVisitData: LiveData<List<ActualVisit>> get() = _unSyncedActualVisitData
    private val _unSyncedOfficeWorkData = MutableLiveData<List<OfficeWork>>()
    val unSyncedOfficeWorkData: LiveData<List<OfficeWork>> get() = _unSyncedOfficeWorkData
    private val _unSyncedVacationData = MutableLiveData<List<Vacation>>()
    val unSyncedVacationData: LiveData<List<Vacation>> get() = _unSyncedVacationData
    private val _unSyncedNewPlanData = MutableLiveData<List<NewPlanEntity>>()
    val unSyncedNewPlanData: LiveData<List<NewPlanEntity>> get() = _unSyncedNewPlanData
    private val _unSyncedOfflineLogData = MutableLiveData<List<OfflineLog>>()
    val unSyncedOfflineLogData: LiveData<List<OfflineLog>> get() = _unSyncedOfflineLogData
    private val _unSyncedOfflineLocData = MutableLiveData<List<OfflineLoc>>()
    val unSyncedOfflineLocData: LiveData<List<OfflineLoc>> get() = _unSyncedOfflineLocData
    private val _relationalPlannedVisitData = MutableLiveData<List<RelationalPlannedVisit>>()
    val relationalPlannedVisitData: LiveData<List<RelationalPlannedVisit>> get() = _relationalPlannedVisitData
    private val _relationalNewPlanData = MutableLiveData<List<RelationalNewPlan>>()
    val relationalNewPlanData: LiveData<List<RelationalNewPlan>> get() = _relationalNewPlanData
    private val _relationalPlannedOfficeWorkData = MutableLiveData<List<RelationalPlannedOfficeWork>>()
    val relationalPlannedOfficeWorkData: LiveData<List<RelationalPlannedOfficeWork>> get() = _relationalPlannedOfficeWorkData
    private val _todayPlannedVisitsAndOWCount = MutableLiveData<Long>()
    val todayPlannedVisitsAndOWCount: LiveData<Long> get() = _todayPlannedVisitsAndOWCount
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

    private val _actualVisitStatus = MutableLiveData<Long>()
    val actualVisitStatus: LiveData<Long> get() = _actualVisitStatus
    private val _officeWorkStatus = MutableLiveData<Long>()
    val officeWorkStatus: LiveData<Long> get() = _officeWorkStatus
    private val _vacationStatus = MutableLiveData<Long>()
    val vacationStatus: LiveData<Long> get() = _vacationStatus

    private val _planningMapStatus = MutableLiveData<HashMap<Int, Long>>()
    val planningMapStatus: LiveData<HashMap<Int, Long>> get() = _planningMapStatus

    private val _plannedVisitMarkedDone = MutableLiveData<Boolean>()
    val plannedVisitMarkedDone: LiveData<Boolean> get() = _plannedVisitMarkedDone
    private val _plannedOWMarkedDone = MutableLiveData<Boolean>()
    val plannedOWMarkedDone: LiveData<Boolean> get() = _plannedOWMarkedDone

    fun getDataStoreService(): DataStoreService = this.dataStoreService

    fun genericLoadAll(tableName: String) {
        CoroutineManager.getScope().launch {
            try {
                _genericData.value = offlineDataRepo.genericLoadAll(tableName)
            } catch (e: Exception) {
                _genericData.value = FaultedArrayList()
                Log.d("CacheViewModel", "genericLoadAll: failed $e")
            }
        }
    }

    private suspend fun copyFile(context: Context, filePath: String): String {
        var returnValue = ""
        val job = CoroutineManager.getScope().launch {
            try {
                println("------------------------------ 33333333333333333333333333333333333333")
                println("------------------------------ $filePath")

                withContext(Dispatchers.IO) {
                    println("------------------------------ 4444444444444444444444444444444444")

                    val filesFolderName = "myFiles" // Specify the folder name

                    val filesFolder = File(context.cacheDir, filesFolderName)
                    if (!filesFolder.exists()) {
                        filesFolder.mkdirs()
                    }

                    val extension = filePath.split('.').last()
                    val file = File(filesFolder, "${Date().time}_file.$extension")

                    println("------------------------------ 666666666666666666666666666667")
                    val inputStream = FileInputStream(File(filePath))
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
                    println("------------------------------ ${file.absolutePath}")
                    returnValue = file.absolutePath
                }
            } catch (e: Exception) {
                Log.d("ServerViewModel", "getFile failed $e")
                returnValue = "error"
            }
        }
        job.join()
        // todo return boolean
        return returnValue
    }

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

    fun loadMainActivitySettings() {
        CoroutineManager.getScope().launch {
            try {
                _settingData.value = offlineDataRepo.loadMainActivitySettings()
            } catch (e: Exception) {
                _settingData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadMainActivitySettings: failed $e")
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

    fun loadClassesData(divIds: List<Long>, brickIds: List<Long>, accTypeIds: List<Int>) {
        CoroutineManager.getScope().launch {
            try {
                _classesData.value = offlineDataRepo.loadClassesData(divIds, brickIds, accTypeIds)
            } catch (e: Exception) {
                _classesData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadClassesData: failed $e")
            }
        }
    }

    fun loadRelationalLines() {
        CoroutineManager.getScope().launch {
            try {
                _lineData.value = offlineDataRepo.loadRelationalLines()
            } catch (e: Exception) {
                _lineData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadRelationalLines: failed $e")
            }
        }
    }

    fun loadUserDivisions() {
        CoroutineManager.getScope().launch {
            try {
                _divisionData.value = offlineDataRepo.loadUserDivisions()
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

    fun loadIdAndNameTablesByTAblesListForActualActivity(lineId: Long) {
        CoroutineManager.getScope().launch {
            try {
                _idAndNameEntityData.value = offlineDataRepo.loadIdAndNameTablesByTAblesListForActualActivity(lineId)
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

    fun loadVacationTypesData() {
        CoroutineManager.getScope().launch {
            try {
                _vacationTypeData.value = offlineDataRepo.loadVacationTypes()
            } catch (e: Exception) {
                _vacationTypeData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadVacationTypesData: failed $e")
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

    fun loadActualAccounts(lineId: Long, divId: Long, brickId: Long, accTypeId: Int) {
        CoroutineManager.getScope().launch {
            try {
                _accountData.value = offlineDataRepo.loadActualAccounts(lineId, divId, brickId, accTypeId)
            } catch (e: Exception) {
                _accountData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadActualAccounts: failed $e")
            }
        }
    }

    fun loadActualDoctors(lindId: Long, accountId: Long, accTypeId: Int) {
        CoroutineManager.getScope().launch {
            try {
                _doctorData.value = offlineDataRepo.loadActualDoctors(lindId, accountId, accTypeId)
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

    fun loadUnSyncedOfficeWorks() {
        CoroutineManager.getScope().launch {
            try {
                _unSyncedOfficeWorkData.value = offlineDataRepo.loadUnSyncedOfficeWorksData()
            } catch (e: Exception) {
                _unSyncedOfficeWorkData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedOfficeWorks: failed $e")
            }
        }
    }

    fun loadUnSyncedVacations() {
        CoroutineManager.getScope().launch {
            try {
                _unSyncedVacationData.value = offlineDataRepo.loadUnSyncedVacationsData()
            } catch (e: Exception) {
                _unSyncedVacationData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedVacations: failed $e")
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

    fun loadUnSyncedOfflineLogs() {
        CoroutineManager.getScope().launch {
            try {
                _unSyncedOfflineLogData.value = offlineDataRepo.loadUnSyncedOfflineLogData()
            } catch (e: Exception) {
                _unSyncedOfflineLogData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedOfflineLog: failed $e")
            }
        }
    }

    fun loadUnSyncedOfflineLocations() {
        CoroutineManager.getScope().launch {
            try {
                _unSyncedOfflineLocData.value = offlineDataRepo.loadUnSyncedOfflineLocData()
            } catch (e: Exception) {
                _unSyncedOfflineLocData.value = FaultedArrayList()
                Log.d("CacheViewModel", "loadUnSyncedOfflineLocations: failed $e")
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
                    offlineDataRepo.loadTodayRelationalPlannedOfficeWorksData()
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

    fun loadTodayPlannedVisitsAndOWCount() {
        CoroutineManager.getScope().launch {
            try {
                _todayPlannedVisitsAndOWCount.value = offlineDataRepo.loadTodayPlannedVisitsAndOWCount()
            } catch (e: Exception) {
                _todayPlannedVisitsAndOWCount.value = 0L
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

    fun markPlannedOfficeWorkAsDone(doneId: Long) {
        CoroutineManager.getScope().launch {
            try {
                _plannedOWMarkedDone.value = offlineDataRepo.markPlannedOfficeWorkAsDone(doneId)
            } catch (e: Exception) {
                _plannedOWMarkedDone.value = false
                Log.d("CacheViewModel", "markPlannedOfficeWorkAsDone: failed $e")
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
                Log.d("CacheViewModel", "loadAllAccountReportData: failed $e")
            }
        }
    }

    fun updateAccountLocation(
        llFirst: String, lgFirst: String,
        id: Long, accTypeId: Int, lineId: Long, divId: Long, brickId: Long
    ) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.updateAccountLocation(
                    llFirst, lgFirst, id, accTypeId, lineId, divId, brickId
                )
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
                Log.d("CacheViewModel", "loadAllDoctorPlanningData: failed $e")
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

    // Todo check later
    fun uploadedOfficeWorkData(officeWorkDTO: OfficeWorkDTO) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.uploadedOfficeWorkData(officeWorkDTO)
                Log.d("CacheViewModel", "uploadedOfficeWorkData: success $officeWorkDTO")
            } catch (e: Exception) {
                Log.d("CacheViewModel", "uploadedOfficeWorkData: failed $e")
            }
        }
    }

    // Todo check later
    fun uploadedVacationData(vacationDTO: VacationDTO) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.uploadedVacationData(vacationDTO)
                Log.d("CacheViewModel", "uploadedVacationData: success $vacationDTO")
            } catch (e: Exception) {
                Log.d("CacheViewModel", "uploadedVacationData: failed $e")
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

    fun insertOfficeWorkWithValidation(officeWork: OfficeWork) {
        CoroutineManager.getScope().launch {
            try {
                if (officeWork.shift == ShiftEnum.FULL_DAY) {
                    _officeWorkStatus.value = offlineDataRepo.insertFullDayActualOfficeWorkWithValidation(officeWork)
                }
                else {
                    _officeWorkStatus.value = offlineDataRepo.insertOfficeWorkWithValidation(officeWork)
                }
            } catch (e: Exception) {
                _officeWorkStatus.value = -2
                Log.d("CacheViewModel", "insertOfficeWorkWithValidation: failed $e")
            }
        }
    }

    fun insertVacationValidation(context: Context, vacation: Vacation) {
        CoroutineManager.getScope().launch {
            try {
                vacation.uriListsInfo.paths = vacation.uriListsInfo.paths.map {
                    val newPath = copyFile(context, it)
                    if (newPath == "" || newPath == "error") throw Exception("Error When Copping file")
                    else return@map newPath
                }
                if (vacation.durationType == DurationEnum.FULL_DAY) {
                    _vacationStatus.value = offlineDataRepo.insertFullDayVacationWithValidation(vacation)
                }
                else {
                    _vacationStatus.value = offlineDataRepo.insertHalfDayVacationWithValidation(vacation)
                }
            } catch (e: Exception) {
                _vacationStatus.value = -2
                Log.d("CacheViewModel", "insertVacationValidation: failed $e")
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
                Log.d("CacheViewModel", "savePlannedVisitData: failed $e")
            }
        }
    }

    // Todo check later
    fun savePlannedOWData(
        plannedOWsPharmaResponse: PlannedOWsPharmaResponse,
        withTrack: BaseDataActivity? = null
    ) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.savePlannedOWData(plannedOWsPharmaResponse)
                if (withTrack != null) {
                    withTrack.plannedOWDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.plannedOWDataTrack.value =
                        CachingDataTackStatus.DATA_GET_CACHED_WITH_ERROR
                }
                Log.d("CacheViewModel", "savePlannedOWData: failed $e")
            }
        }
    }

    fun saveOfflineLog(offlineLog: OfflineLog) {
        CoroutineManager.getScope().launch {
            try {
                if (offlineLog.userId == 0L)
                    offlineLog.userId = dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await().toLong()
                offlineDataRepo.saveOfflineLog(offlineLog)
            } catch (e: Exception) {
                Log.d("CacheViewModel", "saveOfflineLog: failed $e")
            }
        }
    }

    fun saveOfflineLoc(offlineLoc: OfflineLoc) {
        CoroutineManager.getScope().launch {
            try {
//                if (offlineLoc.userId == 0L)
//                    offlineLoc.userId = dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await().toLong()
                offlineDataRepo.saveOfflineLoc(offlineLoc)
                Log.d("CacheViewModel", "saveOfflineLoc: success (${offlineLoc.ll}, ${offlineLoc.lg})")
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

    // Todo check later
    fun clearPlannedOWData(withTrack: BaseDataActivity? = null) {
        CoroutineManager.getScope().launch {
            try {
                offlineDataRepo.clearPlannedOWData()
                if (withTrack != null) {
                    withTrack.plannedOWDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_SUCCESSFULLY
                }
            } catch (e: Exception) {
                if (withTrack != null) {
                    withTrack.plannedOWDataTrack.value =
                        CachingDataTackStatus.CACHE_DATA_TRUNCATED_WITH_ERROR
                }
                Log.d("CacheViewModel", "clearPlannedOWData: failed $e")
            }
        }
    }
}