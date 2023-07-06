package com.itgates.ultra.pulpo.cira.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val ITGatesDarkPrimaryColor = Color(0xFF4A3D9E)
val ITGatesPrimaryColor = Color(0xFF6A5AD3)
val ITGatesSecondaryColor = Color(0xFFA095E1)
val ITGatesErrorColor = Color(0xFFFF0000)
val ITGatesWhiteColor = Color(0xFFFFFFFF)
val ITGatesOrangeColor = Color(0xFFEC7E2B)
val ITGatesLightOrangeColor = Color(0xA6EC7E2B)
val ITGatesGreyColor = Color(0x749B9B9B)
val ITGatesGreenColor = Color(0xFF089E0A)
val ITGatesIconGreyColor = Color(0xCC404040)
val ITGatesIconGreenColor = Color(0xFF00FF22)
val ITGatesTransparentColor = Color.Transparent

val Purple200 = ITGatesSecondaryColor
val Purple500 = ITGatesPrimaryColor
val Purple700 = Color(0xFF03A9F4)
val Teal200 = ITGatesSecondaryColor


val brush = Brush.verticalGradient(
    listOf(
        ITGatesPrimaryColor,
        ITGatesSecondaryColor,
        ITGatesWhiteColor,
    )
)
val isDoneBrush = Brush.verticalGradient(
    listOf(
        ITGatesSecondaryColor,
        ITGatesWhiteColor,
        ITGatesWhiteColor,
        ITGatesWhiteColor,
        ITGatesWhiteColor,
        ITGatesSecondaryColor,
    )
)
val isDoneOfficeWorkBrush = Brush.verticalGradient(
    listOf(
        ITGatesIconGreyColor,
        ITGatesWhiteColor,
        ITGatesWhiteColor,
        ITGatesWhiteColor,
        ITGatesWhiteColor,
        ITGatesIconGreyColor,
    )
)