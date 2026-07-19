package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * DATUMS-AUSWAHL
 * Zeigt das aktuell gewählte Datum in einer klickbaren Karte an.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(
    dateText: String,                 // Das aktuell gewählte Datum als Text
    showDatePicker: Boolean,          // Schalter für die Sichtbarkeit des Kalender-Dialogs
    onDateClick: () -> Unit,          // Aktion beim Klick auf die Karte (Dialog öffnen)
    onDateSelected: (Long?) -> Unit,  // Aktion wenn ein Datum im Kalender gewählt wurde
    onDismiss: () -> Unit,            // Aktion zum Schließen des Kalenders
    modifier: Modifier = Modifier
) {
    // Merkt sich den Zustand des Material 3 Kalenders
    val datePickerState = rememberDatePickerState()

    // WENN showDatePicker true ist, wird der Kalender-Dialog eingeblendet
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                    Text("Auswählen")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Abbrechen") }
            }
        ) {
            // Der eigentliche Kalender-Inhalt (Material 3 Komponente)
            DatePicker(state = datePickerState)
        }
    }

    // Eine OutlinedCard (mit Rahmen), die beim Anklicken den Kalender öffnet
    OutlinedCard(
        onClick = onDateClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        // ListItem sorgt für die perfekte Ausrichtung von Icon, Text und Label
        ListItem(
            headlineContent = { Text("Datum") }, // Was wird gewählt?
            supportingContent = { Text(dateText) }, // Welches Datum ist aktuell aktiv?
            leadingContent = {
                // Ein passendes Kalender-Icon links
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            },
            trailingContent = {
                // Ein kleiner Hinweis-Text rechts
                Text("Ändern", color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}
