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
    error = SoftDanger
)

private val LightColorScheme = lightColorScheme(
    primary = DeepBlue,                  // Seriöses Dunkelblau
    onPrimary = Color.White,
    primaryContainer = SoftBlue,         // Sanftes Hellblau für Header/Container
    onPrimaryContainer = IronGray,
    
    secondary = RoyalPurple,             // Elegantes Lila
    onSecondary = Color.White,
    secondaryContainer = PastelPurple,   // Zartes Lila für Akzente
    onSecondaryContainer = IronGray,
    
    tertiary = SoftPurple,               // Ergänzendes sanftes Lila
    onTertiary = IronGray,
    
    background = CoolGray,               // Kaltes, sauberes Grau für den Hintergrund
    onBackground = IronGray,
    
    surface = Color.White,               // Weiß für Kartenoberflächen
    onSurface = IronGray,
    
    surfaceVariant = PastelBlue,         // Sanftes Blau für Buttons/Details
    onSurfaceVariant = IronGray,
    
    error = SoftDanger,                  // Dezente Fehlermeldung
    onError = IronGray
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamische Farben deaktiviert, um das gewählte Branding zu erzwingen
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
