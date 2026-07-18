package com.moco.DBNavigatorAlternative.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repräsentiert einen Kommentar zu einem Bahnhof oder einem spezifischen Gleis.
 */
data class StationComment(
    val commentId: String = "",
    val stationId: String = "",
    val stationName: String = "",
    val platform: String? = null,
    val userId: String = "",
    val username: String = "",
    val content: String = "",
    // Wir nutzen neue Feldnamen, um Konflikte mit alten Cache-Daten zu vermeiden
    val dateText: String = "", 
    val timestamp: Long = 0L // Wir behalten Long für die Sortierung bei
)

/**
 * Repräsentiert eine Bewertung für einen Bahnhof.
 */
data class StationRating(
    val stationId: String = "",
    val userId: String = "",
    val rating: Int = 0,
    val timestamp: Long = Date().time
)

/**
 * Aggregierte Bewertungsinformationen für die Anzeige in der UI.
 */
data class StationRatingSummary(
    val stationId: String = "",
    val averageRating: Float = 0f,
    val reviewCount: Int = 0
)
