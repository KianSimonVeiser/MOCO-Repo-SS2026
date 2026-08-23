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
    primary = Color(0xFFA1C9FF),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    onPrimaryContainer = Color(0xFFD1E4FF),
    
    secondary = Color(0xFFBBC7DB),
    onSecondary = Color(0xFF253140),
    secondaryContainer = Color(0xFF3B4858),
    onSecondaryContainer = Color(0xFFD7E3F7),
    
    tertiary = Color(0xFFD6BEE4),
    onTertiary = Color(0xFF3B2948),
    tertiaryContainer = Color(0xFF523F5F),
    onTertiaryContainer = Color(0xFFF2DAFF),
    
    background = Color(0xFF191C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF191C1E),
    onSurface = Color(0xFFE2E2E6),
    error = Color(0xFFFFB4AB)
)

private val LightColorScheme = lightColorScheme(
    primary = CleanBlue,
    onPrimary = OnCleanBlue,
    primaryContainer = CleanBlueContainer,
    onPrimaryContainer = OnCleanBlueContainer,
    
    secondary = CleanSecondaryBlue,
    onSecondary = OnCleanSecondaryBlue,
    secondaryContainer = CleanSecondaryBlueContainer,
    onSecondaryContainer = OnCleanSecondaryBlueContainer,
    
    tertiary = CleanTertiaryBlue,
    onTertiary = OnCleanTertiaryBlue,
    tertiaryContainer = CleanTertiaryBlueContainer,
    onTertiaryContainer = OnCleanTertiaryBlueContainer,
    
    background = CleanBackground,
    onBackground = CleanOnSurface,
    
    surface = CleanSurface,
    onSurface = CleanOnSurface,
    
    surfaceVariant = CleanSurfaceVariant,
    onSurfaceVariant = CleanOnSurfaceVariant,
    
    outline = CleanOutline,
    error = Color(0xFFBA1A1A)
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
