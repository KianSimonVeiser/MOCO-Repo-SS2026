package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.ui.graphics.Color

data class TrainData(
    val name: String,
    val color: Color
)

data class ConnectionData(
    val depTime: String,
    val arrTime: String,
    val score: Double,
    val trains: List<TrainData>,
    val showBindingHint: Boolean = false,
    val isLate: Boolean = false
)

val previewConnections = listOf(
    ConnectionData(
        depTime = "MM:HH",
        arrTime = "MM:HH",
        score = 9.4,
        trains = listOf(
            TrainData(
                name = "RB1",
                color = Color(0xFFE2104E)
            ),
            TrainData(
                name = "ICE 2",
                color = Color.Gray
            )
        )
    ),
    ConnectionData(
        depTime = "MM:HH",
        arrTime = "MM:HH",
        score = 2.3,
        trains = listOf(
            TrainData(
                name = "RB2",
                color = Color(0xFFE2104E)
            ),
            TrainData(
                name = "ICE 3",
                color = Color.Gray
            ),
            TrainData(
                name = "IC1",
                color = Color(0xFF009FE3)
            )
        ),
        isLate = true
    ),
    ConnectionData(
        depTime = "MM:HH",
        arrTime = "MM:HH",
        score = 5.4,
        trains = listOf(
            TrainData(
                name = "RB2",
                color = Color(0xFFE2104E)
            ),
            TrainData(
                name = "ICE 5",
                color = Color.Gray
            ),
            TrainData(
                name = "SB 3",
                color = Color(0xFF951A81)
            )
        ),
        showBindingHint = true
    )
)