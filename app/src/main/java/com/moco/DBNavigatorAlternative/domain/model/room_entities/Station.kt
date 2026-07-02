package com.moco.DBNavigatorAlternative.domain.model.room_entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Definition der Datenbank-Entität für Bahnhöfe (Room).
 */
@Entity(tableName = "stations")
data class Station(
    @PrimaryKey val id: String,
    val name: String,
    val type: String = "stop",
    @Embedded(prefix = "loc_") val location: StationLocation?,
    @Embedded(prefix = "prod_") val products: StationProducts?
)

/**
 * Modell für geographische Koordinaten innerhalb der lokalen Datenbank.
 */
data class StationLocation(
    val latitude: Double,
    val longitude: Double
)

/**
 * Modell für die verfügbaren Verkehrsmittel innerhalb der lokalen Datenbank.
 */
data class StationProducts(
    val suburban: Boolean = false,
    val subway: Boolean = false,
    val tram: Boolean = false,
    val bus: Boolean = false,
    val ferry: Boolean = false,
    val express: Boolean = false,
    val regional: Boolean = false
)
