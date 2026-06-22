package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moco.DBNavigatorAlternative.domain.model.Comment
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.ConnectionSegment
import com.moco.DBNavigatorAlternative.domain.model.Stop
import com.moco.DBNavigatorAlternative.domain.model.Train
import com.moco.DBNavigatorAlternative.domain.model.TrainType
import com.moco.DBNavigatorAlternative.domain.model.User
 val previewConnection = Connection(  //nicht private weil die navigation zugriff braucht
    id = "conn001",
    totalDurationMinutes = 180,
    transferCount = 1,
    segments = listOf(
        ConnectionSegment(
            id = "cs1",
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
            id = "cs2",
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
            id = "cs3",
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

private val user1 = User(
    "us1",
    "Peter"
)

private val user2 = User(
    "us3",
    "Peter2"
)

val previewCommentList = listOf(
    Comment(
        id = "1",
        user = user1,
        segmentId = "cs1",
        content = "KOmmentarinhalt 1"
    ),
    Comment(
        id = "2",
        user = user2,
        segmentId = "cs2",
        content = "Kommentarinhalt 2"
    )
)

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        connection = previewConnection,
        comments = previewCommentList
    )
}