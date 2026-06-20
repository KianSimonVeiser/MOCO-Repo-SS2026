package com.moco.DBNavigatorAlternative.ui.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.sp

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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text("Abbrechen") }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1.5f).height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Abfahrt", fontSize = 12.sp)
            Switch(
                checked = isArrival,
                onCheckedChange = onArrivalChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Black)
            )
            Text("Ankunft", fontSize = 12.sp)
        }

        Button(
            // Hier nutzen wir einfach die aktuelle Zeit des Systems
            onClick = {
                val now = java.util.Calendar.getInstance()
                onTimeSelected(now.get(java.util.Calendar.HOUR_OF_DAY), now.get(java.util.Calendar.MINUTE))
            },
            modifier = Modifier.weight(0.8f).height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(1.dp, Color.Black)
        ) { Text("Jetzt los", fontSize = 11.sp) }

        Box(
            modifier = Modifier.weight(0.7f).height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
                .clickable { onTimeClick() },
            contentAlignment = Alignment.Center
        ) { Text(timeText) }
    }
}