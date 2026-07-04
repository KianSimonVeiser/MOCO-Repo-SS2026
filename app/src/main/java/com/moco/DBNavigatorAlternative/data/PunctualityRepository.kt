package com.moco.DBNavigatorAlternative.data

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
 * Kann Daten sowohl lokal berechnen (Fallback) als auch vom Pünktlichkeitsserver abrufen.
 */
class PunctualityRepository {

    // Retrofit-Instanz für den Server-Zugriff
    private val apiService: PunctualityApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // Zugriff auf localhost des PCs vom Emulator aus
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PunctualityApiService::class.java)
    }

    /**
     * Hauptfunktion zum Abrufen der Pünktlichkeitsdaten.
     * In einer Produktiv-App würde hier ein Netzwerkaufruf mit Fehlerbehandlung stehen.
     */
    suspend fun getPunctualityForConnection(connection: Connection): PunctualityInfo {
        return try {
            // Versuche Daten vom Server zu laden (hier noch auskommentiert, da Server-URL fiktiv)
            // val stats = apiService.getConnectionForecast(from = connection.segments.first().departureStop.name, to = connection.segments.last().arrivalStop.name)
            // stats
            
            // Aktueller Fallback: Lokale Berechnung basierend auf historischen Statistiken
            calculatePunctualityLocally(connection)
        } catch (e: Exception) {
            calculatePunctualityLocally(connection)
        }
    }

    /**
     * Lokale Berechnungslogik als Fallback oder für den Offline-Modus.
     * Nutzt die Basis-Statistiken der DB (z.B. Fernverkehr ist unpünktlicher).
     */
    private fun calculatePunctualityLocally(connection: Connection): PunctualityInfo {
        var score = 9.0f
        
        // Malus für Fernverkehr (ICE/IC)
        val hasLongDistance = connection.segments.any { it.train.type == TrainType.ICE || it.train.type == TrainType.IC }
        if (hasLongDistance) score -= 2.0f
        
        // Malus für jeden Umstieg (erhöhtes Verspätungsrisiko)
        score -= (connection.transferCount * 1.5f)
        
        // Berechnung der Bindungsverlust-Wahrscheinlichkeit (> 20 Min Verspätung)
        var lossProb = 0.05f
        if (hasLongDistance) lossProb += 0.15f
        lossProb += (connection.transferCount * 0.12f)
        
        // Falls Segmente bereits Scores haben (aus Mock-Daten), diese einfließen lassen
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
