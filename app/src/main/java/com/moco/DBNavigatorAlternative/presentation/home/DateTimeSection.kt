package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(
    dateText: String,                 // Das formatierte Datum aus dem ViewModel
    showDatePicker: Boolean,          // Ob der Dialog gerade sichtbar ist
    onDateClick: () -> Unit,          // Klick auf die Box zum Öffnen
    onDateSelected: (Long?) -> Unit,  // Auswahl im Kalender
    onDismiss: () -> Unit,            // Dialog schließen
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Abbrechen") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f).height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable { onDateClick() },
            contentAlignment = Alignment.Center
        ) { Text("Datum") }

        Box(
            modifier = Modifier.width(110.dp).height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable { onDateClick() },
            contentAlignment = Alignment.Center
        ) { Text(dateText) }
    }
}