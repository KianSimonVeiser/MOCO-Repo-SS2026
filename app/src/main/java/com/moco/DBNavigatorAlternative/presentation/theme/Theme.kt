package com.moco.DBNavigatorAlternative.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    error = PastelDanger
)

private val LightColorScheme = lightColorScheme(
    primary = DB_Red_Soft,                // Sanftes Rot für DB-Branding
    onPrimary = Color.White,
    primaryContainer = PastelViolet,      // Sanftes Violett für Header
    onPrimaryContainer = DeepGray,
    
    secondary = PurpleGrey40,
    secondaryContainer = PastelYellow,    // Sanftes Gelb
    onSecondaryContainer = DeepGray,
    
    tertiary = PastelGreen,               // Sanftes Grün
    onTertiary = DeepGray,
    
    error = PastelDanger,                 // Sanftes Rot für Warnungen
    onError = DeepGray,
    
    background = Color.White,
    surface = SoftGray,                   // Sehr helles Grau für Karten
    surfaceVariant = PastelRed,           // Zartes Rot für Aktions-Buttons
    onSurfaceVariant = DeepGray
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
