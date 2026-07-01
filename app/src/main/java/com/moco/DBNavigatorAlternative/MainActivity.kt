package com.moco.DBNavigatorAlternative

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.moco.DBNavigatorAlternative.presentation.navigation.AppNavigation
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

/**
 * Der Haupteinstiegspunkt deiner App.
 * Stell es dir wie den Zündschlüssel vor, der den Motor (deine App) startet.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Sorgt dafür, dass die App den ganzen Bildschirm nutzt
        enableEdgeToEdge()
        
        setContent {
            // Wir nutzen dein Design (Theme)
            MyApplicationTheme {
                // Hier starten wir deine Navigation
                AppNavigation()
            }
        }
    }
}
