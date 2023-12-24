package com.itgates.co.pulpo.ultra.ui.utils

import android.location.Location
import com.itgates.co.pulpo.ultra.CoroutineManager
import com.itgates.co.pulpo.ultra.dataStore.PreferenceKeys
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Account
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Doctor
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.AccountType
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Brick
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Division
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.IdAndNameObj
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.MultiplicityEnum.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.SettingEnum
import com.itgates.co.pulpo.ultra.ui.activities.ActualActivity
import com.itgates.co.pulpo.ultra.ui.activities.actualTabs.ActualBarScreen
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_startLocation
import com.itgates.co.pulpo.ultra.utilities.PassedValues.actualActivity_startDate
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class ActualCurrentValues(private val activity: ActualActivity) {
    private var isManager by Delegates.notNull<Boolean>()
    var userId by Delegates.notNull<Long>()

    companion object {
        // start values
        val divisionStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Division"), -2)
        val brickStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Brick"), -2)
        val accTypeStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Acc Type"), -2)
        val accountStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Account"), -2)
        val doctorStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Doctor"), -2)
        //val noOfDoctorStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Doctors Num"), -2)
        val multiplicityStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Visit Type"), -2)
        val commentStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Feedback"), -2)
        val productStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Product"), -2)
        val giveawayStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Giveaway"), -2)
        val managerStartValue: IdAndNameObj = IdAndNameEntity(0L, UN_SELECTED, EmbeddedEntity("Select Manager"), -2)

        // sample lists
        val productSamplesList: List<IdAndNameEntity> = listOf(
            IdAndNameEntity(0L, PRODUCT_SAMPLE, EmbeddedEntity("0"), -2),
            IdAndNameEntity(1L, PRODUCT_SAMPLE, EmbeddedEntity("1"), -2),
            IdAndNameEntity(2L, PRODUCT_SAMPLE, EmbeddedEntity("2"), -2),
            IdAndNameEntity(3L, PRODUCT_SAMPLE, EmbeddedEntity("3"), -2),
            IdAndNameEntity(4L, PRODUCT_SAMPLE, EmbeddedEntity("4"), -2),
            IdAndNameEntity(5L, PRODUCT_SAMPLE, EmbeddedEntity("5"), -2),
            IdAndNameEntity(6L, PRODUCT_SAMPLE, EmbeddedEntity("6"), -2),
            IdAndNameEntity(7L, PRODUCT_SAMPLE, EmbeddedEntity("7"), -2),
            IdAndNameEntity(8L, PRODUCT_SAMPLE, EmbeddedEntity("8"), -2),
            IdAndNameEntity(9L, PRODUCT_SAMPLE, EmbeddedEntity("9"), -2),
            IdAndNameEntity(10L, PRODUCT_SAMPLE, EmbeddedEntity("10"), -2)
        )
        val giveawaySamplesList: List<IdAndNameEntity> = listOf(
            IdAndNameEntity(1L, GIVEAWAY_SAMPLE, EmbeddedEntity("1"), -2),
            IdAndNameEntity(2L, GIVEAWAY_SAMPLE, EmbeddedEntity("2"), -2),
            IdAndNameEntity(3L, GIVEAWAY_SAMPLE, EmbeddedEntity("3"), -2),
            IdAndNameEntity(4L, GIVEAWAY_SAMPLE, EmbeddedEntity("4"), -2),
            IdAndNameEntity(5L, GIVEAWAY_SAMPLE, EmbeddedEntity("5"), -2),
            IdAndNameEntity(6L, GIVEAWAY_SAMPLE, EmbeddedEntity("6"), -2),
            IdAndNameEntity(7L, GIVEAWAY_SAMPLE, EmbeddedEntity("7"), -2),
            IdAndNameEntity(8L, GIVEAWAY_SAMPLE, EmbeddedEntity("8"), -2),
            IdAndNameEntity(9L, GIVEAWAY_SAMPLE, EmbeddedEntity("9"), -2),
            IdAndNameEntity(10L, GIVEAWAY_SAMPLE, EmbeddedEntity("10"), -2)
        )
        val quotationPaymentMethodList: List<IdAndNameEntity> = listOf(
            IdAndNameEntity(1L, QUOTATION_PAYMENT_METHOD, EmbeddedEntity("Cash"), -2),
            IdAndNameEntity(2L, QUOTATION_PAYMENT_METHOD, EmbeddedEntity("Cheque"), -2),
            IdAndNameEntity(3L, QUOTATION_PAYMENT_METHOD, EmbeddedEntity("Installment"), -2)
        )
        val noOfDoctorList: List<IdAndNameEntity> = List(50) {
            IdAndNameEntity((it + 1).toLong(), NO_OF_DOCTORS, EmbeddedEntity((it + 1).toString()), -2)
        }

        // texts start values
        const val textStartValue = ""
        // boolean start values
        const val booleanStartValue = false
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


    var managersList: List<IdAndNameEntity> = listOf()
    var giveawaysList: List<IdAndNameEntity> = listOf()
    var productsList: List<IdAndNameEntity> = listOf()
    var feedbacksList: List<IdAndNameEntity> = listOf()

    val multiplicityList: List<IdAndNameEntity> = listOf(
        IdAndNameEntity(1L, MULTIPLICITY, EmbeddedEntity(SINGLE_VISIT.text), -2),
        IdAndNameEntity(2L, MULTIPLICITY, EmbeddedEntity(DOUBLE_VISIT.text), -2)
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
        productsModuleList.size == productsList.size
    fun isAllGiveawayListIsPicked(): Boolean =
        giveawaysModuleList.size == giveawaysList.size
    fun isAllManagerListIsPicked(): Boolean =
        managersModuleList.size == managersList.size

    fun getFeedbackList(): List<IdAndNameEntity> = feedbacksList
    fun getManagerList(): List<IdAndNameEntity> = managersList
    fun isManagerListEmpty(): Boolean = managersList.isEmpty()
    fun getProductList(): List<IdAndNameEntity> = productsList
    fun isProductListEmpty(): Boolean = productsList.isEmpty()
    fun getGiveawayList(): List<IdAndNameEntity> = giveawaysList
    fun isGiveawayListEmpty(): Boolean = giveawaysList.isEmpty()

    fun isAllDataReady(): String {
        var routeText = "NoRouteText"
        val toastsText = StringBuilder("")
        if (!isDivisionSelected()) {
            divisionErrorValue = true
            routeText = ActualBarScreen.VisitDetails.route
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


        val membersSetting = isSettingActive(SettingEnum.IS_REQUIRED_MANAGER_MEMBER)
        if (isMultiplicityDouble() && (!isUserManager() || membersSetting)) {
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
        else {
            if (productsModuleList.isNotEmpty() && productsModuleList.last().isProductSelected()) {
                if (!productsModuleList.last().isProductCompleted(settingMap, true)) {
                    if (routeText == "NoRouteText") routeText = ActualBarScreen.Product.route
                }
            }
            else if (productsModuleList.isNotEmpty() && !productsModuleList.last().isProductSelected()) {
                productsModuleList.removeLast()
            }
        }

        return routeText
    }

    private fun isSettingActive(setting: SettingEnum) = 1 == settingMap[setting.text]

    fun notifyChanges(idAndNameObj: IdAndNameObj) {
        when(idAndNameObj) {
            is Division -> {
                divisionCurrentValue = idAndNameObj

                activity.loadBrickData(divisionCurrentValue.id)
                activity.loadIdAndNameEntityData((divisionCurrentValue as Division).lineId)

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
                    (divisionCurrentValue as Division).lineId,
                    divisionCurrentValue.id,
                    brickCurrentValue.id,
                    accTypeCurrentValue.id.toInt()
                )

                accountCurrentValue = accountStartValue
                doctorCurrentValue = doctorStartValue
            }
            is Account -> {
                accountCurrentValue = idAndNameObj

                activity.loadDoctorData(
                    (divisionCurrentValue as Division).lineId,
                    accountCurrentValue.id,
                    accTypeCurrentValue.id.toInt()
                )

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
            FEEDBACK -> {
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
            QUOTATION_PAYMENT_METHOD -> {
                productsModuleList[index].quotationPaymentMethodCurrentValue = idAndNameObj
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
            "quotation" -> {
                productsModuleList[index].quotation = newText
            }
            else -> {}
        }
    }

    fun notifyBooleanChanges(newBoolean: Boolean, index: Int) {
        // there is no if or when checks because no boolean field here except `isDemo`
        productsModuleList[index].isDemo = newBoolean
    }
}

class ProductModule {
    var productCurrentValue: IdAndNameObj = ActualCurrentValues.productStartValue
    var commentCurrentValue: IdAndNameObj = ActualCurrentValues.commentStartValue
    var sampleCurrentValue: IdAndNameObj = ActualCurrentValues.productSamplesList[0]
    var quotationPaymentMethodCurrentValue: IdAndNameObj = ActualCurrentValues.quotationPaymentMethodList[0]
    var comment = ActualCurrentValues.textStartValue
    var followUp = ActualCurrentValues.textStartValue
    var markedFeedback = ActualCurrentValues.textStartValue
    var quotation = ActualCurrentValues.textStartValue
    var isDemo = ActualCurrentValues.booleanStartValue
    var demoDate = GlobalFormats.getDashedDate(Locale.getDefault(), Date())

    var productErrorValue = false
    var commentErrorValue = false
    var quotationPaymentMethodErrorValue = false
    var textCommentError = false
    var textFollowUpError = false
    var textMarkedFeedbackError = false
    var textQuotationError = false

    fun isProductSelected(): Boolean = productCurrentValue.id != 0L
    private fun isCommentSelected(): Boolean = commentCurrentValue.id != 0L

    fun isProductCompleted(settingMap: Map<String, Int>, wantToShowErrors: Boolean = false): Boolean {
        val feedbackValidation = (settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text] == 0 || isCommentSelected()))
                // Todo remove this line or modified it
                || !(settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_FEEDBACK.text))
        val commentValidation = (settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text] == 0 || comment.trim().isNotEmpty()))
                // Todo remove this line or modified it
                || !(settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_COMMENT.text))
        val followUpValidation = (settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text] == 0 || followUp.trim().isNotEmpty()))
                // Todo remove this line or modified it
                || !(settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_FOLLOW_UP.text))
        val markedFeedbackValidation = (settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text)
                && (settingMap[SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text] == 0 || markedFeedback.trim().isNotEmpty()))
                // Todo remove this line or modified it
                || !(settingMap.containsKey(SettingEnum.IS_REQUIRED_PRODUCT_M_FEEDBACK.text))

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