package com.moco.DBNavigatorAlternative.ui.search

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
import com.moco.DBNavigatorAlternative.ui.theme.MyApplicationTheme

//Datenmodelle
data class TrainData(val name: String, val color: Color)
data class ConnectionData(
    val depTime: String,
    val arrTime: String,
    val score: Double,
    val trains: List<TrainData>,
    val showBindingHint: Boolean = false,
    val isLate: Boolean = false
)

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
        // Beispiel-Daten basierend auf Wireframe
        val connections = listOf(
            ConnectionData("MM:HH", "MM:HH", 9.4, listOf(TrainData("RB1", Color(0xFFE2104E)), TrainData("ICE 2", Color.Gray))),
            ConnectionData("MM:HH", "MM:HH", 2.3, listOf(TrainData("RB2", Color(0xFFE2104E)), TrainData("ICE 3", Color.Gray), TrainData("IC1", Color(0xFF009FE3))), isLate = true),
            ConnectionData("MM:HH", "MM:HH", 5.4, listOf(TrainData("RB2", Color(0xFFE2104E)), TrainData("ICE 5", Color.Gray), TrainData("SB 3", Color(0xFF951A81))), showBindingHint = true)
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
fun SearchHeader() {
    Surface(
        color = Color(0xFFE0E0E0),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .statusBarsPadding(), verticalArrangement = Arrangement.spacedBy(8.dp)) {

            // Zeile: Von
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black), // Schwarze Umrandung
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.width(80.dp)
                ) { Text("Von") }

                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )
                )
            }

            // Zeile: Bis
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Black), // Schwarze Umrandung
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier.width(80.dp)
                ) { Text("Bis") }

                Spacer(Modifier.width(8.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )
                )
            }

            // Zeile: Zeit-Filter
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Früher", "Jetzt", "Später").forEach { label ->
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConnectionCard(connection: ConnectionData) {
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
                        text = "${connection.depTime} -> ${connection.arrTime}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (connection.isLate) Color.Red else Color.Black
                    )
                    ScoreBadge(connection.score)
                }

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    connection.trains.forEach { train ->
                        TrainBadge(train.name, train.color)
                    }
                }

                if (connection.showBindingHint) {
                    Surface(color = Color(0xFF76B82A).copy(alpha = 0.8f), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black)) {
                        Text("Zugbindung zu 94% aufgehoben", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = Color.Black, fontSize = 14.sp)
                    }
                }
            }

            // Icon Platzhalter unten rechts
            Surface(modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(40.dp), shape = RoundedCornerShape(8.dp), color = Color(0xFFE0E0E0)) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun ScoreBadge(score: Double) {
    val bgColor = when {
        score >= 8.0 -> Color(0xFF76B82A)
        score >= 5.0 -> Color(0xFFFFD700)
        else -> Color(0xFFE2104E)
    }
    Surface(color = bgColor, shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color.Black)) {
        Text(text = score.toString(), modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TrainBadge(name: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, Color.Black)) {
        Text(text = name, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun BottomNavigation() {
    BottomAppBar(containerColor = Color(0xFFE0E0E0)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(32.dp))
            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(48.dp))
            Box(modifier = Modifier
                .size(32.dp)
                .background(Color.LightGray)) // Platzhalter Icon
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectionPreview() {
    MyApplicationTheme {
        ConnectionSelectionScreen()
    }
}

