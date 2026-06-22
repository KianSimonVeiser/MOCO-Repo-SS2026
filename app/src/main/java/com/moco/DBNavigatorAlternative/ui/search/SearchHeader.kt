package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.ui.home.SearchSection
import com.moco.DBNavigatorAlternative.ui.home.DateTimeSection

/**
 * Der SearchHeader bündelt alle UI-Komponenten für die Verbindungssuche.
 * Er integriert die Sektionen für Start/Ziel und Datum/Uhrzeit.
 * Der Suchen-Button wurde entfernt, da die Suche automatisch aktualisiert.
 * Stattdessen gibt es Optionen für "Früher" und "Später".
 */
@Composable
fun SearchHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        // Abstand zwischen den einzelnen Such-Sektionen
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Eingabebereich für Start und Ziel
        SearchSection()
        
        // Auswahl für Datum und Uhrzeit
        DateTimeSection()

        // Früher / Später Optionen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Frühere Verbindungen laden */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Früher")
            }
            Button(
                onClick = { /* Spätere Verbindungen laden */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text("Später")
            }
        }
    }
}
