package com.moco.DBNavigatorAlternative.presentation.search

import com.moco.DBNavigatorAlternative.domain.model.*

/**
 * UI-spezifische Erweiterungen für das zentrale Modell aus TrainConnection.kt.
 */

/**
 * Liefert eine Liste von Beispiel-Verbindungen für die Anzeige in Previews und Tests.
 * Die Daten sind an die Mock-Daten der Detailansicht angelehnt.
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
                    departureStop = Stop("FFM", "Frankfurt Hbf", "14:30", "7"),
                    arrivalStop = Stop("KAS", "Kassel-Wilhelmshöhe", "15:50", "3"),
                    train = Train("ice572", TrainType.ICE, "ICE 572"),
                    currentProgress = 1f,
                    punctualityScore = 9.4f
                ),
                ConnectionSegment(
                    id = "seg2",
                    departureStop = Stop("KAS", "Kassel-Wilhelmshöhe", "16:00", "5"),
                    arrivalStop = Stop("BER", "Berlin Hbf", "17:30", "11"),
                    train = Train("re21", TrainType.RE, "RE 21"),
                    currentProgress = 0.9f
                )
            )
        ),
        Connection(
            id = "conn002",
            totalDurationMinutes = 120,
            transferCount = 0,
            segments = listOf(
                ConnectionSegment(
                    id = "seg3",
                    departureStop = Stop("MUEN", "München Hbf", "09:00", "12"),
                    arrivalStop = Stop("STGT", "Stuttgart Hbf", "11:00", "4"),
                    train = Train("ice123", TrainType.ICE, "ICE 123"),
                    currentProgress = 0.5f,
                    punctualityScore = 3.2f
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
                    departureStop = Stop("HAM", "Hamburg Hbf", "18:15", "5"),
                    arrivalStop = Stop("LUEB", "Lübeck Hbf", "19:00", "2"),
                    train = Train("re8", TrainType.RE, "RE 8"),
                    currentProgress = 0f,
                    punctualityScore = 5.5f
                )
            )
        )
    )
}
