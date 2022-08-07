package com.sk.shotsapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sk.shotsapp.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

val Kodchasan = FontFamily(
    Font(R.font.regular_400, FontWeight.Normal),
    Font(R.font.bold_700, FontWeight.Bold),
    Font(R.font.light_300, FontWeight.Light),
    Font(R.font.extra_light_275, FontWeight.ExtraLight),
    Font(R.font.medium_500, FontWeight.Medium),
    Font(R.font.semibold_600, FontWeight.SemiBold)
)

val MyTypography = Typography(
    body1 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    h1 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 96.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 60.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 48.sp,
        letterSpacing = (0).sp
    ),
    h4 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 34.sp,
        letterSpacing = (0.25).sp
    ),
    h5 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 24.sp,
        letterSpacing = (0).sp
    ),
    h6 = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 20.sp,
        letterSpacing = (0.15).sp
    ),
    button = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 14.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontFamily = Kodchasan,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp
    )
)

