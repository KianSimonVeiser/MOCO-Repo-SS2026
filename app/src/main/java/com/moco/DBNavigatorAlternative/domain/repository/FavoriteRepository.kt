package com.moco.DBNavigatorAlternative.domain.repository

import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<FavoriteConnection>>
    suspend fun getFavoriteByChecksum(connectionId: String): FavoriteConnection?
    suspend fun insertFavorite(favorite: FavoriteConnection)
    suspend fun deleteFavoriteByChecksum(connectionId: String)
    fun isFavorite(connectionId: String): Flow<Boolean>
    suspend fun syncWithRemote()
    suspend fun clearLocalFavorites()
}
