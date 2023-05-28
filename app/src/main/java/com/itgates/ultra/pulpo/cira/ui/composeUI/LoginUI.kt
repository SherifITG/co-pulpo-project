package com.itgates.ultra.pulpo.cira.ui.composeUI

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.MutableLiveData
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.uiComponents.MyDefaultOutlinedTextField
import kotlinx.coroutines.flow.MutableStateFlow
import com.itgates.ultra.pulpo.cira.R
import java.io.File


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LoginPage(
    internetStateFlow: MutableStateFlow<Boolean>,
    loadingStateFlow: MutableStateFlow<Boolean>,
    rememberMe: MutableLiveData<Boolean>,
    loginAction: (username: String, password: String) -> Unit
) {
// ------------------------------------------------------------------------------------------------
//    val usernameValue = remember { mutableStateOf("Test MR") }
//    val passwordValue = remember { mutableStateOf("1234") }
//    val usernameValue = remember { mutableStateOf("Ahmed kamal") }
//    val passwordValue = remember { mutableStateOf("1234") }
//    val usernameValue = remember { mutableStateOf("alaa bahaa") }
//    val passwordValue = remember { mutableStateOf("1234") }
// ------------------------------------------------------------------------------------------------
    val usernameValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val usernameHasError = remember { mutableStateOf(false) }
    val passwordHasError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(padding_16)
    ) {
        CustomDefaultOutlinedTextField(myValue = usernameValue, myHint = "Username", hasError = usernameHasError)
        OutlinedTextFieldPassword(myValue = passwordValue, myHint = "Password", hasError = passwordHasError)
        SimpleCheckboxComponent(rememberMe)
        ButtonFactory(text = "Login", withPercentage = 0.6F) {
            val isUsernameEmpty = usernameValue.value.trim().isEmpty()
            val isPasswordEmpty = passwordValue.value.trim().isEmpty()
            if (isUsernameEmpty && isPasswordEmpty) {
                usernameHasError.value = true
                passwordHasError.value = true
                return@ButtonFactory
            }
            else if (isUsernameEmpty) {
                usernameHasError.value = true
                return@ButtonFactory
            }
            else if (isPasswordEmpty) {
                passwordHasError.value = true
                return@ButtonFactory
            }
            loginAction(usernameValue.value, passwordValue.value)
        }
        NoInternetDialog(internetStateFlow)
        LoadingDialog(loadingStateFlow, "Loading")
    }
}

@Composable
fun PdfViewer(pdfFile: File) {
    val parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
    val pdfRenderer = PdfRenderer(parcelFileDescriptor)
    println("..................................................... ${pdfRenderer.pageCount}")
    val bitmap: Bitmap = pdfRenderer.openPage(2).run {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        close()
        bmp
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )

    pdfRenderer.close()
    parcelFileDescriptor.close()
}

@Composable
fun OutlinedTextFieldPassword(myValue: MutableState<String>, myHint: String, hasError: MutableState<Boolean>) {
    val fieldVisibility = remember { mutableStateOf(false) }
    MyDefaultOutlinedTextField (
        modifier = Modifier.fillMaxWidth(),
        shape = ITGatesLoginCornerShape,
        value = myValue.value,
        onValueChange = { text ->
            myValue.value = text
            hasError.value = false
        },
        label = {
            Text(
                text = myHint,
                fontWeight = FontWeight.SemiBold,
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(padding_40)
                        .clip(ITGatesCircularCornerShape)
                        .clickable { fieldVisibility.value = !fieldVisibility.value },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(padding_22),
                        painter =
                        if (fieldVisibility.value)
                            painterResource(R.drawable.password_hide_icon)
                        else
                            painterResource(R.drawable.password_show_icon),
                        contentDescription = "Visibility Icon",
                        tint = ITGatesPrimaryColor
                    )
                }
                Spacer(modifier = Modifier.size(padding_8))
            }

        },
        visualTransformation = if (fieldVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    )
}
