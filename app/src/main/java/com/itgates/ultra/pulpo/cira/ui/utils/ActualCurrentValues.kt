package com.itgates.ultra.pulpo.cira.ui.utils

import android.location.Location
import android.os.Bundle
import com.google.gson.Gson
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.EmbeddedEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Account
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.Doctor
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.AccountType
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Brick
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.Division
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.MultiplicityEnum.*
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.ultra.pulpo.cira.ui.activities.ActualActivity
import com.itgates.ultra.pulpo.cira.ui.activities.actualTabs.ActualBarScreen
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_startLocation
import com.itgates.ultra.pulpo.cira.utilities.PassedValues.actualActivity_startDate
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.util.Date
import kotlin.properties.Delegates

class ActualCurrentValues(private val activity: ActualActivity) {
    private var isManager by Delegates.notNull<Boolean>()
    var userId by Delegates.notNull<Long>()

    companion object {
        // start values
        val divisionStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Division"))
        val brickStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Brick"))
        val accTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Acc Type"))
        val accountStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Account"))
        val doctorStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Doctor"))
//        val noOfDoctorStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Doctors Num"))
        val multiplicityStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Visit Type"))
        val commentStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Feedback"))
        val productStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Product"))
        val giveawayStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Giveaway"))
        val managerStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Manager"))

        // sample lists
        val productSamplesList: List<IdAndNameEntity> = listOf(
            IdAndNameEntity(0L, PRODUCT_SAMPLE, EmbeddedEntity("0")),
            IdAndNameEntity(1L, PRODUCT_SAMPLE, EmbeddedEntity("1")),
            IdAndNameEntity(2L, PRODUCT_SAMPLE, EmbeddedEntity("2")),
            IdAndNameEntity(3L, PRODUCT_SAMPLE, EmbeddedEntity("3")),
            IdAndNameEntity(4L, PRODUCT_SAMPLE, EmbeddedEntity("4")),
            IdAndNameEntity(5L, PRODUCT_SAMPLE, EmbeddedEntity("5")),
            IdAndNameEntity(6L, PRODUCT_SAMPLE, EmbeddedEntity("6")),
            IdAndNameEntity(7L, PRODUCT_SAMPLE, EmbeddedEntity("7")),
            IdAndNameEntity(8L, PRODUCT_SAMPLE, EmbeddedEntity("8")),
            IdAndNameEntity(9L, PRODUCT_SAMPLE, EmbeddedEntity("9")),
            IdAndNameEntity(10L, PRODUCT_SAMPLE, EmbeddedEntity("10"))
        )
        val giveawaySamplesList: List<IdAndNameEntity> = listOf(
            IdAndNameEntity(1L, GIVEAWAY_SAMPLE, EmbeddedEntity("1")),
            IdAndNameEntity(2L, GIVEAWAY_SAMPLE, EmbeddedEntity("2")),
            IdAndNameEntity(3L, GIVEAWAY_SAMPLE, EmbeddedEntity("3")),
            IdAndNameEntity(4L, GIVEAWAY_SAMPLE, EmbeddedEntity("4")),
            IdAndNameEntity(5L, GIVEAWAY_SAMPLE, EmbeddedEntity("5")),
            IdAndNameEntity(6L, GIVEAWAY_SAMPLE, EmbeddedEntity("6")),
            IdAndNameEntity(7L, GIVEAWAY_SAMPLE, EmbeddedEntity("7")),
            IdAndNameEntity(8L, GIVEAWAY_SAMPLE, EmbeddedEntity("8")),
            IdAndNameEntity(9L, GIVEAWAY_SAMPLE, EmbeddedEntity("9")),
            IdAndNameEntity(10L, GIVEAWAY_SAMPLE, EmbeddedEntity("10")),
        )
        val noOfDoctorList: List<IdAndNameEntity> = List(50) {
            IdAndNameEntity((it + 1).toLong(), NO_OF_DOCTORS, EmbeddedEntity((it + 1).toString()))
        }

        // texts start values
        const val textStartValue = ""
    }

    init {
        CoroutineManager.getScope().launch {
            isManager = activity.getDataStoreService().getDataObjAsync(PreferenceKeys.IS_MANAGER)
                .await().toInt() == 1
            userId = activity.getDataStoreService().getDataObjAsync(PreferenceKeys.USER_ID)
                .await().toLong()
        }
    }

    var settingMap: Map<String, Int> = mapOf()
    var divisionsList: List<Division> = listOf()
    var bricksList: List<Brick> = listOf()
    var accountTypesList: List<AccountType> = listOf()
    var accountsList: List<Account> = listOf()
    var doctorsList: List<Doctor> = listOf()
    var multipleList: List<IdAndNameEntity> = listOf()
    var presentationList: List<Presentation> = listOf()

    val multiplicityList: List<IdAndNameEntity> = listOf(
        IdAndNameEntity(1L, MULTIPLICITY, EmbeddedEntity(SINGLE_VISIT.text)),
        IdAndNameEntity(2L, MULTIPLICITY, EmbeddedEntity(DOUBLE_VISIT.text))
    )

    // current values
    var divisionCurrentValue: IdAndNameObj = divisionStartValue
    var brickCurrentValue: IdAndNameObj = brickStartValue
    var accTypeCurrentValue: IdAndNameObj = accTypeStartValue
    var accountCurrentValue: IdAndNameObj = accountStartValue
    var doctorCurrentValue: IdAndNameObj = doctorStartValue
    var noOfDoctorCurrentValue: IdAndNameObj = noOfDoctorList[0]
    var multiplicityCurrentValue: IdAndNameObj = multiplicityStartValue

    // error values
    var divisionErrorValue = false
    var brickErrorValue = false
    var accTypeErrorValue = false
    var accountErrorValue = false
    var doctorErrorValue = false
    var multiplicityErrorValue = false

    // data lists
    val productsModuleList = ArrayList<ProductModule>()
    val giveawaysModuleList = ArrayList<GiveawayModule>()
    val managersModuleList = ArrayList<ManagerModule>()

    // dates and times and locations
    var endDate: Date? = null
    var startDate: Date? = actualActivity_startDate
    var endLocation: Location? = null
    var startLocation: Location? = actualActivity_startLocation

    fun isDivisionSelected(): Boolean = divisionCurrentValue !is IdAndNameEntity
    fun isBrickSelected(): Boolean = brickCurrentValue !is IdAndNameEntity
    fun isAccTypeSelected(): Boolean = accTypeCurrentValue !is IdAndNameEntity
    fun isAccountSelected(): Boolean = accountCurrentValue !is IdAndNameEntity
    fun isDoctorSelected(): Boolean = doctorCurrentValue !is IdAndNameEntity
    fun isNoOfDoctorsSelected(): Boolean = isAccountSelected()
    fun isMultiplicitySelected(): Boolean = (multiplicityCurrentValue as IdAndNameEntity).tableId == MULTIPLICITY
    fun isMultiplicitySingle(): Boolean = (multiplicityCurrentValue as IdAndNameEntity).embedded.name == SINGLE_VISIT.text
    fun isMultiplicityDouble(): Boolean = (multiplicityCurrentValue as IdAndNameEntity).embedded.name == DOUBLE_VISIT.text
    fun isMainPageAnyItemSelected(): Boolean = isDivisionSelected() || isBrickSelected() ||
            isAccTypeSelected() ||  isAccountSelected() || isDoctorSelected() ||  isMultiplicitySelected()
    fun isUserManager(): Boolean = isManager

    fun isBrickVisible(): Boolean = isDivisionSelected()
    fun isAccTypeVisible(): Boolean = isDivisionSelected() && isBrickSelected()
    fun isAccountVisible(): Boolean = isDivisionSelected() && isBrickSelected() && isAccTypeSelected()
    fun isDoctorVisible(): Boolean = isDivisionSelected() && isBrickSelected() && isAccTypeSelected() && isAccountSelected()
    fun isNoOfDoctorVisible(): Boolean = isDivisionSelected() && isBrickSelected() &&
            isAccTypeSelected() && isAccountSelected()// && isDoctorSelected()

    fun isAddingProductIsAccepted(): Boolean = productsModuleList.isEmpty() || productsModuleList.last().isProductCompleted(this.settingMap)
    fun isAddingGiveawayIsAccepted(): Boolean = giveawaysModuleList.isEmpty() || giveawaysModuleList.last().isGiveawaySelected()
    fun isAddingManagerIsAccepted(): Boolean = managersModuleList.isEmpty() || managersModuleList.last().isManagerSelected()
    fun isAllProductListIsPicked(): Boolean =
        productsModuleList.size == multipleList.filter { it.tableId == PRODUCT }.size
    fun isAllGiveawayListIsPicked(): Boolean =
        giveawaysModuleList.size == multipleList.filter { it.tableId == GIVEAWAY }.size
    fun isAllManagerListIsPicked(): Boolean =
        managersModuleList.size == multipleList.filter {
            it.tableId == MANAGER
        }.filter {
            it.embedded.name != "System Administrator"
        }.size

    fun isAllDataReady(): String {
        var routeText = "NoRouteText"
        var toastsText : StringBuilder = StringBuilder("")
        if (!isDivisionSelected()) {
            divisionErrorValue = true
            if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
        }
        if (!isBrickSelected()) {
            brickErrorValue = true
            if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
        }
        if (!isAccTypeSelected()) {
            accTypeErrorValue = true
            if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
        }
        if (!isAccountSelected()) {
            accountErrorValue = true
            if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
        }
        if (!isDoctorSelected()) {
            doctorErrorValue = true
            if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
        }
        if (!isMultiplicitySelected()) {
            multiplicityErrorValue = true
            if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
        }


        if (isMultiplicityDouble() && !isManager) {
            if (managersModuleList.isEmpty()) {
                toastsText.append("you should add 1 manager at least")
                if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
            }
            else if (!managersModuleList.first().isManagerSelected()) {
                managersModuleList.first().managerErrorValue = true
                if (routeText == "NoRouteText") routeText = ActualBarScreen.VisitDetails.route
            }
        }

        val isGiveawaySettingFound = settingMap.containsKey(SettingEnum.NO_OF_GIVEAWAYS.text)
        val noOfGiveaway = settingMap[SettingEnum.NO_OF_GIVEAWAYS.text]
        if (isGiveawaySettingFound && noOfGiveaway!! > giveawaysModuleList.size) {
            toastsText.append("you should add $noOfGiveaway giveaway at least")
            if (routeText == "NoRouteText") routeText = ActualBarScreen.Giveaway.route
        }
        else if (isGiveawaySettingFound && noOfGiveaway!! == giveawaysModuleList.size) {
            if (giveawaysModuleList.isEmpty()) {
                toastsText.append("you should add $noOfGiveaway giveaway at least")
            }
            else if (!giveawaysModuleList.last().isGiveawaySelected()) {
                giveawaysModuleList.last().giveawayErrorValue = true
                if (routeText == "NoRouteText") routeText = ActualBarScreen.Giveaway.route
            }
        }

        val isProductSettingFound = settingMap.containsKey(SettingEnum.NO_OF_PRODUCT.text)
        val noOfProduct = settingMap[SettingEnum.NO_OF_PRODUCT.text]
        if (isProductSettingFound && noOfProduct!! > productsModuleList.size) {
            toastsText.append("you should add $noOfProduct product at least")
            if (routeText == "NoRouteText") routeText = ActualBarScreen.Product.route
        }
        else if (isProductSettingFound && noOfProduct!! == productsModuleList.size) {
            if (productsModuleList.isEmpty()) {
                toastsText.append("you should add $noOfProduct product at least")
            }
            else if (!productsModuleList.last().isProductCompleted(settingMap, true)) {
                if (routeText == "NoRouteText") routeText = ActualBarScreen.Product.route
            }
        }

        return routeText
    }

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is Division -> {
                divisionCurrentValue = idAndNameObj

                activity.loadBrickData(divisionCurrentValue.id)

                brickCurrentValue = brickStartValue
                accTypeCurrentValue = accTypeStartValue
                accountCurrentValue = accountStartValue
                doctorCurrentValue = doctorStartValue
            }
            is Brick -> {
                brickCurrentValue = idAndNameObj

                activity.loadAccTypeData(divisionCurrentValue.id, brickCurrentValue.id)

                accTypeCurrentValue = accTypeStartValue
                accountCurrentValue = accountStartValue
                doctorCurrentValue = doctorStartValue
            }
            is AccountType -> {
                accTypeCurrentValue = idAndNameObj

                activity.loadAccountData(
                    divisionCurrentValue.id,
                    brickCurrentValue.id,
                    (accTypeCurrentValue as AccountType).table
                )

                accountCurrentValue = accountStartValue
                doctorCurrentValue = doctorStartValue
            }
            is Account -> {
                accountCurrentValue = idAndNameObj

                activity.loadDoctorData(accountCurrentValue.id, (accTypeCurrentValue as AccountType).table)

                doctorCurrentValue = doctorStartValue
                noOfDoctorCurrentValue = noOfDoctorList[0]
            }
            is Doctor -> {
                doctorCurrentValue = idAndNameObj
            }
            is IdAndNameEntity -> {
                if (idAndNameObj.tableId == MULTIPLICITY) {
                    multiplicityCurrentValue = idAndNameObj

                    if (idAndNameObj.embedded.name == SINGLE_VISIT.text) {
                        managersModuleList.clear()
                    }
                }
                else if (idAndNameObj.tableId == NO_OF_DOCTORS) {
                    noOfDoctorCurrentValue = idAndNameObj
                }
            }
        }
    }

    fun notifyListsChanges(idAndNameObj: IdAndNameObj, index: Int) {
        when((idAndNameObj as IdAndNameEntity).tableId) {
            PRODUCT -> {
                productsModuleList[index].productCurrentValue = idAndNameObj
            }
            COMMENT -> {
                productsModuleList[index].commentCurrentValue = idAndNameObj
            }
            PRODUCT_SAMPLE -> {
                productsModuleList[index].sampleCurrentValue = idAndNameObj
            }
            GIVEAWAY -> {
                giveawaysModuleList[index].giveawayCurrentValue = idAndNameObj
            }
            GIVEAWAY_SAMPLE -> {
                giveawaysModuleList[index].sampleCurrentValue = idAndNameObj
            }
            MANAGER -> {
                managersModuleList[index].managerCurrentValue = idAndNameObj
            }
            else -> {}
        }
    }

    fun notifyTextChanges(newText: String, hint: String, index: Int) {
        when(hint) {
            "comment" -> {
                productsModuleList[index].comment = newText
            }
            "marked feedback" -> {
                productsModuleList[index].markedFeedback = newText
            }
            "follow up" -> {
                productsModuleList[index].followUp = newText
            }
            else -> {}
        }
    }
}

class ProductModule {
    var productCurrentValue: IdAndNameObj = ActualCurrentValues.productStartValue
    var commentCurrentValue: IdAndNameObj = ActualCurrentValues.commentStartValue
    var sampleCurrentValue: IdAndNameObj = ActualCurrentValues.productSamplesList[0]
    var comment = ActualCurrentValues.textStartValue
    var followUp = ActualCurrentValues.textStartValue
    var markedFeedback = ActualCurrentValues.textStartValue

    var productErrorValue = false
    var commentErrorValue = false
    var textCommentError = false
    var textFollowUpError = false
    var textMarkedFeedbackError = false

    fun isProductSelected(): Boolean = productCurrentValue.id != 0L
    private fun isCommentSelected(): Boolean = commentCurrentValue.id != 0L

    fun isProductCompleted(settingMap: Map<String, Int>, wantToShowErrors: Boolean = false): Boolean {
        val feedbackValidation = settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text] == 0 || isCommentSelected())
        val commentValidation = settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text] == 0 || comment.trim().isNotEmpty())
        val followUpValidation = settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text] == 0 || followUp.trim().isNotEmpty())
        val markedFeedbackValidation = settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text] == 0 || markedFeedback.trim().isNotEmpty())

        if (wantToShowErrors) {
            if (!isProductSelected()) productErrorValue = true
            if (!feedbackValidation) commentErrorValue = true
            if (!commentValidation) textCommentError = true
            if (!followUpValidation) textFollowUpError = true
            if (!markedFeedbackValidation) textMarkedFeedbackError = true
        }

        return isProductSelected() && feedbackValidation && commentValidation && followUpValidation && markedFeedbackValidation
    }
}

class GiveawayModule {
    var giveawayCurrentValue: IdAndNameObj = ActualCurrentValues.giveawayStartValue
    var sampleCurrentValue: IdAndNameObj = ActualCurrentValues.giveawaySamplesList[0]

    var giveawayErrorValue = false
    var sampleErrorValue = false

    fun isGiveawaySelected(): Boolean = giveawayCurrentValue.id != 0L
}

class ManagerModule {
    var managerCurrentValue: IdAndNameObj = ActualCurrentValues.managerStartValue

    var managerErrorValue = false

    fun isManagerSelected(): Boolean = managerCurrentValue.id != 0L
}