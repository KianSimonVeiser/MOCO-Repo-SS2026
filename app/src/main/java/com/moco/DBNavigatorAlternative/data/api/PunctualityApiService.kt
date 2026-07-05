package com.moco.DBNavigatorAlternative.data.api

import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface für den Pünktlichkeitsserver.
 * Kommuniziert mit dem Python-Backend (FastAPI + DuckDB).
 */
interface PunctualityApiService {

    /**
     * Ruft Statistiken für einen spezifischen Zug ab.
     * @param trainType Typ des Zuges (z.B. "ICE")
     * @param trainNumber Nummer des Zuges (z.B. "572")
     */
    @GET("statistics/train")
    suspend fun getTrainStatistics(
        @Query("type") trainType: String,
        @Query("number") trainNumber: String
    ): PunctualityInfo

    /**
     * Ruft die Pünktlichkeitsvorhersage für eine gesamte Verbindung ab.
     * Der Server berechnet dies basierend auf den übergebenen Zugnummern.
     * @param trainIds Liste von "Typ Nummer" (z.B. ["ICE 572", "RE 21"])
     */
    @GET("statistics/connection")
    suspend fun getConnectionForecast(
        @Query("trains") trainIds: List<String>
    ): PunctualityInfo
}
