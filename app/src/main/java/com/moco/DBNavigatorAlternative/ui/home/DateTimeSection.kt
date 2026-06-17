package com.moco.DBNavigatorAlternative.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()

    val formatter = remember {
        SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    }

    var selectedDateText by remember {
        mutableStateOf(formatter.format(Date()))
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDateText = formatter.format(Date(millis))
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Abbrechen")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable {
                    showDatePicker = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Datum")
        }

        Box(
            modifier = Modifier
                .width(110.dp)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable {
                    showDatePicker = true
                },
            contentAlignment = Alignment.Center
        ) {
            Text(selectedDateText)
        }
    }
}
