package com.itgates.ultra.pulpo.cira.ui.composeUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun LoadingDialog(
    visibleFlow: MutableStateFlow<Boolean>,
    title: String
) {
    val openDialog = visibleFlow.collectAsState()
    if (openDialog.value) {
        AlertDialog(
            shape = ITGatesMenuCornerShape,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                visibleFlow.value = false
            },
            title = {
                TextFactory(text = title)
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator()
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

@Composable
fun NoInternetDialog(internetStateFlow: MutableStateFlow<Boolean>) {
    val messageFlow = MutableStateFlow(stringResource(R.string.no_internet_connection_message))
    MessageDialog(internetStateFlow, messageFlow, stringResource(R.string.no_internet_connection))
}

@Composable
fun MessageDialog(
    visibleFlow: MutableStateFlow<Boolean>,
    messageFlow: MutableStateFlow<String>,
    title: String
) {
    val openDialog = visibleFlow.collectAsState()
    val messageText = messageFlow.collectAsState()
    if (openDialog.value) {
        AlertDialog(
            shape = ITGatesMenuCornerShape,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                visibleFlow.value = false
            },
            title = {
                TextFactory(text = title)
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MultiLineTextFactory(text = messageText.value)
                }
            },
            confirmButton = {
                ButtonFactory(
                    text = "  Ok  ",
                    hasContent = true,
                    content = {
                        TextFactory(
                            text = "  Ok  ",
                            modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                            size = 15.sp,
                            color = ITGatesWhiteColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                ) {
                    visibleFlow.value = false
                }
            },
            dismissButton = {
                ButtonFactory(
                    text = "  Cancel  ",
                    hasContent = true,
                    content = {
                        TextFactory(
                            text = "  Cancel  ",
                            modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                            size = 15.sp,
                            color = ITGatesWhiteColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                ) {
                    visibleFlow.value = false
                }
            }
        )
    }
}

@Composable
fun DeviationMessageDialog(
    visibleFlow: MutableStateFlow<Boolean>,
    messageFlow: MutableStateFlow<String>,
    title: String,
    onSaveAction: () -> Unit
) {
    val openDialog = visibleFlow.collectAsState()
    val messageText = messageFlow.collectAsState()
    if (openDialog.value) {
        AlertDialog(
            shape = ITGatesMenuCornerShape,
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onCloseRequest.
                visibleFlow.value = false
            },
            title = {
                TextFactory(text = title)
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MultiLineTextFactory(text = messageText.value)
                }
            },
            confirmButton = {
                ButtonFactory(
                    text = "  Save  ",
                    hasContent = true,
                    content = {
                        TextFactory(
                            text = "  Save  ",
                            modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                            size = 15.sp,
                            color = ITGatesWhiteColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                ) {
                    onSaveAction()
                    visibleFlow.value = false
                }
            },
            dismissButton = {
                ButtonFactory(
                    text = "  Cancel  ",
                    hasContent = true,
                    content = {
                        TextFactory(
                            text = "  Cancel  ",
                            modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                            size = 15.sp,
                            color = ITGatesWhiteColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                ) {
                    visibleFlow.value = false
                }
            }
        )
    }
}

@Composable
fun MySnackBar(
    visibleFlow: MutableStateFlow<Boolean>,
    messageFlow: MutableStateFlow<String>
) {
    val showSnackBar = visibleFlow.collectAsState()
    val messageText = messageFlow.collectAsState()
    if (showSnackBar.value) {
        Snackbar(
            modifier = Modifier
                .height(padding_70)
                .padding(padding_8),
            action = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(ITGatesCircularCornerShape)
                            .clickable { visibleFlow.value = false },
                        painter = painterResource(R.drawable.visibility_on),
                        contentDescription = "Hide Snack Bar Icon",
                        tint = ITGatesPrimaryColor
                    )
                }
            }
        ) { TextFactory(text = messageText.value) }
    }
}