package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * Modell zur Speicherung von Bahnhofsdaten in Firebase Firestore.
 */
data class CloudStation(
    val id: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)
