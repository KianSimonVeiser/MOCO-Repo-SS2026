package com.moco.DBNavigatorAlternative.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getCurrentLocation(): Location? // "Ich verspreche, dass diese Funktion einen Text liefert."
}