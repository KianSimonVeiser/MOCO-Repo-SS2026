package com.moco.DBNavigatorAlternative.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repräsentiert einen Kommentar zu einer Verkehrslinie (z.B. Bus 123 oder ICE 549).
 */
data class LineComment(
    val commentId: String = "",
    val lineId: String = "",
    val lineName: String = "",
    val userId: String = "",
    val username: String = "",
    val content: String = "",
    val dateText: String = "", 
    val timestamp: Long = 0L 
)

/**
 * Repräsentiert eine Bewertung für eine Verkehrslinie.
 */
data class LineRating(
    val lineId: String = "",
    val userId: String = "",
    val rating: Int = 0,
    val timestamp: Long = Date().time
)

/**
 * Aggregierte Bewertungsinformationen für die Anzeige in der UI.
 */
data class LineRatingSummary(
    val lineId: String = "",
    val averageRating: Float = 0f,
    val reviewCount: Int = 0
)
