package com.itgates.co.pulpo.ultra.ui.composeUI

import android.annotation.SuppressLint
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.network.config.NetworkConfiguration
import com.itgates.co.pulpo.ultra.roomDataBase.entity.e_detailing.Presentation
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.GIVEAWAY
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.PRODUCT
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.MANAGER
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.FEEDBACK
import com.itgates.co.pulpo.ultra.ui.activities.ActualActivity
import com.itgates.co.pulpo.ultra.ui.activities.DetailingActivity
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.ui.utils.ActualCurrentValues
import com.itgates.co.pulpo.ultra.ui.utils.ProductModule
import com.itgates.co.pulpo.ultra.utilities.GlobalFormats
import com.itgates.co.pulpo.ultra.utilities.PassedValues
import com.itgates.co.pulpo.ultra.utilities.Utilities
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductsScreen(activity: ActualActivity) {

    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    println("-------------------------------------------------- >> ${isDataChangedToRefresh.value}")

    if (activity.currentValues.isDivisionSelected() && !activity.currentValues.isProductListEmpty()) {
        Scaffold(
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (!activity.currentValues.isAllProductListIsPicked()) {
                    FloatingActionButton(onClick = { /* TODO -> handled below */ }) {
                        ButtonFactory(text = "Add Product") {
                            if (activity.currentValues.isAddingProductIsAccepted()) {
                                activity.currentValues.productsModuleList.add(ProductModule())
                                isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                            }
                            else {
                                Utilities.createCustomToast(activity.applicationContext, "Fill the last product record first")
                            }
                        }
                    }
                }
            },
            content = {
                Box {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(padding_8)
                    ) {
                        itemsIndexed(activity.currentValues.productsModuleList) { index, item ->
                            ProductsLazyColumnItem(activity, isDataChangedToRefresh, item, index)
                        }
                    }
                }
            }
        )
    }
    else if (!activity.currentValues.isDivisionSelected()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextFactory(text = "Select Division first")
        }
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TextFactory(text = "There is no Products for this Division")
        }
    }
}

@Composable
fun ProductsLazyColumnItem(
    activity: ActualActivity,
    isDataChangedToRefresh: MutableState<Boolean>,
    productModule: ProductModule,
    index: Int
) {
    val productData = activity.currentValues.getProductList()
    val commentData = activity.currentValues.getFeedbackList()
    val quotationPaymentMethodData = ActualCurrentValues.quotationPaymentMethodList

    val productCurrentValue = remember { mutableStateOf(productModule.productCurrentValue) }
    val sampleCurrentValue = remember { mutableStateOf(productModule.sampleCurrentValue) }
    val feedbackCurrentValue = remember { mutableStateOf(productModule.commentCurrentValue) }
    val quotationPaymentMethodCurrentValue = remember { mutableStateOf(productModule.quotationPaymentMethodCurrentValue) }
    productCurrentValue.value = productModule.productCurrentValue
    sampleCurrentValue.value = productModule.sampleCurrentValue
    feedbackCurrentValue.value = productModule.commentCurrentValue
    quotationPaymentMethodCurrentValue.value = productModule.quotationPaymentMethodCurrentValue

    val commentCurrentText = remember { mutableStateOf(productModule.comment) }
    val followUpCurrentText = remember { mutableStateOf(productModule.followUp) }
    val markedFeedbackCurrentText = remember { mutableStateOf(productModule.markedFeedback) }
    val quotationCurrentText = remember { mutableStateOf(productModule.quotation) }
    val isDemoCurrentValue = MutableLiveData<Boolean>()
    val demoDateCurrentValue = remember { mutableStateOf(productModule.demoDate) }

    commentCurrentText.value = productModule.comment
    followUpCurrentText.value = productModule.followUp
    markedFeedbackCurrentText.value = productModule.markedFeedback
    quotationCurrentText.value = productModule.quotation
    isDemoCurrentValue.value = productModule.isDemo
    demoDateCurrentValue.value = productModule.demoDate

    val commentCurrentError = remember { mutableStateOf(productModule.textCommentError) }
    val followUpCurrentError = remember { mutableStateOf(productModule.textFollowUpError) }
    val markedFeedbackCurrentError = remember { mutableStateOf(productModule.textMarkedFeedbackError) }
    val quotationCurrentError = remember { mutableStateOf(productModule.textQuotationError) }

    commentCurrentError.value = productModule.textCommentError
    followUpCurrentError.value = productModule.textFollowUpError
    markedFeedbackCurrentError.value = productModule.textMarkedFeedbackError
    quotationCurrentError.value = productModule.textQuotationError

    val modifier = if (index == 0) {
        Modifier
            .padding(horizontal = padding_16)
            .padding(top = padding_8)
    }
    else {
        Modifier.padding(horizontal = padding_16)
    }

    Card(
        shape = ITGatesCardCornerShape,
        modifier = modifier,
        elevation = padding_16
    ) {
        Box(modifier = Modifier.padding(horizontal = padding_16, vertical = padding_12)) {
            Column(verticalArrangement = Arrangement.spacedBy(padding_8)) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(padding_4)
                ) {
                    Box(modifier = Modifier.weight(5F)) {
                        SelectableDropDownMenu(
                            activity,
                            productData,
                            productCurrentValue,
                            isDataChangedToRefresh,
                            productModule.productErrorValue,
                            index
                        )
                    }

                    if (NetworkConfiguration.configuration.name != "helal") {
                        if (productModule.isProductSelected()) {
                            Box(modifier = Modifier.weight(2F)) {
                                SelectableDropDownMenu(
                                    activity,
                                    ActualCurrentValues.productSamplesList,
                                    sampleCurrentValue,
                                    isDataChangedToRefresh,
                                    false,
                                    index
                                )
                            }
                        }
                        else {
                            Box(modifier = Modifier.weight(2F)) {
                                DisabledDropDownMenu(productModule.sampleCurrentValue.embedded.name)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(padding_40)
                            .clip(ITGatesCircularCornerShape)
                            .background(ITGatesGreyColor)
                            .clickable {
                                activity.currentValues.productsModuleList.removeAt(index)
                                isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(padding_22),
                            painter = painterResource(R.drawable.trash_icon),
                            contentDescription = "Icon",
                            tint = ITGatesPrimaryColor
                        )
                    }
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(padding_4)
                ) {
                    Box(modifier = Modifier.weight(1F)) {
                        if (productModule.isProductSelected()) {
                            SelectableDropDownMenu(
                                activity,
                                commentData,
                                feedbackCurrentValue,
                                isDataChangedToRefresh,
                                productModule.commentErrorValue,
                                index
                            )
                        }
                        else {
                            DisabledDropDownMenu(productModule.commentCurrentValue.embedded.name)
                        }
                    }

                    Box(modifier = Modifier.weight(1F)) {
                        CustomOutlinedTextField(commentCurrentText, "comment", commentCurrentError) {
                            activity.currentValues.notifyTextChanges(it, "comment", index)
                            activity.currentValues.productsModuleList[index].textCommentError = false
                        }
                    }
                }
                if (NetworkConfiguration.configuration.name != "helal") {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(padding_4)
                    ) {
                        Box(modifier = Modifier.weight(1F)) {
                            CustomOutlinedTextField(
                                markedFeedbackCurrentText,
                                "marked feedback",
                                markedFeedbackCurrentError
                            ) {
                                activity.currentValues.notifyTextChanges(it, "marked feedback", index)
                                activity.currentValues.productsModuleList[index].textMarkedFeedbackError = false
                            }
                        }
                        Box(modifier = Modifier.weight(1F)) {
                            CustomOutlinedTextField(followUpCurrentText, "follow up", followUpCurrentError) {
                                activity.currentValues.notifyTextChanges(it, "follow up", index)
                                activity.currentValues.productsModuleList[index].textFollowUpError = false
                            }
                        }
                    }
                }

                // this ui only for El-Helal company
                if (NetworkConfiguration.configuration.name == "helal") {
                    val isDemoDateAppear = remember { mutableStateOf(false) }
                    isDemoCurrentValue.observe(activity) { isDemoDateAppear.value = it }
                    ProductSpacerLine(text = "Quotation")
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(padding_4)
                    ) {
                        Box(modifier = Modifier.weight(1F)) {
                            CustomOutlinedTextField(quotationCurrentText, "quotation", quotationCurrentError) {
                                activity.currentValues.notifyTextChanges(it, "quotation", index)
                                activity.currentValues.productsModuleList[index].textQuotationError = false
                                if (it.isEmpty() || it.length == 1)
                                    isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                            }
                        }
                        Box(modifier = Modifier.weight(1F)) {
                            if (productModule.quotation.isNotEmpty()) {
                                SelectableDropDownMenu(
                                    activity,
                                    quotationPaymentMethodData,
                                    quotationPaymentMethodCurrentValue,
                                    isDataChangedToRefresh,
                                    productModule.quotationPaymentMethodErrorValue,
                                    index
                                )
                            }
                            else {
                                DisabledDropDownMenu(productModule.quotationPaymentMethodCurrentValue.embedded.name)
                            }
                        }
                    }
                    AnimatedVisibility(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = SpringSpec(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                        visible = productModule.quotation.isNotEmpty()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CustomOutlinedTextField(
                                    markedFeedbackCurrentText,
                                    "PQ Adjustments",
                                    markedFeedbackCurrentError
                                ) {
                                    activity.currentValues.notifyTextChanges(it, "marked feedback", index)
                                    activity.currentValues.productsModuleList[index].textMarkedFeedbackError = false
                                }
                            }
                        }
                    }
                    ProductSpacerLine(text = "Demo")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(modifier = Modifier) {
                            SimpleCheckboxComponent(isDemoCurrentValue, "Demo request") {
                                activity.currentValues.notifyBooleanChanges(it, index)
                            }
                        }
                    }
                    // demo date
                    AnimatedVisibility(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = SpringSpec(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ),
                        visible = isDemoDateAppear.value
                    ) {
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(padding_4)
                        ) {
                            Box(modifier = Modifier.weight(1F)) {
                                TextFactory(
                                    text = "Demo Date: ",
                                    color = ITGatesPrimaryColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = padding_12)
                                )
                            }
                            Box(modifier = Modifier.weight(1F)) {
                                DemoDateComposeView(
                                    demoDateCurrentValue,
                                    R.drawable.calendar_icon,
                                    productModule
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .animateContentSize(
                            animationSpec = SpringSpec(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                    visible = productModule.isProductSelected()
//                    visible = false
                ) {
                    val presentationsList = ArrayList<Presentation>()
                    activity.currentValues.presentationList.forEach {
                        if (it.productId == productModule.productCurrentValue.id) {
                            presentationsList.add(it)
                        }
                    }
                    LazyRow {
                        items(presentationsList) {
                            ButtonFactory(text = "   ${it.embedded.name}   ") {
                                PassedValues.detailingActivity_presentationId = it.id
                                activity.startActivity(Intent(activity.applicationContext, DetailingActivity::class.java))
                            }
                        }
                    }
                }
            }
        }
    }
}

fun isObjectSelectedBefore(activity: ActualActivity, idAndNameEntity: IdAndNameEntity): Boolean {
    var isObjectSelected = false
    when(idAndNameEntity.tableId) {
        GIVEAWAY -> {
            activity.currentValues.giveawaysModuleList.forEach {
                if (it.giveawayCurrentValue.id == idAndNameEntity.id) {
                    isObjectSelected = true
                    return@forEach
                }
            }
        }
        PRODUCT -> {
            activity.currentValues.productsModuleList.forEach {
                if (it.productCurrentValue.id == idAndNameEntity.id) {
                    isObjectSelected = true
                    return@forEach
                }
            }
        }
        MANAGER -> {
            activity.currentValues.managersModuleList.forEach {
                if (it.managerCurrentValue.id == idAndNameEntity.id) {
                    isObjectSelected = true
                    return@forEach
                }
            }
        }
        else -> {}
    }
    return isObjectSelected
}

@Composable
fun DemoDateComposeView(currentValue: MutableState<String>, @DrawableRes resId: Int, productModule: ProductModule) {
    var pickedDemoDate by remember { mutableStateOf(LocalDate.now()) }
    val demoDateDialogState = rememberMaterialDialogState()

    Box(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .border(padding_2, ITGatesPrimaryColor, ITGatesCircularCornerShape)
            .clickable { demoDateDialogState.show() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(horizontal = padding_12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_2)
            ) {
                Icon(
                    modifier = Modifier.size(padding_24),
                    painter = painterResource(resId),
                    contentDescription = "Dropdown Menu Icon",
                    tint = ITGatesPrimaryColor
                )
                Box(modifier = Modifier.weight(1F)) { SelectableDropDownMenuItem(currentValue.value) }
            }

            MaterialDialog(
                dialogState = demoDateDialogState,
                shape = ITGatesMenuCornerShape,
                buttons = {
                    positiveButton(text = "Ok") {}
                    negativeButton(text = "Cancel")
                }
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ITGatesPrimaryColor)
                            .padding(top = padding_30),
                        contentAlignment = Alignment.Center
                    ) {
                        WhiteTextFactory(text = "Select Date", size = 18.sp)
                    }
                    datepicker(
                        initialDate = pickedDemoDate,
                        title = "",
                        allowedDateValidator = {
                            !it.isBefore(LocalDate.now())
                        },
                        colors = DatePickerDefaults.colors(
                            calendarHeaderTextColor = ITGatesPrimaryColor,
                            dateActiveBackgroundColor = ITGatesPrimaryColor,
                            dateActiveTextColor = ITGatesWhiteColor,
                            dateInactiveBackgroundColor = ITGatesGreyColor,
                            dateInactiveTextColor = ITGatesPrimaryColor,
                            headerBackgroundColor = ITGatesPrimaryColor,
                            headerTextColor = ITGatesWhiteColor
                        )
                    ) {
                        pickedDemoDate = it
                        currentValue.value = DateTimeFormatter.ofPattern(GlobalFormats.dashDateFormat).format(pickedDemoDate)
                        productModule.demoDate = currentValue.value
                    }
                }
            }
        }
    }
}

@Composable
fun ProductSpacerLine(text: String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding_4)
    ) {
        Spacer(modifier = Modifier
            .weight(1F)
            .height(2.dp)
            .background(ITGatesPrimaryColor))
        TextFactory(text = text)
        Spacer(modifier = Modifier
            .weight(1F)
            .height(2.dp)
            .background(ITGatesPrimaryColor))
    }

}