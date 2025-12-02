package com.theperiodpurse.app.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.theperiodpurse.app.R

val Avenir = FontFamily(
    Font(R.font.avenir_medium),
    Font(R.font.avenir_book, FontWeight.Thin),
    Font(R.font.avenir_light, FontWeight.ExtraLight),
    Font(R.font.avenir_heavy, FontWeight.Bold),
    Font(R.font.avenir_black, FontWeight.ExtraBold),
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Avenir,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
)
