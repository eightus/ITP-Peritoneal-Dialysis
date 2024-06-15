// Theme.kt

package com.itp.pdbuddy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkOnPrimary,
    secondary = LightBlue,
    tertiary = HeartBlue,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White ,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue,
    secondary = LightBlue,
    tertiary = HeartBlue,
    background = LightPastelBlue,
    surface = LightGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = DarkBlue,
    onSurface = DarkBlue,
)

data class CustomColors(val strongTextColor: Color)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        strongTextColor = StrongTextColor
    )
}

@Composable
fun PDBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val customColors = CustomColors(
        strongTextColor = StrongTextColor
    )

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}

object PDBuddyTheme {
    val customColors: CustomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCustomColors.current
}