package com.moco.DBNavigatorAlternative.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.model.Connection
import com.moco.DBNavigatorAlternative.model.ConnectionSegment
import com.moco.DBNavigatorAlternative.model.Stop
import com.moco.DBNavigatorAlternative.model.Train
import com.moco.DBNavigatorAlternative.model.TrainType
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.moco.DBNavigatorAlternative.model.Comment

val testconnection = Connection(
    id = "conn001",
    totalDurationMinutes = 180,
    transferCount = 1,

    segments = listOf(

        ConnectionSegment(
            id = "1",

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
            ),
            currentProgress = 1f,
            punctualityScore = 9.392809f
        ),

        ConnectionSegment(
            id = "2",
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
            ),
            currentProgress = 0.9f
        ),

        ConnectionSegment(
            id = "3",
            departureStop = Stop(
                id = "BER",
                name = "Berlin-HBF",
                time = "18:00",
                platform = "6"
            ),

            arrivalStop = Stop(
                id = "MUN",
                name = "München HBF",
                time = "23:30",
                platform = "11"
            ),

            train = Train(
                id = "ic12",
                type = TrainType.IC,
                line = "IC 21"
            ),
            currentProgress = 0f,
            punctualityScore = 6.0f
        )
    )
)

val testComments = listOf(
    Comment(
        id = "1",
        userId = "user1",
        segmentId = "1",
        content = "Dieser Abschnitt ist häufig verspätet."
    ),
    Comment(
        id = "2",
        userId = "user2",
        segmentId = "2",
        content = "Hier ist der Umstieg manchmal knapp."
    ),
    Comment(
        id = "3",
        userId = "user3",
        segmentId = "3",
        content = "Auf diesem Abschnitt gibt es öfter Gleisänderungen."
    )
)

@Composable
fun ConnectionStop(
    stop: Stop
) {
    Text(
        text = "${stop.time} - ${stop.name} - ${stop.platform}",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 10.dp),
        fontSize = 20.sp
    )
    /*
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
                text = ": ${stop.time}\n"
            )

            Text(
                text = "Gleis: ${stop.platform}"
            )
        }
    }*/
}

@Composable
fun TrainCard(
    train: Train
){
    Card(modifier = Modifier
        .padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = train.type.color()
        )
    ){
        Text(
            text = train.line,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )

    }
}

fun TrainType.color(): Color = when (this) {
    TrainType.ICE -> Color(0xFFE53935)
    TrainType.IC -> Color(0xFF1E88E5)
    TrainType.RE -> Color(0xFF43A047)
    TrainType.RB -> Color(0xFF7CB342)
    TrainType.S_BAHN -> Color(0xFF2E7D32)
    TrainType.U_BAHN -> Color(0xFF1565C0)
    TrainType.TRAM -> Color(0xFF8E24AA)
    TrainType.BUS -> Color(0xFFFF8F00)
}

@Composable
fun TrainProgressBar(
    progress: Float
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .width(6.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.LightGray)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress)
                .align(Alignment.TopCenter) // wächst nach unten
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

fun punctualityColor(score: Float?): Color {
    if (score == null) return Color.Gray
    val clamped = score.coerceIn(0f, 10f)
    val normalized = clamped / 10f

    val red = Color(0xFFE53935)
    val yellow = Color(0xFFFDD835)
    val green = Color(0xFF43A047)

    return when {
        normalized < 0.5f -> {
            // Rot → Gelb
            lerp(red, yellow, normalized / 0.5f)
        }
        else -> {
            // Gelb → Grün
            lerp(yellow, green, (normalized - 0.5f) / 0.5f)
        }
    }
}

@Composable
fun TrainPuncutalityCard(
    punctualityScore: Float?
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = punctualityColor(
                punctualityScore
            )
        )
    ){
        Text(
            text = punctualityScore
                ?.let { "%.1f".format(it) }
                ?: "Keine Daten",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Composable
fun ConnectionSegment(
    connectionSegment: ConnectionSegment,
    modifier: Modifier
){
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        ConnectionStop(connectionSegment.departureStop)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TrainProgressBar(progress = connectionSegment.currentProgress)
            TrainCard(train = connectionSegment.train)
            TrainPuncutalityCard(punctualityScore = connectionSegment.punctualityScore)
        }
        ConnectionStop(connectionSegment.arrivalStop)
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
    //Connection Segmente
    val segments = remember(connection) {
        buildList{
            connection.segments.forEach {
                add(it)
            }
        }
    }
    //States für Popup aus Material Design 3
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var showCommentSheet by remember {
        mutableStateOf(false)
    }

    val commentCount = testComments.size

    Scaffold (
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                //conent Padding, um "verdeckete Inhalte" zu zeigen
                contentPadding = PaddingValues(
                    bottom = 320.dp
                )
            ) {
                items(segments) { segment ->
                    ConnectionSegment(segment, modifier = Modifier)
                }

            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .width(350.dp)
                            .height(90.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Historische Pünktlichkeit",
                                    fontSize = 20.sp
                                )

                                TrainPuncutalityCard(3.5f)
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .width(350.dp)
                            .height(60.dp),
                        onClick = {
                            showCommentSheet = true
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = SpanStyle(color = Color.Blue)
                                        ) {
                                            append("$commentCount")
                                        }
                                        append(" Kommentare")
                                    },
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showCommentSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showCommentSheet = false
                },
                sheetState = sheetState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(color = Color.Blue)
                            ) {
                                append("$commentCount")
                            }
                            append(" Kommentare")
                        },
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    testComments.forEach { comment ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = comment.content,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Abschnitt ${comment.segmentId} · ${comment.userId}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
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