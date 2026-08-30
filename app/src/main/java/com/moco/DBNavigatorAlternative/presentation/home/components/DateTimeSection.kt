package com.moco.DBNavigatorAlternative.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// # Datums-Auswahl
// Eine Karte, die das gewählte Datum anzeigt und beim Klick den Kalender öffnet.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(
    dateText: String,                 // Das aktuell gewählte Datum
    showDatePicker: Boolean,          // Ob der Kalender gerade offen sein soll
    onDateClick: () -> Unit,          // Kalender öffnen
    onDateSelected: (Long?) -> Unit,  // Wenn ein Datum ausgesucht wurde
    onDismiss: () -> Unit,            // Kalender einfach wieder schließen
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState()

    // Den Kalender-Dialog anzeigen, wenn er gebraucht wird
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
            DatePicker(state = datePickerState)
        }
    }

    OutlinedCard(
        onClick = onDateClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        ListItem(
            headlineContent = { Text("Datum") },
            supportingContent = { Text(dateText) },
            leadingContent = {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            },
            trailingContent = {
                Text("Ändern", color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}

