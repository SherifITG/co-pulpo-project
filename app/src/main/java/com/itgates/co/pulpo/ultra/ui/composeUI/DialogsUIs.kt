package com.itgates.co.pulpo.ultra.ui.composeUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.ui.utils.BaseDataActivity
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
                TextFactory(text = title, size = textSize_17)
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
fun CustomLoadingDialog(
    openDialogFlow: MutableStateFlow<Boolean>,
    isLoadingFlow: MutableStateFlow<Boolean>,
    titleFlow: MutableStateFlow<String>,
    activity: BaseDataActivity,
    onOkClickAction: () -> Unit = {}
) {
    val openDialog = openDialogFlow.collectAsState()
    val isLoading = isLoadingFlow.collectAsState()
    val title = titleFlow.collectAsState()
    if (openDialog.value) {
        if (isLoading.value) {
            AlertDialog(
                shape = ITGatesMenuCornerShape,
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    // visibleFlow.value = false // disabled dismiss
                },
                title = {
                    TextFactory(text = title.value)
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
        else {
            AlertDialog(
                shape = ITGatesMenuCornerShape,
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    // visibleFlow.value = false // disabled dismiss
                },
                title = {
                    TextFactory(text = title.value, size = 18.sp, fontWeight = FontWeight.Bold)
                },
                text = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            activity.loadingStatusList.forEachIndexed { index, item ->
                                val bullet = "⦿"
//                                when(index) {
//                                    0 -> "➊"; 1 -> "➋"; 2 -> "➌"; 3 -> "➍"; 4 -> "➎"
//                                    5 -> "➏"; 6 -> "➐"; 7 -> "➑"; 8 -> "➒"
//                                    else -> "⦿"
//                                }
                                MultiLineTextFactory(text = "$bullet $item")
                            }
                        }
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
                        activity.openLoadingStateFlow.value = false
                        activity.loadingStatusList.clear()
                        onOkClickAction()
                    }
                },
                dismissButton = {}
            )
        }
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
                TextFactory(text = title, size = textSize_17)
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
fun DeviationOrInfoMessageDialog(
    visibleFlow: MutableStateFlow<Boolean>,
    messageFlow: MutableStateFlow<String>,
    title: String,
    isInfo: Boolean,
    onSaveAction: () -> Unit,
    onViewMapAction: () -> Unit
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
                TextFactory(text = title, size = textSize_17)
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    MultiLineTextFactory(text = messageText.value, textAlign = TextAlign.Start)
                }
            },
            confirmButton = {
                ButtonFactory(
                    text = if (isInfo) "  Ok  " else "  Save  ",
                    hasContent = true,
                    content = {
                        TextFactory(
                            text = if (isInfo) "  Ok  " else "  Save  ",
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
                when {
                    isInfo -> {}
                    else -> {
                        ButtonFactory(
                            text = "  View Map  ",
                            hasContent = true,
                            content = {
                                TextFactory(
                                    text = "  View Map  ",
                                    modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                                    size = 15.sp,
                                    color = ITGatesWhiteColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        ) {
                            onViewMapAction()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DeviationMessageWithoutSaveDialog(
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
            }
        )
    }
}

@Composable
fun TableRowContentDialog(
    visibleFlow: MutableStateFlow<Boolean>,
    messageFlow: MutableStateFlow<String>,
    title: String,
    showMoreInfo: () -> Unit = {},
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
                TextFactory(text = title, size = textSize_17)
            },
            text = {
                val messageContent = if (messageText.value.endsWith(" ...END_OF_ACTUAL_VISIT..."))
                    messageText.value.removeSuffix(" ...END_OF_ACTUAL_VISIT...")
                else if (messageText.value.endsWith(" ...END_OF_OFFLINE_LOG..."))
                    messageText.value.removeSuffix(" ...END_OF_OFFLINE_LOG...")
                else if (messageText.value.endsWith(" ...END_OF_OFFLINE_LOC..."))
                    messageText.value.removeSuffix(" ...END_OF_OFFLINE_LOC...")
                else if (messageText.value.endsWith(" ...END_OF_OFFLINE_LOC..."))
                    messageText.value.removeSuffix(" ...END_OF_OFFLINE_LOC...")
                else if (messageText.value.endsWith(" ...END_OF_VACATION..."))
                    messageText.value.removeSuffix(" ...END_OF_VACATION...")
                else
                    messageText.value

                Box(modifier = Modifier.fillMaxWidth()) {
                    LazyColumn {
                        item {
                            MultiLineTextFactory(text = messageContent, textAlign = TextAlign.Start)
                        }
                    }
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
                if (messageText.value.endsWith(" ...END_OF_ACTUAL_VISIT...")) {
                    ButtonFactory(
                        text = "  Show Lists  ",
                        hasContent = true,
                        content = {
                            TextFactory(
                                text = "  Show Lists  ",
                                modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                                size = 15.sp,
                                color = ITGatesWhiteColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    ) {
                        showMoreInfo()
                    }
                }
                else if (
                    messageText.value.endsWith(" ...END_OF_OFFLINE_LOG...") ||
                    messageText.value.endsWith(" ...END_OF_OFFLINE_LOC...")
                ) {
                    ButtonFactory(
                        text = "  Message Info  ",
                        hasContent = true,
                        content = {
                            TextFactory(
                                text = "  Message Info  ",
                                modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                                size = 15.sp,
                                color = ITGatesWhiteColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    ) {
                        showMoreInfo()
                    }
                }
                else if (
                    messageText.value.endsWith(" ...END_OF_VACATION...")
                ) {
                    ButtonFactory(
                        text = "  Uri List  ",
                        hasContent = true,
                        content = {
                            TextFactory(
                                text = "  Uri List  ",
                                modifier = Modifier.padding(horizontal = padding_4, vertical = padding_4),
                                size = 15.sp,
                                color = ITGatesWhiteColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    ) {
                        showMoreInfo()
                    }
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