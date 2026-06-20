package com.moco.DBNavigatorAlternative.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moco.DBNavigatorAlternative.model.Connection
import com.moco.DBNavigatorAlternative.model.ConnectionSegment
import com.moco.DBNavigatorAlternative.model.Stop
import com.moco.DBNavigatorAlternative.model.Train
import com.moco.DBNavigatorAlternative.model.TrainType

private val previewConnection = Connection(
    id = "conn001",
    totalDurationMinutes = 180,
    transferCount = 1,
    segments = listOf(
        ConnectionSegment(
            id = "seg001",
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
            id = "seg002",
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
            id = "seg003",
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
                id = "re21-2",
                type = TrainType.RE,
                line = "RE 21"
            ),
            currentProgress = 0f,
            punctualityScore = 6.0f
        )
    )
)

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        connection = previewConnection
    )
}