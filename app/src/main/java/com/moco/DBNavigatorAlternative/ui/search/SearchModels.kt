package com.moco.DBNavigatorAlternative.ui.search

import com.moco.DBNavigatorAlternative.model.*

/**
 * UI-spezifische Erweiterungen für das zentrale Modell aus TrainConnection.kt.
 * Diese Properties berechnen Werte für die Anzeige in der UI.
 */

/**
 * Liefert den Pünktlichkeits-Score der Verbindung basierend auf dem ersten Segment.
 */
val Connection.displayScore: Double
    get() = segments.firstOrNull()?.punctualityScore?.toDouble() ?: 0.0

/**
 * Bestimmt, ob die Verbindung als verspätet/kritisch markiert werden soll (Score < 4.0).
 */
val Connection.isLate: Boolean
    get() = displayScore < 4.0

/**
 * Bestimmt, ob ein Hinweis zur aufgehobenen Zugbindung angezeigt werden soll.
 */
val Connection.shouldShowBindingHint: Boolean
    get() = displayScore in 5.0..6.0

/**
 * Liefert eine Liste von Beispiel-Verbindungen für die Anzeige in Previews und Tests.
 */
fun getMockConnections(): List<Connection> {
    return listOf(
        createMockConnection("1", "RB1", TrainType.RB, "ICE 2", TrainType.ICE, 9.4f),
        createMockConnection("2", "RB2", TrainType.RB, "ICE 3", TrainType.ICE, 2.3f),
        createMockConnection("3", "RB2", TrainType.RB, "ICE 5", TrainType.ICE, 5.4f)
    )
}

/**
 * Hilfsfunktion zur Erstellung einer Mock-Verbindung mit zwei Segmenten.
 */
private fun createMockConnection(id: String, l1: String, t1: TrainType, l2: String, t2: TrainType, score: Float): Connection {
    val segments = listOf(
        ConnectionSegment("s1", Stop("st1", "A", "10:00"), Stop("st2", "B", "11:00"), Train("t1", t1, l1), 0f, score),
        ConnectionSegment("s2", Stop("st2", "B", "11:15"), Stop("st3", "C", "12:30"), Train("t2", t2, l2), 0f, score)
    )
    return Connection(id, segments, 150, 1)
}
