package com.moco.DBNavigatorAlternative.domain.model

/**
 * Datenmodell für einen Benutzeraccount.
 * Repräsentiert die öffentlichen und profilrelevanten Informationen eines Nutzers.
 * 
 * @property userId Eindeutige Kennung des Benutzers.
 * @property username Der vom Benutzer gewählte Anzeigename.
 * @property email Die verknüpfte E-Mail-Adresse des Benutzers.
 * @property profilePictureUrl Optionale URL zu einem Profilbild des Benutzers.
 * @property savedConnections Eine Liste von IDs, die auf vom Benutzer gespeicherte Zugverbindungen verweisen.
 */
data class User(
    val userId: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null,
    val savedConnections: List<String> = emptyList()
)

/**
 * Erweitertes Modell für die Authentifizierung und Account-Verwaltung.
 * Trennt sensible Daten wie Passwörter von den öffentlichen Profilinformationen.
 * 
 * @property user Die zugehörigen Profilinformationen (siehe [User]).
 * @property passwordHash Der kryptografische Hash des Passworts für die Authentifizierung.
 *                       (Hinweis: In einer Produktivumgebung niemals Passwörter im Klartext speichern).
 */
data class UserAccount(
    val user: User,
    val passwordHash: String
)
