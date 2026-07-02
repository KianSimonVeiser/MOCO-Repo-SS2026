package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * Erweiterte Modelle für Reiseverbindungen in der Cloud.
 */
data class CloudJourney(
    val realtimeDataUpdatedAt: Long = 0,
    val legs: List<CloudLeg> = emptyList()
)

data class CloudLeg(
    val tripId: String = "",
    val direction: String = "",
    val line: CloudLine? = null,
    val origin: CloudStation? = null,
    val destination: CloudStation? = null,
    val departure: String = "",
    val plannedDeparture: String = "",
    val departureDelay: Int? = null,
    val arrival: String = "",
    val plannedArrival: String = "",
    val arrivalDelay: Int? = null,
    val walking: Boolean = false,
    val distance: Int? = null,
    val stopovers: List<CloudStopover> = emptyList()
)

data class CloudLine(
    val id: String = "",
    val fahrtNr: String = "",
    val name: String = "",
    val product: String = "",
    val operatorName: String = ""
)

data class CloudStopover(
    val stop: CloudStation? = null,
    val arrival: String? = null,
    val plannedArrival: String? = null,
    val departure: String? = null,
    val plannedDeparture: String? = null,
    val cancelled: Boolean = false
)
