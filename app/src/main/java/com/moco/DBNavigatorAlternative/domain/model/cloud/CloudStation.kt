package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * Repräsentiert eine Haltestelle in der Firebase Cloud (Firestore).
 *
 * WICHTIG FÜR DIE PRÜFUNG:
 * 1. Wir nutzen Standardwerte (z.B. = ""), weil Firebase einen leeren Konstruktor braucht.
 */
data class CloudStation(
    val id: String = "",              // Die Bahnhofs-ID der Deutschen Bahn
    val name: String = "",            // Der Name des Bahnhofs
    val latitude: Double = 0.0,       // Breitengrad für Berechnungen
    val longitude: Double = 0.0,      // Längengrad für Berechnungen
    val lastSyncTimestamp: Long = System.currentTimeMillis() // Wann wurde die Kopie erstellt?
)
