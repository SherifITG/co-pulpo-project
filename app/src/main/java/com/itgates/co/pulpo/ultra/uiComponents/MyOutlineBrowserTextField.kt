package com.itgates.co.pulpo.ultra.uiComponents

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.theme.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itgates.co.pulpo.ultra.ui.activities.VacationActivity
import com.itgates.co.pulpo.ultra.ui.composeUI.TextFactory
import com.itgates.co.pulpo.ultra.utilities.PathUtil
import java.io.File

@Composable
fun MyOutlineBrowserTextField(
    activity: VacationActivity, names: ArrayList<Uri>, myHint: String,
    isDataChangedToRefresh: MutableState<Boolean>, hasError: MutableState<Boolean>,
    browseAction: () -> Unit = {}
) {
    MyDefaultOutlinedTextFieldWithDecorationBox (
        modifier = Modifier.fillMaxWidth(),
        shape = ITGatesLoginCornerShape,
        value = "",
        readOnly = true,
        onValueChange = {},
        label = {
            Text(
                text = myHint,
                fontWeight = FontWeight.SemiBold,
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ITGatesPrimaryColor,
            focusedLabelColor = ITGatesPrimaryColor,
            unfocusedBorderColor = ITGatesPrimaryColor,
            unfocusedLabelColor = ITGatesPrimaryColor,
            errorBorderColor = ITGatesErrorColor,
            errorLabelColor = ITGatesErrorColor,
        ),
        isError = hasError.value,
        placeholder =  {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(padding_4)) {
                itemsIndexed(names) { index, item ->
                    AttachmentNameView(activity, index, isDataChangedToRefresh)
                }
            }
        },
        trailingIcon = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(padding_40)
                        .clip(ITGatesCircularCornerShape)
                        .clickable { browseAction() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(padding_22),
                        painter = painterResource(R.drawable.attachment_icon),
                        contentDescription = "Icon",
                        tint = ITGatesPrimaryColor
                    )
                }
                Spacer(modifier = Modifier.size(padding_8))
            }

        }
    )
}

@Composable
fun AttachmentNameView(
    activity: VacationActivity,
    index: Int,
    isDataChangedToRefresh: MutableState<Boolean>
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val maxWidth = screenWidth.div(2.5).minus(16)
    Row(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .background(ITGatesLightSecondaryColor)
            .padding(horizontal = padding_8, vertical = padding_2),
        horizontalArrangement = Arrangement.spacedBy(padding_4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        println("----------------------------------------------------------------------------kkk")
        println(activity.currentValues.attachmentsUris[index].toString())
        val file = File(
            PathUtil.getPath(
                activity.applicationContext, activity.currentValues.attachmentsUris[index]
            )!!
        )
        TextFactory(
            text = file.name,
            size = 14.sp,
            modifier = Modifier.widthIn(min = 0.dp, max = maxWidth.dp)
        )
        Box(
            modifier = Modifier
                .size(padding_16)
                .clip(ITGatesCircularCornerShape)
                .clickable {
                    activity.currentValues.attachmentsUris.removeAt(index)
                    isDataChangedToRefresh.value = !isDataChangedToRefresh.value
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(padding_12),
                painter = painterResource(R.drawable.circle_canceled_icon),
                contentDescription = "Icon",
                tint = ITGatesPrimaryColor
            )
        }
    }
}