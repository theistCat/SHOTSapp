package com.sk.shotsapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

//private val DarkColorPalette = darkColors(
//    primary = Purple200,
//    primaryVariant = Purple700,
//    secondary = Teal200
//)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ifDarkTheme(status: Boolean): Color {
    val color = if (isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color.White
    }
    val color2 = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

    return if (status) {
        color
    } else {
        color2
    }
}

@Composable
fun SHOTScomposeTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
    LightColorPalette
//    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.White
    )
//        systemUiController.setSystemBarsColor(
//            color = Color.Black
//        )
//    } else {
//        systemUiController.setSystemBarsColor(
//            color = Color.White
//        )
//    }

    MaterialTheme(
        colors = colors,
        typography = MyTypography,
        shapes = Shapes,
        content = content
    )
}