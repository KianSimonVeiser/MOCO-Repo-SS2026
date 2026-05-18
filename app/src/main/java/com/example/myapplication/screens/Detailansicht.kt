package com.example.myapplication.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Connection
import com.example.myapplication.model.ConnectionSegment
import com.example.myapplication.model.Stop
import com.example.myapplication.model.Train
import com.example.myapplication.model.TrainType

val testconnection = Connection(
    id = "conn001",
    totalDurationMinutes = 180,
    transferCount = 1,

    segments = listOf(

        ConnectionSegment(
            departureStop = Stop(
                id = "FFM",
                name = "Frankfurt Hbf",
                time = "14:30",
                platform = "7"
            ),

            arrivalStop = Stop(
                id = "KAS",
                name = "Kassel-Wilhelmshöhe",
                time = "15:50",
                platform = "3"
            ),

            train = Train(
                id = "ice572",
                type = TrainType.ICE,
                line = "ICE 572"
            )
        ),

        ConnectionSegment(
            departureStop = Stop(
                id = "KAS",
                name = "Kassel-Wilhelmshöhe",
                time = "16:00",
                platform = "5"
            ),

            arrivalStop = Stop(
                id = "BER",
                name = "Berlin Hbf",
                time = "17:30",
                platform = "11"
            ),

            train = Train(
                id = "re21",
                type = TrainType.RE,
                line = "RE 21"
            )
        )
    )
)

@Composable
fun StopCard(
    stop: Stop
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = stop.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Zeit: ${stop.time}"
            )

            Text(
                text = "Gleis: ${stop.platform}"
            )
        }
    }
}

@Composable
fun SaveConnectionSwitch() {
    var checked by remember {
        mutableStateOf(true)
    }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    connection: Connection
) {
    val stops = remember(connection) {
        buildList {

            add(connection.segments.first().departureStop)

            connection.segments.forEach {
                add(it.arrivalStop)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Verbindung speichern")
                },
                actions = {
                    SaveConnectionSwitch()
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },

        bottomBar = {
            BottomAppBar(
                actions = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.AddCircle,
                                contentDescription = "Add"
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.AreaChart,
                                contentDescription = "Chart"
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Person"
                            )
                        }
                    }
                }
            )
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ){
            items(stops) {stop ->
                StopCard(
                    stop = stop
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        connection = testconnection
    )
}