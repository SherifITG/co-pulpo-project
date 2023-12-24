package com.itgates.co.pulpo.ultra.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.itgates.co.pulpo.ultra.ui.theme.*
import com.itgates.co.pulpo.ultra.uiComponents.MyOutlinedTextField

class sherif : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Greeting()
        }

    }
}

@Composable
fun Greeting() {
    val expanded = remember { mutableStateOf(false) }
    val list = listOf("0", "sh", "as", "al")

    val searchValue = remember { mutableStateOf("") }

    Column {
        Button(onClick = { expanded.value = true
        }) {
            Text(text = "Test Me")
        }
        MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = ITGatesMenuCornerShape)) {
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                modifier = Modifier
                    .fillMaxWidth(0.7F),
            ) {
                list.forEachIndexed { index, item ->
                    if (index == 0) {
                        val requester = FocusRequester()
                        CustomOutlinedTextField(
                            myValue = searchValue,
                            myHint = "Search \uD83D\uDD0E",
                            hasError = remember { mutableStateOf(false) },
                            modifier = Modifier.focusRequester(requester)
                        )
                        LaunchedEffect(Unit) {
                            requester.requestFocus()
                        }
                    }
                    DropdownMenuItem(
                        modifier = Modifier,
                        onClick = {}
                    ) {
                        Text(text = item)
                    }
                }
            }
        }
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