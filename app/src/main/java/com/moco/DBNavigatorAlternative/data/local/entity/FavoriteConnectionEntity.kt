package com.moco.DBNavigatorAlternative.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection

@Entity(tableName = "favorite_connections")
data class FavoriteConnectionEntity(
    @PrimaryKey val connectionId: String,
    val userId: String,
    val fromStation: String,
    val fromId: String,
    val toStation: String,
    val toId: String,
    val lineName: String,
    val departureTime: String,
    val arrivalTime: String,
    val timestamp: Long
)

fun FavoriteConnectionEntity.toDomain(): FavoriteConnection {
    return FavoriteConnection(
        connectionId = connectionId,
        userId = userId,
        fromStation = fromStation,
        fromId = fromId,
        toStation = toStation,
        toId = toId,
        lineName = lineName,
        departureTime = departureTime,
        arrivalTime = arrivalTime,
        timestamp = timestamp
    )
}

fun FavoriteConnection.toEntity(): FavoriteConnectionEntity {
    return FavoriteConnectionEntity(
        connectionId = connectionId,
        userId = userId,
        fromStation = fromStation,
        fromId = fromId,
        toStation = toStation,
        toId = toId,
        lineName = lineName,
        departureTime = departureTime,
        arrivalTime = arrivalTime,
        timestamp = timestamp
    )
}
