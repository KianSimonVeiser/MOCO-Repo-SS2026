package com.moco.DBNavigatorAlternative.presentation.search

import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.ConnectionSegment
import com.moco.DBNavigatorAlternative.domain.model.Stop
import com.moco.DBNavigatorAlternative.domain.model.Train
import com.moco.DBNavigatorAlternative.domain.model.TrainType

/**
 * Liefert eine Liste von Beispiel-Verbindungen für Previews und Tests.
 */
fun getMockConnections(): List<Connection> {
    return listOf(
        Connection(
            id = "conn001",
            totalDurationMinutes = 180,
            transferCount = 1,
            segments = listOf(
                ConnectionSegment(
                    id = "seg1",
                    departureStop = Stop(
                        "FFM",
                        "Frankfurt Hbf",
                        "14:30",
                        "7"
                    ),
                    arrivalStop = Stop(
                        "KAS",
                        "Kassel-Wilhelmshöhe",
                        "15:50",
                        "3"
                    ),
                    train = Train(
                        "ice572",
                        TrainType.ICE,
                        "ICE 572"
                    ),
                    currentProgress = 1f,
                    punctualityScore = 9.4f
                ),
                ConnectionSegment(
                    id = "seg2",
                    departureStop = Stop(
                        "KAS",
                        "Kassel-Wilhelmshöhe",
                        "16:00",
                        "5"
                    ),
                    arrivalStop = Stop(
                        "BER",
                        "Berlin Hbf",
                        "17:30",
                        "11"
                    ),
                    train = Train(
                        "re21",
                        TrainType.RE,
                        "RE 21"
                    ),
                    currentProgress = 0.9f
                )
            )
        ),

        Connection(
            id = "conn002",
            totalDurationMinutes = 90,
            transferCount = 0,
            segments = listOf(
                ConnectionSegment(
                    id = "seg3",
                    departureStop = Stop(
                        "ROTT",
                        "Rottweil",
                        "05:30",
                        "1"
                    ),
                    arrivalStop = Stop(
                        "STGT",
                        "Stuttgart Hbf",
                        "07:00",
                        "4"
                    ),
                    train = Train(
                        "ic2388",
                        TrainType.IC,
                        "IC 2388"
                    ),
                    currentProgress = 0.5f,
                    punctualityScore = 9.9f
                )
            )
        ),

        Connection(
            id = "conn003",
            totalDurationMinutes = 45,
            transferCount = 0,
            segments = listOf(
                ConnectionSegment(
                    id = "seg4",
                    departureStop = Stop(
                        "HAM",
                        "Hamburg Hbf",
                        "18:15",
                        "5"
                    ),
                    arrivalStop = Stop(
                        "LUEB",
                        "Lübeck Hbf",
                        "19:00",
                        "2"
                    ),
                    train = Train(
                        "re8",
                        TrainType.RE,
                        "RE 8"
                    ),
                    currentProgress = 0f,
                    punctualityScore = 5.5f
                )
            )
        ),

        Connection(
            id = "conn004",
            totalDurationMinutes = 295,
            transferCount = 0,
            segments = listOf(
                ConnectionSegment(
                    id = "seg5",
                    departureStop = Stop(
                        "KOELN",
                        "Köln Hbf",
                        "05:26",
                        "4"
                    ),
                    arrivalStop = Stop(
                        "BERO",
                        "Berlin Ostbahnhof",
                        "10:21",
                        "2"
                    ),
                    train = Train(
                        "ice641",
                        TrainType.ICE,
                        "ICE 641"
                    ),
                    currentProgress = 0.3f,
                    punctualityScore = 1.3f
                )
            )
        )
    )
}