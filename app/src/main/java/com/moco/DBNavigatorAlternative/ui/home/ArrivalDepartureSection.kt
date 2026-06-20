package com.moco.DBNavigatorAlternative.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalDepartureSection(
    modifier: Modifier = Modifier
) {
    var isArrival by remember {
        mutableStateOf(false)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    val calendar = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    var selectedTimeText by remember {
        mutableStateOf(
            formatTime(
                hour = timePickerState.hour,
                minute = timePickerState.minute
            )
        )
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = {
                showTimePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedTimeText = formatTime(
                            hour = timePickerState.hour,
                            minute = timePickerState.minute
                        )

                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                    }
                ) {
                    Text("Abbrechen")
                }
            },
            text = {
                TimePicker(
                    state = timePickerState
                )
            }
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1.5f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Abfahrt",
                fontSize = 12.sp
            )

            Switch(
                checked = isArrival,
                onCheckedChange = {
                    isArrival = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    uncheckedThumbColor = Color.Black,
                    checkedTrackColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )

            Text(
                text = "Ankunft",
                fontSize = 12.sp
            )
        }

        Button(
            onClick = {
                val now = Calendar.getInstance()

                selectedTimeText = formatTime(
                    hour = now.get(Calendar.HOUR_OF_DAY),
                    minute = now.get(Calendar.MINUTE)
                )
            },
            modifier = Modifier
                .weight(0.8f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(
                text = "Jetzt los",
                fontSize = 11.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(0.7f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
                .clickable {
                    showTimePicker = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text(selectedTimeText)
        }
    }
}

private fun formatTime(
    hour: Int,
    minute: Int
): String {
    return String.format(
        Locale.GERMANY,
        "%02d:%02d",
        hour,
        minute
    )
}