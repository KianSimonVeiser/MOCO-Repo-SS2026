package com.moco.DBNavigatorAlternative.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moco.DBNavigatorAlternative.domain.model.room_entities.Station
import kotlinx.coroutines.flow.Flow

/**
 * Hier definieren wir alle Befehle für die Datenbank.
 */
@Dao
interface StationDao {

    /**
     * Speichere einen Bahnhof.
     * 'OnConflictStrategy.REPLACE' sagt: Wenn der Bahnhof schon da ist,
     * überschreibe ihn einfach mit den neuesten Infos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station:Station)

    /**

     * 'Flow' ist wie ein Live-Ticker: Wenn sich in der Datenbank etwas ändert,
     * bekommt die UI sofort automatisch Bescheid.
     */
    @Query("SELECT * FROM stations")
    fun getAllStations(): Flow<List<Station>>

    /**
     * Lösche einen bestimmten Bahnhof anhand seiner Nummer (ID).
     */
    @Query("DELETE FROM stations WHERE id = :stationId")
    suspend fun deleteStationById(stationId: String)
}