package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * Repräsentiert einen Nutzer-Kommentar in der Cloud (Firebase).
 */
data class CloudComment(
    val commentId: String = "",       // Eine eindeutige ID für diesen Kommentar
    val userId: String = "",          // Die ID des Nutzers, der den Kommentar verfasst hat
    val username: String = "",        // Der Name des Nutzers (zum schnellen Anzeigen)
    val targetId: String = "",        // ID der Station oder des Zuges, zu dem der Kommentar gehört
    val content: String = "",         // Der eigentliche Text des Kommentars
    val timestamp: Long = System.currentTimeMillis() // Zeitstempel der Erstellung
)
