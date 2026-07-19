package com.moco.DBNavigatorAlternative.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class NearbyLocationDto(
    val locationId: String,
    val locationType: String,
    val name: String,
    val coordinates: CoordinatesDto,
    val distance: Int? = null,
    val evaNr: String? = null,
    val products: List<String> = emptyList(),
    val stationId: String? = null,
    val weight: Int? = null
)