package com.example.myapplication.ui.home

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchRow(label = "Von")
        SearchRow(label = "Bis")
    }
}

@Composable
fun SearchRow(label: String) {
    var text by remember { mutableStateOf("") }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(40.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(label)
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY) }
    var selectedDateText by remember { mutableStateOf(formatter.format(Date())) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        selectedDateText = formatter.format(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Abbrechen")
                }
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
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable { showDatePicker = true },
            contentAlignment = Alignment.Center
        ) {
            Text("Datum")
        }
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable { showDatePicker = true },
            contentAlignment = Alignment.Center
        ) {
            Text(selectedDateText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalDepartureSection(modifier: Modifier = Modifier) {
    var isArrival by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    var selectedTimeText by remember { 
        mutableStateOf(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)) 
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedTimeText = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Abbrechen") }
            },
            text = {
                TimePicker(state = timePickerState)
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
            Text("Abfahrt", fontSize = 12.sp)
            Switch(
                checked = isArrival,
                onCheckedChange = { isArrival = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    uncheckedThumbColor = Color.Black,
                    checkedTrackColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )
            Text("Ankunft", fontSize = 12.sp)
        }

        Button(
            onClick = {
                val now = Calendar.getInstance()
                selectedTimeText = String.format("%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
            },
            modifier = Modifier
                .weight(0.8f)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text("Jetzt los", fontSize = 11.sp)
        }

        Box(
            modifier = Modifier
                .weight(0.7f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
                .clickable { showTimePicker = true },
            contentAlignment = Alignment.Center
        ) {
            Text(selectedTimeText)
        }
    }
}

@Composable
fun TicketOptionSection(modifier: Modifier = Modifier) {
    var onlyDTicket by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Nur D-Ticket Verbindungen")
        }
        Switch(
            checked = onlyDTicket,
            onCheckedChange = { onlyDTicket = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                uncheckedThumbColor = Color.Black,
                checkedTrackColor = Color.Gray,
                uncheckedTrackColor = Color.LightGray
            )
        )
    }
}

@Composable
fun SearchButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { },
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
    ) {
        Text("SUCHEN", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FavoritesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Meine Favoriten",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        FavoriteItem("Verbindung 1")
        FavoriteItem("Verbindung 2")
        FavoriteItem("Verbindung 3")
        FavoriteItem("Verbindung 4")
    }
}

@Composable
fun FavoriteItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color(0xFFFFD700) // Gold
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text)
        }
    }
}

@Composable
fun HomeBottomBar() {
    BottomAppBar(
        containerColor = Color.LightGray,
        contentColor = Color.Black,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(32.dp))
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(32.dp), tint = Color.Gray)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewComponents() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SearchSection()
        DateTimeSection()
        ArrivalDepartureSection()
        TicketOptionSection()
        SearchButton()
        FavoritesSection()
    }
}
