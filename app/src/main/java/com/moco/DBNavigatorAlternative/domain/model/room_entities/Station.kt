package com.moco.DBNavigatorAlternative.domain.model.room_entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Die Akte für einen Bahnhof in unserer Datenbank.
 * @Entity sagt Room: "Leg dafür eine Tabelle namens stations an."
 */
@Entity(tableName = "stations")
data class Station(
    @PrimaryKey val id: String,         // Die eindeutige Nummer (z.B. '900000100003')
    val name: String,                   // Der Name (z.B. 'S+U Alexanderplatz')
    val type: String = "stop",          // Typ der Station
    @Embedded(prefix = "loc_") val location: StationLocation, // Unter-Ordner für Koordinaten
    @Embedded(prefix = "prod_") val products: StationProducts // Unter-Ordner für Verkehrsmittel
)

/**
 * Die Koordinaten eines Bahnhofs.
 */
data class StationLocation(
    val latitude: Double,
    val longitude: Double
)

/**
 * Die verfügbaren Verkehrsmittel an einem Bahnhof.
 */
data class StationProducts(
    val suburban: Boolean, // S-Bahn
    val subway: Boolean,   // U-Bahn
    val tram: Boolean,     // Straßenbahn
    val bus: Boolean,      // Bus
    val ferry: Boolean,    // Fähre
    val express: Boolean,  // Fernverkehr (ICE/IC)
    val regional: Boolean  // Regionalverkehr (RE/RB)
)

