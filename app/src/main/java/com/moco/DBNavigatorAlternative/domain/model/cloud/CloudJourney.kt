package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * DER HAUPT-ORDNER: Repräsentiert eine komplette Reiseverbindung.
 */
data class CloudJourney(
    val realtimeDataUpdatedAt: Long = 0,
    val legs: List<CloudLeg> = emptyList()
)

/**
 * DIE REGISTERKARTE: Ein einzelner Abschnitt einer Reise.
 * Z.B. die Fahrt von Berlin nach Darmstadt oder ein Fußweg.
 */
data class CloudLeg(
    val tripId: String = "",
    val direction: String = "",
    val line: CloudLine? = null,        // Der Zug-Steckbrief (null bei Fußweg)
    val origin: CloudStation? = null,   // Startpunkt dieses Abschnitts
    val destination: CloudStation? = null, // Zielpunkt dieses Abschnitts
    val departure: String = "",
    val plannedDeparture: String = "",
    val departureDelay: Int? = null,
    val arrival: String = "",
    val plannedArrival: String = "",
    val arrivalDelay: Int? = null,
    val walking: Boolean = false,
    val distance: Int? = null,
    val stopovers: List<CloudStopover> = emptyList() // Liste aller Haltepunkte
)

/**
 * DER ZUG-STECKBRIEF: Infos zur Linie (z.B. S7 oder ICE 572).
 */
data class CloudLine(
    val id: String = "",
    val fahrtNr: String = "",
    val name: String = "",
    val product: String = "",          // z.B. 'suburban' oder 'express'
    val operatorName: String = ""       // z.B. 'S-Bahn Berlin GmbH'
)

/**
 * DER FAHRPLAN-EINTRAG: Ein einzelner Halt an einem Bahnhof während der Fahrt.
 */
data class CloudStopover(
    val stop: CloudStation? = null,
    val arrival: String? = null,
    val plannedArrival: String? = null,
    val departure: String? = null,
    val plannedDeparture: String? = null,
    val cancelled: Boolean = false
)
