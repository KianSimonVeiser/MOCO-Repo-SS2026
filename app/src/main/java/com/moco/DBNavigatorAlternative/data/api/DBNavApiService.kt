package com.moco.DBNavigatorAlternative.data.api

import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.domain.model.Connection

interface DBNavApiService {

    suspend fun getNearbyStations(
        latitude: Double,
        longitude: Double,
        radius: Int = 400,
        maxResults: Int = 8
    ): List<NearbyLocationDto>

    /**
     * Sucht Stationen nach Namen und gibt vollständige Location-Daten zurück.
     */
    suspend fun getStationsByName(
        name: String
    ): List<NearbyLocationDto>

    /**
     * Ruft Verbindungen zwischen zwei Orten ab.
     * @param fromId Location-ID des Startpunkts
     * @param toId Location-ID des Zielpunkts
     * @param dateTime ISO 8601 Zeitstempel
     */
    suspend fun getConnections(
        fromId: String,
        toId: String,
        dateTime: String
    ): List<Connection>
}
