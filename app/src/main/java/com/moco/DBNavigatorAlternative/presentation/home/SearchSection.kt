package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * DIE SUCHE-KARTE
 * Diese Komponente nutzt eine ElevatedCard (erhabene Karte), um die Eingabefelder
 * für Start und Ziel optisch vom Hintergrund abzuheben.
 */
@Composable
fun SearchSection(
    fromValue: String,                // Aktueller Text für den Startbahnhof
    toValue: String,                  // Aktueller Text für den Zielbahnhof
    onFromChange: (String) -> Unit,   // Funktion, die gerufen wird, wenn man im Startfeld tippt
    onToChange: (String) -> Unit,     // Funktion, die gerufen wird, wenn man im Zielfeld tippt
    modifier: Modifier = Modifier
) {
    // ElevatedCard erzeugt eine Karte mit leichtem Schatten (Material 3 Standard)
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large // Nutzt die großen Rundungen aus dem Theme
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Abstand zwischen den Feldern
        ) {
            // Eine kleine Überschrift für die Sektion
            Text(
                text = "Reiseplanung",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Eingabefeld für den Startpunkt (Von)
            OutlinedTextField(
                value = fromValue,
                onValueChange = onFromChange,
                label = { Text("Von") },
                placeholder = { Text("Startbahnhof") },
                modifier = Modifier.fillMaxWidth(),
                // Ein Standort-Icon am Anfang des Feldes
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                shape = MaterialTheme.shapes.medium
            )

            // Eingabefeld für das Ziel (Bis)
            OutlinedTextField(
                value = toValue,
                onValueChange = onToChange,
                label = { Text("Bis") },
                placeholder = { Text("Zielbahnhof") },
                modifier = Modifier.fillMaxWidth(),
                // Eine Lupe am Anfang des Feldes
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}
