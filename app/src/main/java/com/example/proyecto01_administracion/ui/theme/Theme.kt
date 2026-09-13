package com.example.proyecto01_administracion.ui.theme

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
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class TransAndinaColors(
    val statusGreen: Color,
    val statusYellow: Color,
    val statusRed: Color,
    val statusBlue: Color
)

val LocalTransAndinaColors = staticCompositionLocalOf {
    TransAndinaColors(
        statusGreen = StatusGreen,
        statusYellow = StatusYellow,
        statusRed = StatusRed,
        statusBlue = AccentBlue
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = AccentPurple,
    tertiary = AccentBluePurple,
    background = BackgroundBlack,
    surface = CardGray,
    onPrimary = TextWhite,
    onSecondary = TextWhite,
    onTertiary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    surfaceVariant = CardGray,
    onSurfaceVariant = TextGrayLight,
    outline = CardBorderGray
)

// TransAndina primarily uses a dark theme, but we keep the light scheme for compatibility
private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    secondary = AccentPurple,
    tertiary = AccentBluePurple,
    background = BackgroundWhite,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextBlack,
    onSurface = TextBlack,
    surfaceVariant = SurfaceGrayLight,
    onSurfaceVariant = TextGrayDark,
    outline = BorderGrayLight
)

@Composable
fun Proyecto01AdministracionTheme(
    darkTheme: Boolean = true, // Default to dark theme for TransAndina
    // Dynamic color is disabled by default to keep the TransAndina brand identity
    dynamicColor: Boolean = false,
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

    val transAndinaColors = if (darkTheme) {
        TransAndinaColors(
            statusGreen = StatusGreen,
            statusYellow = StatusYellow,
            statusRed = StatusRed,
            statusBlue = AccentBlue
        )
    } else {
        TransAndinaColors(
            statusGreen = LightStatusGreen,
            statusYellow = LightStatusYellow,
            statusRed = LightStatusRed,
            statusBlue = LightStatusBlue
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }

    CompositionLocalProvider(LocalTransAndinaColors provides transAndinaColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
