package com.moco.DBNavigatorAlternative.domain.repository

interface LocationRepository {
    suspend fun getCurrentLocation(): String? // "Ich verspreche, dass diese Funktion einen Text liefert."
}