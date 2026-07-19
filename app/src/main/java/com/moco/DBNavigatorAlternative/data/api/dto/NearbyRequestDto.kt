package com.moco.DBNavigatorAlternative.data.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class NearbyRequestDto(
    val area: NearbyAreaDto,
    val maxResults: Int = 8,
    val products: List<String> = listOf("ALL")
)

@Serializable
data class NearbyAreaDto(
    val coordinates: CoordinatesDto,
    val radius: Int = 400
)

@Serializable
data class CoordinatesDto(
    val longitude: Double,
    val latitude: Double
)