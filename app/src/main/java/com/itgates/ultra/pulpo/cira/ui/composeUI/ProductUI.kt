package com.itgates.ultra.pulpo.cira.ui.composeUI

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.e_detailing.Presentation
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.GIVEAWAY
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.PRODUCT
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.MANAGER
import com.itgates.ultra.pulpo.cira.roomDataBase.roomUtils.enums.IdAndNameTablesNamesEnum.COMMENT
import com.itgates.ultra.pulpo.cira.ui.activities.ActualActivity
import com.itgates.ultra.pulpo.cira.ui.activities.DetailingActivity
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.ui.utils.ActualCurrentValues
import com.itgates.ultra.pulpo.cira.ui.utils.ProductModule
import com.itgates.ultra.pulpo.cira.utilities.PassedValues
import com.itgates.ultra.pulpo.cira.utilities.Utilities

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProductsScreen(activity: ActualActivity) {

    val isDataChangedToRefresh = remember { mutableStateOf(false) }
    println("-------------------------------------------------- >> ${isDataChangedToRefresh.value}")

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

@Composable
fun ProductsLazyColumnItem(
    activity: ActualActivity,
    isDataChangedToRefresh: MutableState<Boolean>,
    productModule: ProductModule,
    index: Int
) {
    val productData = activity.currentValues.multipleList.filter { it.tableId == PRODUCT }
    val commentData = activity.currentValues.multipleList.filter { it.tableId == COMMENT }

    val productCurrentValue = remember { mutableStateOf(productModule.productCurrentValue) }
    val sampleCurrentValue = remember { mutableStateOf(productModule.sampleCurrentValue) }
    val feedbackCurrentValue = remember { mutableStateOf(productModule.commentCurrentValue) }
    productCurrentValue.value = productModule.productCurrentValue
    sampleCurrentValue.value = productModule.sampleCurrentValue
    feedbackCurrentValue.value = productModule.commentCurrentValue

    val commentCurrentText = remember { mutableStateOf(productModule.comment) }
    val followUpCurrentText = remember { mutableStateOf(productModule.followUp) }
    val markedFeedbackCurrentText = remember { mutableStateOf(productModule.markedFeedback) }

    commentCurrentText.value = productModule.comment
    followUpCurrentText.value = productModule.followUp
    markedFeedbackCurrentText.value = productModule.markedFeedback

    val commentCurrentError = remember { mutableStateOf(productModule.textCommentError) }
    val followUpCurrentError = remember { mutableStateOf(productModule.textFollowUpError) }
    val markedFeedbackCurrentError = remember { mutableStateOf(productModule.textMarkedFeedbackError) }

    commentCurrentError.value = productModule.textCommentError
    followUpCurrentError.value = productModule.textFollowUpError
    markedFeedbackCurrentError.value = productModule.textMarkedFeedbackError

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
                    if (productModule.isProductSelected()) {
                        Box(modifier = Modifier.weight(1F)) {
                            SelectableDropDownMenu(
                                activity,
                                commentData,
                                feedbackCurrentValue,
                                isDataChangedToRefresh,
                                productModule.commentErrorValue,
                                index
                            )
                        }
                    }
                    else {
                        Box(modifier = Modifier.weight(1F)) {
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
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(padding_4)
                ) {
                    Box(modifier = Modifier.weight(1F)) {
                        CustomOutlinedTextField(markedFeedbackCurrentText, "markable feedback", followUpCurrentError) {
                            activity.currentValues.notifyTextChanges(it, "marked feedback", index)
                            activity.currentValues.productsModuleList[index].textMarkedFeedbackError = false
                        }
                    }
                    Box(modifier = Modifier.weight(1F)) {
                        CustomOutlinedTextField(followUpCurrentText, "follow up", markedFeedbackCurrentError) {
                            activity.currentValues.notifyTextChanges(it, "follow up", index)
                            activity.currentValues.productsModuleList[index].textFollowUpError = false
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