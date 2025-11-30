package com.example.floussi.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = VibrantTeal,
    onPrimary = TextOnDark,
    primaryContainer = DeepMidnightBlueLight,
    onPrimaryContainer = TextOnDark,
    secondary = ElectricGreen,
    onSecondary = TextPrimary,
    tertiary = SoftGold,
    onTertiary = TextPrimary,
    background = DeepMidnightBlue,
    onBackground = TextOnDark,
    surface = SurfaceDark,
    onSurface = TextOnDark,
    error = ErrorRed,
    onError = TextOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = DeepMidnightBlue,
    onPrimary = TextOnDark,
    primaryContainer = DeepMidnightBlueLight,
    onPrimaryContainer = TextOnDark,
    secondary = VibrantTeal,
    onSecondary = TextOnDark,
    tertiary = SoftGold,
    onTertiary = TextPrimary,
    background = OffWhite,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    error = ErrorRed,
    onError = TextOnDark
)

@Composable
fun FloussiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val systemUiController = rememberSystemUiController()
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false

            // Set navigation bar color
            window.navigationBarColor = colorScheme.background.toArgb()

            // Apply system UI colors
            systemUiController.setSystemBarsColor(
                color = colorScheme.primary,
                darkIcons = false
            )
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
