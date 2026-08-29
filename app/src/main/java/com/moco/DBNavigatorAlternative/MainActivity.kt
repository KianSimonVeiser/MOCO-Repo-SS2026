package com.moco.DBNavigatorAlternative

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppNavigation
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

/**
 * Haupteinstiegspunkt der Anwendung.
 * Initialisiert die App-Navigation und das globale Theme.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        UserRepository.context = applicationContext

        // Aktiviert Edge-to-Edge Design
        enableEdgeToEdge()
        
        setContent {
            MyApplicationTheme {
                // Startet die zentrale Navigation der App
                AppNavigation()
            }
        }
    }
}
