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
    error = MutedDanger
)

private val LightColorScheme = lightColorScheme(
    primary = IndigoBlue,                 // Seriöses Blau
    onPrimary = Color.White,
    primaryContainer = SkyBlueSoft,       // Sanftes Blau-Grau für Header
    onPrimaryContainer = CharcoalGray,
    
    secondary = AmethystPurple,           // Elegantes Lila
    onSecondary = Color.White,
    secondaryContainer = LavenderSoft,    // Zartes Lila für Sektionen
    onSecondaryContainer = CharcoalGray,
    
    tertiary = CrystalBlue,               // Kristalblau für Akzente
    onTertiary = CharcoalGray,
    
    background = OffWhite,                // Sauberer, kühler Hintergrund
    onBackground = CharcoalGray,
    
    surface = Color.White,                // Reinweiß für Karten
    onSurface = CharcoalGray,
    
    surfaceVariant = SilverGray,          // Graue Trenner und Rahmen
    onSurfaceVariant = CharcoalGray,
    
    error = MutedDanger,                  // Völlig neutralisierte Gefahrenzone
    onError = CharcoalGray
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
