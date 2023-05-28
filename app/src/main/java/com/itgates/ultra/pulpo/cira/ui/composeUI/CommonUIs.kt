package com.itgates.ultra.pulpo.cira.ui.composeUI

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.uiComponents.MyDefaultOutlinedTextField
import com.itgates.ultra.pulpo.cira.uiComponents.MyOutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.MutableLiveData
import com.itgates.ultra.pulpo.cira.utilities.Utilities

@Composable
fun AppBarComposeView(text: String, goToHomeContext: Context? = null) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(padding_65)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(padding_35))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(padding_30)
                .background(brush))
        }
        Box(
            modifier = Modifier
                .height(padding_55)
                .clip(ITGatesAppBarCornerShape)
                .background(ITGatesWhiteColor)
                .padding(horizontal = padding_12, vertical = padding_12)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(padding_4)
            ) {
                AppIcon(padding_48)
                Box(modifier = Modifier.weight(1F)) {
                    TextFactory(text = text)
                }
                if (goToHomeContext != null) {
                    Box(
                        modifier = Modifier
                            .clip(ITGatesCircularCornerShape)
                            .clickable {
                                Utilities.navigateToMainActivity(goToHomeContext)
                            }
                    ) {
                        Icon(
                            modifier = Modifier.size(padding_48),
                            painter = painterResource(R.drawable.polpo5),
                            contentDescription = "Home Icon",
                            tint = ITGatesPrimaryColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppIcon(size: Dp) {
    Box {
        Icon(
            modifier = Modifier.size(size),
            painter = painterResource(R.drawable.polpo5),
            contentDescription = "App Bar Icon",
            tint = ITGatesPrimaryColor
        )
        Icon(
            modifier = Modifier.size(size),
            painter = painterResource(R.drawable.polpo6),
            contentDescription = "App Bar Icon",
            tint = ITGatesGreyColor
        )
    }
}

@Composable
fun SimpleCheckboxComponent(rememberMe: MutableLiveData<Boolean>) {
    val checkedState = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .clip(ITGatesCircularCornerShape)
            .clickable {
                checkedState.value = !checkedState.value
                rememberMe.value = checkedState.value
            }
            .padding(end = padding_20, start = padding_4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState.value,
            modifier = Modifier,
            onCheckedChange = {
                checkedState.value = it
                rememberMe.value = checkedState.value
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = ITGatesWhiteColor,
                checkedColor = ITGatesPrimaryColor,
                uncheckedColor = ITGatesPrimaryColor
            )
        )
        TextFactory(text = "Remember me")
    }
}

@Composable
fun ButtonFactory(
    text: String,
    withPercentage: Float = 0F,
    size : TextUnit = defaultTextSize,
    color: Color = ITGatesPrimaryColor,
    fontWeight: FontWeight = FontWeight.SemiBold,
    hasContent: Boolean = false,
    content: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    Box(modifier = Modifier) {
        Button(
            modifier = (if (withPercentage == 0F) Modifier else Modifier.fillMaxWidth(withPercentage)),
            colors = ButtonDefaults.buttonColors(backgroundColor = ITGatesPrimaryColor),
            elevation =  ButtonDefaults.elevation(
                defaultElevation = 10.dp,
                pressedElevation = 15.dp,
                disabledElevation = 0.dp
            ),
            contentPadding = PaddingValues(),
            shape = ITGatesCircularCornerShape,
            onClick = onClick
        ) {
            if (hasContent) {
                content()
            }
            else {
                TextFactory(
                    text = text,
                    modifier = Modifier.padding(horizontal = padding_8, vertical = padding_16),
                    size = size,
                    color = if (color == ITGatesPrimaryColor) ITGatesWhiteColor else ITGatesPrimaryColor,
                    fontWeight = fontWeight
                )
            }
        }
    }
}

@Composable
fun ButtonFactoryNoContent(
    text: String,
    withPercentage: Float = 0F,
    size : TextUnit = defaultTextSize,
    color: Color = ITGatesPrimaryColor,
    fontWeight: FontWeight = FontWeight.SemiBold,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier) {
        Button(
            modifier = (if (withPercentage == 0F) Modifier else Modifier.fillMaxWidth(withPercentage)),
            colors = ButtonDefaults.buttonColors(backgroundColor = ITGatesPrimaryColor),
            elevation =  ButtonDefaults.elevation(
                defaultElevation = 10.dp,
                pressedElevation = 15.dp,
                disabledElevation = 0.dp
            ),
            contentPadding = PaddingValues(),
            shape = ITGatesCircularCornerShape,
            onClick = onClick
        ) {
            TextFactory(
                text = text,
                modifier = Modifier.padding(horizontal = padding_8, vertical = padding_16),
                size = size,
                color = if (color == ITGatesPrimaryColor) ITGatesWhiteColor else ITGatesPrimaryColor,
                fontWeight = fontWeight
            )
        }
    }
}

@Composable
fun MultiLineTextFactory(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    size : TextUnit = defaultTextSize,
    color: Color = ITGatesPrimaryColor,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Box(modifier = Modifier) {
        Text(
            text = text,
            textAlign = textAlign,
            fontSize = size,
            color = color,
            fontWeight = fontWeight,
            modifier = modifier
        )
    }
}

@Composable
fun TextFactory(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    size : TextUnit = defaultTextSize,
    color: Color = ITGatesPrimaryColor,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Box(modifier = Modifier) {
        Text(
            text = text,
            textAlign = textAlign,
            fontSize = size,
            color = color,
            fontWeight = fontWeight,
            modifier = modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TextFactory(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    size : TextUnit = defaultTextSize,
    color: Color = ITGatesPrimaryColor,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Box(modifier = Modifier) {
        Text(
            text = text,
            textAlign = textAlign,
            fontSize = size,
            color = color,
            fontWeight = fontWeight,
            modifier = modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun WhiteTextFactory(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    size : TextUnit = defaultTextSize,
    color: Color = ITGatesWhiteColor,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Box(modifier = Modifier) {
        Text(
            text = text,
            textAlign = textAlign,
            fontSize = size,
            color = color,
            fontWeight = fontWeight,
            modifier = modifier,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CustomOutlinedTextField(
    myValue: MutableState<String>,
    myHint: String,
    hasError: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onValueChangeExtra: (textValue: String) -> Unit = {}
) {
    MyOutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = ITGatesCircularCornerShape,
        value = myValue.value,
        onValueChange = { text ->
            myValue.value = text
            hasError.value = false

            onValueChangeExtra(text)
        },
        label = {
            Text(
                text = myHint,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ITGatesPrimaryColor,
            cursorColor = ITGatesPrimaryColor,
            textColor = ITGatesPrimaryColor,
            unfocusedBorderColor = ITGatesSecondaryColor,
            unfocusedLabelColor = ITGatesSecondaryColor,
            errorBorderColor = ITGatesErrorColor,
            errorCursorColor = ITGatesErrorColor,
            errorLabelColor = ITGatesErrorColor,
            focusedLabelColor = ITGatesPrimaryColor,
        ),
        isError = hasError.value,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default,
    )
}

@Composable
fun CustomDefaultOutlinedTextField(
    myValue: MutableState<String>,
    myHint: String, hasError: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onValueChangeExtra: (textValue: String) -> Unit = {}
) {
    MyDefaultOutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        shape = ITGatesLoginCornerShape,
        value = myValue.value,
        onValueChange = { text ->
            myValue.value = text
            hasError.value = false

            onValueChangeExtra(text)
        },
        label = {
            Text(
                text = myHint,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = ITGatesPrimaryColor,
            cursorColor = ITGatesPrimaryColor,
            textColor = ITGatesPrimaryColor,
            unfocusedBorderColor = ITGatesSecondaryColor,
            unfocusedLabelColor = ITGatesSecondaryColor,
            errorBorderColor = ITGatesErrorColor,
            errorCursorColor = ITGatesErrorColor,
            errorLabelColor = ITGatesErrorColor,
            focusedLabelColor = ITGatesPrimaryColor,
        ),
        isError = hasError.value,
        maxLines = 1,
        keyboardOptions = KeyboardOptions.Default,
    )
}




/** Text("Hello Compose ".repeat(50), maxLines = 2, overflow = TextOverflow.Ellipsis) */