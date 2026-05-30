package com.example.myapplication.ui.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.model.*
import com.example.myapplication.ui.theme.MyApplicationTheme

class SearchConnection : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ConnectionSelectionScreen()
            }
        }
    }
}

@Composable
fun ConnectionSelectionScreen() {
    Scaffold(
        topBar = { SearchHeader() },
        bottomBar = { BottomNavigation() },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        // Mock-Daten basierend auf den Modellen
        val connections = listOf(
            createMockConnection("1", "RB1", TrainType.RB, "ICE 2", TrainType.ICE, 9.4f),
            createMockConnection("2", "RB2", TrainType.RB, "ICE 3", TrainType.ICE, 2.3f, "IC1", TrainType.IC),
            createMockConnection("3", "RB2", TrainType.RB, "ICE 5", TrainType.ICE, 5.4f, "SB 3", TrainType.S_BAHN)
        )

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(connections) { connection ->
                ConnectionCard(connection)
            }
        }
    }
}

@Composable
fun ConnectionCard(connection: Connection) {
    // Variablen um Daten aus Modell zu extrahieren
    val firstSegment = connection.segments.firstOrNull()
    val lastSegment = connection.segments.lastOrNull()
    val depTime = firstSegment?.departureStop?.time ?: "MM:HH"
    val arrTime = lastSegment?.arrivalStop?.time ?: "MM:HH"
    val score = firstSegment?.punctualityScore?.toDouble() ?: 0.0

    val isLate = score < 5.0
    // Simulierter Hinweis für die dritte Karte
    val showBindingHint = score in 5.0..6.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "$depTime -> $arrTime",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isLate) Color.Red else Color.Black
                    )
                    ScoreBadge(score)
                }

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    connection.segments.forEach { segment ->
                        TrainBadge(segment.train.line, getTrainColor(segment.train.type))
                    }
                }

                if (showBindingHint) {
                    Surface(color = Color(0xFF76B82A).copy(alpha = 0.8f), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black)) {
                        Text("Zugbindung zu 94% aufgehoben", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Color.Black, fontSize = 14.sp)
                    }
                }
            }

            Surface(modifier = Modifier.align(Alignment.BottomEnd).size(40.dp), shape = RoundedCornerShape(8.dp), color = Color(0xFFE0E0E0)) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

//Funktion für Farben basierend auf TrainType
fun getTrainColor(type: TrainType): Color = when (type) {
    TrainType.RB, TrainType.RE -> Color(0xFFE2104E)
    TrainType.ICE -> Color.Gray
    TrainType.IC -> Color(0xFF009FE3)
    TrainType.S_BAHN -> Color(0xFF951A81)
    else -> Color.Gray
}

// Composables und temporär Mock-Daten Generator

@Composable
fun ScoreBadge(score: Double) {
    val bgColor = when {
        score >= 8.0 -> Color(0xFF76B82A)
        score >= 5.0 -> Color(0xFFFFD700)
        else -> Color(0xFFE2104E)
    }
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        // Double-Wert auf eine Nachkommastelle
        Text(
            text = "%.1f".format(score),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TrainBadge(name: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black)) {
        Text(text = name, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun SearchHeader() {
    Surface(color = Color(0xFFE0E0E0), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp).statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Von", "Bis").forEach { label ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedButton(onClick = {}, shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color.Black), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = Color.Black), modifier = Modifier.width(80.dp)) { Text(label) }
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.fillMaxWidth().height(48.dp), shape = RoundedCornerShape(24.dp), leadingIcon = { Icon(Icons.Default.Search, null) }, colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedBorderColor = Color.Black, unfocusedBorderColor = Color.Black))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Früher", "Jetzt", "Später").forEach { label ->
                    OutlinedButton(onClick = {}, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = Color.Black)) { Text(label) }
                }
            }
        }
    }
}

@Composable
fun BottomNavigation() {
    BottomAppBar(containerColor = Color(0xFFE0E0E0)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, null, Modifier.size(32.dp))
            Icon(Icons.Default.Search, null, Modifier.size(48.dp))
            Box(modifier = Modifier.size(32.dp).background(Color.LightGray))
        }
    }
}

private fun createMockConnection(id: String, t1Name: String, t1Type: TrainType, t2Name: String, t2Type: TrainType, score: Float, t3Name: String? = null, t3Type: TrainType? = null): Connection {
    val segments = mutableListOf(
        ConnectionSegment(Stop("A", "A", "MM:HH"), Stop("B", "B", "MM:HH"), Train("T1", t1Type, t1Name), 0f, score),
        ConnectionSegment(Stop("B", "B", "MM:HH"), Stop("C", "C", "MM:HH"), Train("T2", t2Type, t2Name), 0f, score)
    )
    if (t3Name != null && t3Type != null) {
        segments.add(ConnectionSegment(Stop("C", "C", "MM:HH"), Stop("D", "D", "MM:HH"), Train("T3", t3Type, t3Name), 0f, score))
    }
    return Connection(id, segments, 60, segments.size - 1)
}

@Preview(showBackground = true)
@Composable
fun ConnectionPreview() {
    MyApplicationTheme { ConnectionSelectionScreen() }
}