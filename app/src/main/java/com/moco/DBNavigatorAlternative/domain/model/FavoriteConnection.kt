package com.moco.DBNavigatorAlternative.domain.model

/**
 * Modell für eine vom Benutzer favorisierte Verbindung.
 * Speichert nun die exakten Daten einer spezifischen Reise.
 */
data class FavoriteConnection(
    val connectionId: String = "",    // Der eindeutige Checksum/ID der API
    val userId: String = "",          // Verknüpfung zum Nutzer
    val fromStation: String = "",
    val toStation: String = "",
    val lineName: String = "",        // Z.B. "ICE 572"
    val departureTime: String = "",   // Z.B. "14:30"
    val arrivalTime: String = "",     // Z.B. "15:50"
    val timestamp: Long = System.currentTimeMillis() // Sortierkriterium
)
