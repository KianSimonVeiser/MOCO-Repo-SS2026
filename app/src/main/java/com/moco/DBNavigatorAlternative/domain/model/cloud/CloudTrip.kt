package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * Modell zur Speicherung von Fahrtdaten und Verspätungen in Firebase Firestore.
 */
data class CloudTrip(
    val tripId: String = "",
    val lineName: String = "",
    val direction: String = "",
    val plannedDeparture: String = "",
    val actualDeparture: String = "",
    val delayInSeconds: Int = 0,
    val stationId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
