package com.moco.DBNavigatorAlternative.data.repository

import com.moco.DBNavigatorAlternative.data.local.dao.FavoriteConnectionDao
import com.moco.DBNavigatorAlternative.data.local.entity.toDomain
import com.moco.DBNavigatorAlternative.data.local.entity.toEntity
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import com.moco.DBNavigatorAlternative.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteRepositoryImpl(
    private val dao: FavoriteConnectionDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<FavoriteConnection>> {
        return dao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getFavoriteByChecksum(connectionId: String): FavoriteConnection? {
        return dao.getFavoriteByChecksum(connectionId)?.toDomain()
    }

    override suspend fun insertFavorite(favorite: FavoriteConnection) {
        dao.insertFavorite(favorite.toEntity())
    }

    override suspend fun deleteFavoriteByChecksum(connectionId: String) {
        dao.deleteFavoriteByChecksum(connectionId)
    }

    override fun isFavorite(connectionId: String): Flow<Boolean> {
        return dao.isFavorite(connectionId)
    }
}
