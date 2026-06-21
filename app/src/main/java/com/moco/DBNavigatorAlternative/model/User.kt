package com.moco.DBNavigatorAlternative.model

/**
 * Datenmodell für einen Benutzeraccount.
 */
data class User(
    val userId: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val savedConnections: List<String> = emptyList() // IDs der gespeicherten Verbindungen
)

/**
 * Erweitertes Modell für die Authentifizierung (intern).
 */
data class UserAccount(
    val user: User,
    val passwordHash: String // In einer echten App niemals im Klartext
)
