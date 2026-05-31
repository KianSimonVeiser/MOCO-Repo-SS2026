package com.example.myapplication.ui.home


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/*scaffold für den homescreen
 */
@Composable
fun HomeScreen() {

    Scaffold(
        bottomBar = { HomeBottomBar() } // Die untere Navigationsleiste
    ) { innerPadding ->
        // Column ordnet Elemente vertikal untereinander an
        Column(
            modifier = Modifier
                .padding(innerPadding) // Sorgt dafür, dass der Inhalt nicht hinter der BottomBar verschwindet
                .padding(horizontal = 16.dp) // Abstand zum linken und rechten Bildschirmrand (16 Dichte-Pixel)
                .verticalScroll(rememberScrollState()) // Macht die ganze Seite scrollbar (wichtig bei vielen Favoriten!)
        ) {
            // Wir rufen nacheinander die verschiedenen Abschnitte (Funktionen) auf:
            SearchSection(modifier = Modifier.padding(top = 16.dp)) // Die Von/Bis Felder
            DateTimeSection(modifier = Modifier.padding(top = 16.dp)) // Datumsauswahl
            ArrivalDepartureSection(modifier = Modifier.padding(top = 16.dp)) // Zeit & Abfahrt/Ankunft
            TicketOptionSection(modifier = Modifier.padding(top = 16.dp)) // D-Ticket Schalter
            SearchButton(modifier = Modifier.padding(top = 16.dp)) // Der große SUCHE Button
            FavoritesSection(modifier = Modifier.padding(top = 16.dp)) // Die Favoriten-Liste unten
        }
    }
}

/**
 * DIE SUCHE (Von & Bis)
 */
@Composable
fun SearchSection(modifier: Modifier = Modifier) {
    // Ein grauer Kasten, der zwei Suchzeilen untereinander stapelt
    Column(
        modifier = modifier
            .fillMaxWidth() // Nutzt die gesamte Breite des Bildschirms
            .border(1.dp, Color.LightGray) // Ein feiner grauer Rand
            .padding(16.dp), // Abstand vom Rand des Kastens zum Text innen
        verticalArrangement = Arrangement.spacedBy(8.dp) // 8dp Abstand zwischen "Von" und "Bis"
    ) {
        SearchRow(label = "Von") // Die Zeile für den Startpunkt
        SearchRow(label = "Bis") // Die Zeile für das Ziel
    }
}

/**
 * Eine einzelne Zeile für die Suche (Label + Textfeld).
 */
@Composable
fun SearchRow(label: String) {
    var text by remember { mutableStateOf("") } // Speichert den eingegebenen Text
    Row(
        verticalAlignment = Alignment.CenterVertically, // Richtet Text und Eingabefeld mittig auf einer Linie aus
        modifier = Modifier.fillMaxWidth()
    ) {
        // Die kleine Box links mit der Beschriftung (Von/Bis)
        Box(
            modifier = Modifier
                .width(60.dp) // Feste Breite
                .height(40.dp) // Feste Höhe
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)), // Schwarzer Rand mit 8dp Rundung
            contentAlignment = Alignment.Center // Text in der Mitte der Box platzieren
        ) {
            Text(label)
        }
        
        Spacer(modifier = Modifier.width(8.dp)) // Eine kleine Lücke zwischen Box und Eingabefeld
        
        // Das eigentliche Schreib-Feld (Outlined = mit Umrandung)
        OutlinedTextField(
            value = text, // Welcher Text wird gerade angezeigt?
            onValueChange = { text = it }, // Was passiert beim Tippen? -> Aktualisiere den Text!
            modifier = Modifier
                .weight(1f) // Nimmt sich den restlichen Platz in der Reihe
                .height(56.dp),
            shape = RoundedCornerShape(28.dp), // Sehr rund (sieht aus wie eine Pille)
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }, // Lupe am Anfang
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black, // Randfarbe wenn man reinklickt
                unfocusedBorderColor = Color.Black // Randfarbe wenn man nicht reinklickt
            )
        )
    }
}

/**
 * Sektion für die Datumsauswahl.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeSection(modifier: Modifier = Modifier) {
    // Ein Schalter: Ist der Kalender gerade offen (true) oder zu (false)?
    var showDatePicker by remember { mutableStateOf(false) } 
    // Der "Zustand" des Kalenders (welcher Tag ist markiert?)
    val datePickerState = rememberDatePickerState()

    // Ein Umwandler: Macht aus Computer-Daten lesbaren Text wie "24.12.2024"
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY) }
    // Der Text, den wir im Button sehen (Standardmäßig das heutige Datum)
    var selectedDateText by remember { mutableStateOf(formatter.format(Date())) } 

    // Wenn showDatePicker true ist, wird der Dialog angezeigt
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false }, // Schließen, wenn man daneben klickt
            confirmButton = {
                TextButton(onClick = {
                    // Wenn der Nutzer ein Datum wählt und OK drückt:
                    datePickerState.selectedDateMillis?.let {
                        selectedDateText = formatter.format(Date(it)) // Text aktualisieren
                    }
                    showDatePicker = false // Kalender wieder zumachen
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
            // Der eigentliche Kalender-Inhalt im Fenster
            DatePicker(state = datePickerState)
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Linke Box ("Datum") - wir machen sie klickbar mit .clickable
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable { showDatePicker = true }, // Öffnet den Kalender beim Klick
            contentAlignment = Alignment.Center
        ) {
            Text("Datum")
        }
        
        // Rechte Box (Hier steht das gewählte Datum drin)
        Box(
            modifier = Modifier
                .width(110.dp)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .clickable { showDatePicker = true }, // Öffnet auch den Kalender
            contentAlignment = Alignment.Center
        ) {
            Text(selectedDateText)
        }
    }
}

/**
 * ABFAHRT, ANKUNFT & UHRZEIT
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrivalDepartureSection(modifier: Modifier = Modifier) {
    var isArrival by remember { mutableStateOf(false) } // Schalter für Abfahrt/Ankunft
    var showTimePicker by remember { mutableStateOf(false) } // Schalter für das Uhrzeit-Fenster
    
    // Holt die aktuelle Zeit vom Handy für den Startwert der Uhr
    val calendar = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true // Deutsche 24h Anzeige (statt AM/PM)
    )

    // Der Text der Uhrzeit, den wir auf dem Screen sehen
    var selectedTimeText by remember { 
        mutableStateOf(String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)) 
    }

    // WENN showTimePicker auf 'true' steht, zeige die Uhr
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Zeit aus der Uhr übernehmen und anzeigen
                    selectedTimeText = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Abbrechen") }
            },
            text = {
                TimePicker(state = timePickerState) // Die runde Wählscheibe für die Uhr
            }
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Der kombinierte Schalter für Abfahrt/Ankunft
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
                onCheckedChange = { isArrival = it }, // Ändert den Zustand beim Umlegen
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    uncheckedThumbColor = Color.Black
                )
            )
            Text("Ankunft", fontSize = 12.sp)
        }

        // Der "Jetzt los" Button
        Button(
            onClick = {
                // Holt sich SOFORT die aktuelle Zeit und zeigt sie an
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

        // Die kleine Box, die die Uhrzeit anzeigt
        Box(
            modifier = Modifier
                .weight(0.7f)
                .height(48.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
                .clickable { showTimePicker = true }, // Öffnet die Uhr beim Klick
            contentAlignment = Alignment.Center
        ) {
            Text(selectedTimeText)
        }
    }
}

/**
 * TICKET FILTER
 */
@Composable
fun TicketOptionSection(modifier: Modifier = Modifier) {
    var onlyDTicket by remember { mutableStateOf(false) } // Merkt sich, ob nur D-Ticket aktiv ist
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
                uncheckedThumbColor = Color.Black
            )
        )
    }
}

/**
 * DER GROSSE SUCHEN BUTTON
 */
@Composable
fun SearchButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* Hier kommt später die Logik für die Suche rein */ },
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp) // Extra hoch, damit man ihn nicht verfehlt
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
    ) {
        Text("SUCHEN", fontSize = 24.sp, fontWeight = FontWeight.Bold) // Fettschrift und groß
    }
}

/**
 * FAVORITEN LISTE
 */
@Composable
fun FavoritesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Meine Favoriten",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        // Wir rufen den Baustein "FavoriteItem" viermal mit verschiedenen Namen auf
        FavoriteItem("Verbindung 1")
        FavoriteItem("Verbindung 2")
        FavoriteItem("Verbindung 3")
        FavoriteItem("Verbindung 4")
    }
}

/**
 * EIN EINZELNER FAVORIT (Stern + Kasten)
 */
@Composable
fun FavoriteItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Das Stern-Icon
        Icon(
            Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = Color(0xFFFFD700) // Gold-Gelbe Farbe
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Die Box mit dem Namen der Verbindung
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

/**
 * DIE UNTERE LEISTE (Navigationsleiste)
 */
@Composable
fun HomeBottomBar() {
    BottomAppBar(
        containerColor = Color.LightGray, // Hintergrundfarbe der Leiste
        contentColor = Color.Black,
        modifier = Modifier.height(80.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround, // Symbole gleichmäßig verteilen
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Drei klickbare Icons für die Navigation
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

/**
 * VORSCHAU (Nur für Android Studio)
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen() // Zeigt uns den kompletten Screen in der Design-Ansicht
    }
}
