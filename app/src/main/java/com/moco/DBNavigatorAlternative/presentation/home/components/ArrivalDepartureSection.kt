package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * ZEIT-AUSWAHL UND ABFAHRT/ANKUNFT SCHALTER
 * Erlaubt die Wahl der Uhrzeit und den Wechsel zwischen Abfahrts- und Ankunftszeit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalDepartureSection(
    isArrival: Boolean,               // false = Abfahrt, true = Ankunft
    timeText: String,                 // Die aktuell gewählte Uhrzeit (z.B. "14:30")
    showTimePicker: Boolean,          // Schalter für den Uhrzeit-Dialog
    onArrivalChange: (Boolean) -> Unit,// Funktion zum Wechseln des Schalters
    onTimeClick: () -> Unit,          // Funktion zum Öffnen der Uhrzeit-Auswahl
    onTimeSelected: (Int, Int) -> Unit,// Funktion, wenn Stunde/Minute gewählt wurden
    onDismiss: () -> Unit,            // Funktion zum Schließen des Dialogs
    modifier: Modifier = Modifier
) {
    // Merkt sich den Zustand der Uhr-Wählscheibe
    val timePickerState = rememberTimePickerState(is24Hour = true)

    // WENN showTimePicker true ist, wird die Material 3 Uhr eingeblendet
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onTimeSelected(timePickerState.hour, timePickerState.minute) }) {
                    Text("Auswählen")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Abbrechen") }
            },
            // Die eigentliche Uhr (Wählscheibe) als Inhalt des Dialogs
            text = { TimePicker(state = timePickerState) }
        )
    }

    // Eine Karte mit Rahmen (OutlinedCard) umschließt die Zeiteinstellungen
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Oberer Teil: Uhrzeit-Anzeige und "Ändern"-Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null) // Uhr-Icon
                    Spacer(Modifier.width(12.dp))
                    Text(text = "Uhrzeit", style = MaterialTheme.typography.titleMedium)
                }
                
                // Ein Button, der wie ein Text aussieht, zeigt die Uhrzeit groß an
                TextButton(onClick = { onTimeClick() }) {
                    Text(text = timeText, style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(Modifier.height(8.dp))

            /**
             * DER SEGMENTED BUTTON (Moderne Auswahl-Leiste)
             * Ersetzt den alten Switch und ist der Material 3 Standard für "Entweder-Oder" Wahlen.
             */
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                // Linke Option: Abfahrt
                SegmentedButton(
                    selected = !isArrival,
                    onClick = { onArrivalChange(false) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) {
                    Text("Abfahrt")
                }
                // Rechte Option: Ankunft
                SegmentedButton(
                    selected = isArrival,
                    onClick = { onArrivalChange(true) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) {
                    Text("Ankunft")
                }
            }
        }
    }
}
