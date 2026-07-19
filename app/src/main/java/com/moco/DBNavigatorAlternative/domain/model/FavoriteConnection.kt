package com.moco.DBNavigatorAlternative.domain.model

/**
 * Modell für eine vom Benutzer favorisierte Verbindung.
 */
data class FavoriteConnection(
    val favoriteId: String = "",
    val userId: String = "",
    val fromStation: String = "",
    val toStation: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
