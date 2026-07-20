package com.moco.DBNavigatorAlternative.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.R

/**
 * Sektion zur Auswahl von Uhrzeit und Abfahrt/Ankunft.
 * Nutzt das europäische 24h-Zeitformat.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalDepartureSection(
    isArrival: Boolean,
    timeText: String,
    showTimePicker: Boolean,
    onArrivalChange: (Boolean) -> Unit,
    onTimeClick: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timePickerState = rememberTimePickerState(is24Hour = true)

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onTimeSelected(timePickerState.hour, timePickerState.minute) }) {
                    Text(stringResource(id = R.string.ok_button))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(id = R.string.cancel_button))
                }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text(text = stringResource(id = R.string.select_time), style = MaterialTheme.typography.titleMedium)
                }
                
                TextButton(onClick = { onTimeClick() }) {
                    Text(text = timeText, style = MaterialTheme.typography.titleLarge)
                }
            }

            Spacer(Modifier.height(8.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = !isArrival,
                    onClick = { onArrivalChange(false) },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) {
                    Text(stringResource(id = R.string.departure))
                }
                SegmentedButton(
                    selected = isArrival,
                    onClick = { onArrivalChange(true) },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) {
                    Text(stringResource(id = R.string.arrival))
                }
            }
        }
    }
}
