package com.moco.DBNavigatorAlternative.data.api

import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface für den Pünktlichkeitsserver.
 * Basierend auf den historischen Statistiken der Deutschen Bahn.
 */
interface PunctualityApiService {

    /**
     * Ruft Statistiken für eine bestimmte Zugnummer ab.
     * @param trainId Die Nummer des Zuges (z.B. "ICE 572").
     */
    @GET("statistics")
    suspend fun getTrainStatistics(
        @Query("trainId") trainId: String
    ): PunctualityInfo

    /**
     * Ruft die Pünktlichkeitsvorhersage für eine gesamte Verbindung ab.
     * @param from Startbahnhof
     * @param to Zielbahnhof
     */
    @GET("connection/forecast")
    suspend fun getConnectionForecast(
        @Query("from") from: String,
        @Query("to") to: String
    ): PunctualityInfo
}
