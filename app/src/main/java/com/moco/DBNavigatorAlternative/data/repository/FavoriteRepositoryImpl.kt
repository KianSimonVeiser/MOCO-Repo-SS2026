package com.moco.DBNavigatorAlternative.data.repository

import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.data.local.dao.FavoriteConnectionDao
import com.moco.DBNavigatorAlternative.data.local.entity.toDomain
import com.moco.DBNavigatorAlternative.data.local.entity.toEntity
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import com.moco.DBNavigatorAlternative.domain.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val dao: FavoriteConnectionDao,
    private val interactionRepository: InteractionRepository = InteractionRepository()
) : FavoriteRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllFavorites(): Flow<List<FavoriteConnection>> {
        return UserRepository.currentUser.flatMapLatest { user ->
            if (user == null) {
                flowOf(emptyList())
            } else {
                dao.getAllFavorites().map { entities ->
                    entities.map { it.toDomain() }
                }
            }
        }
    }

    override suspend fun getFavoriteByChecksum(connectionId: String): FavoriteConnection? {
        return dao.getFavoriteByChecksum(connectionId)?.toDomain()
    }

    override suspend fun insertFavorite(favorite: FavoriteConnection) {
        dao.insertFavorite(favorite.toEntity())
        
        // Remote-Synchronisierung, falls Nutzer eingeloggt ist
        UserRepository.currentUser.value?.let { user ->
            val remoteFavorite = favorite.copy(userId = user.userId)
            interactionRepository.addFavorite(remoteFavorite)
        }
    }

    override suspend fun deleteFavoriteByChecksum(connectionId: String) {
        dao.deleteFavoriteByChecksum(connectionId)
        
        // Remote-Synchronisierung, falls Nutzer eingeloggt ist
        UserRepository.currentUser.value?.let { user ->
            interactionRepository.removeFavorite(user.userId, connectionId)
        }
    }

    override fun isFavorite(connectionId: String): Flow<Boolean> {
        return dao.isFavorite(connectionId)
    }

    override suspend fun syncWithRemote() {
        val user = UserRepository.currentUser.value ?: return
        
        // Erst lokal alles löschen, um Platz für den Account-Sync zu schaffen
        dao.deleteAll()
        
        // Remote Favoriten abrufen (einmalig für Sync)
        val remoteFavorites = interactionRepository.getFavorites(user.userId).firstOrNull() ?: return
        
        // In lokale DB schreiben
        remoteFavorites.forEach { remoteFav ->
            dao.insertFavorite(remoteFav.toEntity())
        }
    }

    override suspend fun clearLocalFavorites() {
        dao.deleteAll()
    }
}
