package com.moco.DBNavigatorAlternative.presentation.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

/**
 * Haupteinstiegspunkt (Activity) für den Bereich der Verbindungssuche.
 * Initialisiert das UI-Theme und lädt den ConnectionSelectionScreen.
 */
class SearchConnection : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        // Aktiviert Edge-to-Edge Design für moderne Android-Systeme
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                // Haupt-UI der Verbindungsauswahl
                ConnectionSelectionScreen()
            }
        }
    }
}
