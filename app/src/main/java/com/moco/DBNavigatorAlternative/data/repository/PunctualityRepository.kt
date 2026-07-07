package com.moco.DBNavigatorAlternative.data.repository

import com.moco.DBNavigatorAlternative.data.api.PunctualityApiService
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.TrainType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.max
import kotlin.math.min

/**
 * Repository zur Steuerung der Pünktlichkeitsdaten.
 * Versucht Echtzeit-Statistiken vom Python-Server zu laden und bietet lokale Fallbacks.
 */
class PunctualityRepository {

    private val apiService: PunctualityApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // FastAPI Standardport ist 8000
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PunctualityApiService::class.java)
    }

    /**
     * Ruft die Pünktlichkeitsdaten für eine Verbindung ab.
     * Sendet alle Zugnummern der Verbindung an den Server für eine kombinierte Vorhersage.
     */
    suspend fun getPunctualityForConnection(connection: Connection): PunctualityInfo {
        return try {
            // Die 'line' enthält bereits das Format "TYP NUMMER" (z.B. "ICE 572")
            val trainIds = connection.segments.map { it.train.line }

            // Server-Anfrage
            apiService.getConnectionForecast(trainIds)
        } catch (e: Exception) {
            // Logge den Fehler (optional) und nutze lokale Logik als Fallback
            calculatePunctualityLocally(connection)
        }
    }

    /**
     * Lokale Berechnungslogik (Fallback).
     * Basierend auf allgemeinen DB-Statistiken.
     */
    private fun calculatePunctualityLocally(connection: Connection): PunctualityInfo {
        var score = 9.0f
        val hasLongDistance = connection.segments.any { it.train.type == TrainType.ICE || it.train.type == TrainType.IC }
        if (hasLongDistance) score -= 2.0f
        score -= (connection.transferCount * 1.5f)

        var lossProb = 0.05f
        if (hasLongDistance) lossProb += 0.15f
        lossProb += (connection.transferCount * 0.12f)

        val segmentScore = connection.segments.mapNotNull { it.punctualityScore }.average()
        if (!segmentScore.isNaN()) {
            score = (score + segmentScore.toFloat()) / 2
        }

        return PunctualityInfo(
            score = max(0.0f, min(10.0f, score)),
            bindingLossProbability = max(0.0f, min(1.0f, lossProb))
        )
    }
}