package com.moco.DBNavigatorAlternative.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moco.DBNavigatorAlternative.data.local.entity.FavoriteConnectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteConnectionDao {

    @Query("SELECT * FROM favorite_connections ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteConnectionEntity>>

    @Query("SELECT * FROM favorite_connections WHERE connectionId = :connectionId")
    suspend fun getFavoriteByChecksum(connectionId: String): FavoriteConnectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteConnectionEntity)

    @Query("DELETE FROM favorite_connections WHERE connectionId = :connectionId")
    suspend fun deleteFavoriteByChecksum(connectionId: String)

    @Query("DELETE FROM favorite_connections")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_connections WHERE connectionId = :connectionId)")
    fun isFavorite(connectionId: String): Flow<Boolean>
}
