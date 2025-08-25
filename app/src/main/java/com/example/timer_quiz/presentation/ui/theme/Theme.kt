package com.example.timer_quiz.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = FlagsColors.OrangePrimary,
    onPrimary = FlagsColors.ButtonText,
    primaryContainer = FlagsColors.OrangeLight,
    onPrimaryContainer = FlagsColors.TextPrimary,
    secondary = FlagsColors.SelectedBlue,
    onSecondary = FlagsColors.ButtonText,
    tertiary = FlagsColors.CorrectGreen,
    onTertiary = FlagsColors.ButtonText,
    background = FlagsColors.BackgroundWhite,
    onBackground = FlagsColors.TextPrimary,
    surface = Color.White,
    onSurface = FlagsColors.TextPrimary,
    surfaceVariant = FlagsColors.BackgroundGray,
    onSurfaceVariant = FlagsColors.TextSecondary,
    error = FlagsColors.WrongRed,
    onError = FlagsColors.ButtonText
)

private val DarkColorScheme = darkColorScheme(
    primary = FlagsColors.OrangePrimary,
    onPrimary = FlagsColors.ButtonText,
    primaryContainer = FlagsColors.OrangeDark,
    onPrimaryContainer = FlagsColors.ButtonText,
    secondary = FlagsColors.SelectedBlue,
    onSecondary = FlagsColors.ButtonText,
    tertiary = FlagsColors.CorrectGreen,
    onTertiary = FlagsColors.ButtonText,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFCCCCCC),
    error = FlagsColors.WrongRed,
    onError = FlagsColors.ButtonText
)

@Composable
fun TimerQuizTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for consistent branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}