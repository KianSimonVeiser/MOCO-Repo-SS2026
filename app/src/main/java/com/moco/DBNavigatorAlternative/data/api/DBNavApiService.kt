package com.moco.DBNavigatorAlternative.data.api

import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto

interface DBNavApiService {

    suspend fun getNearbyStations(
        latitude: Double,
        longitude: Double,
        radius: Int = 400,
        maxResults: Int = 8
    ): List<NearbyLocationDto>

    suspend fun getStationsByName(
        name: String
    ): List<String>
}