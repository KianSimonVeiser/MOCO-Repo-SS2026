package com.moco.DBNavigatorAlternative.data.local.dao

import com.moco.DBNavigatorAlternative.data.local.entity.FavoriteConnectionEntity
import com.moco.DBNavigatorAlternative.data.local.entity.toDomain
import com.moco.DBNavigatorAlternative.data.local.entity.toEntity
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteConnectionMapperTest {

    @Test
    fun `map entity to domain`() {
        val entity = FavoriteConnectionEntity(
            connectionId = "check123",
            userId = "user1",
            fromStation = "Berlin",
            fromId = "8011160",
            toStation = "Munich",
            toId = "8000261",
            lineName = "ICE 1",
            departureTime = "10:00",
            arrivalTime = "14:00",
            timestamp = 123456789L
        )
        val domain = entity.toDomain()

        assertEquals(entity.connectionId, domain.connectionId)
        assertEquals(entity.userId, domain.userId)
        assertEquals(entity.fromStation, domain.fromStation)
        assertEquals(entity.fromId, domain.fromId)
        assertEquals(entity.toStation, domain.toStation)
        assertEquals(entity.toId, domain.toId)
        assertEquals(entity.lineName, domain.lineName)
        assertEquals(entity.departureTime, domain.departureTime)
        assertEquals(entity.arrivalTime, domain.arrivalTime)
        assertEquals(entity.timestamp, domain.timestamp)
    }

    @Test
    fun `map domain to entity`() {
        val domain = FavoriteConnection(
            connectionId = "check123",
            userId = "user1",
            fromStation = "Berlin",
            fromId = "8011160",
            toStation = "Munich",
            toId = "8000261",
            lineName = "ICE 1",
            departureTime = "10:00",
            arrivalTime = "14:00",
            timestamp = 123456789L
        )
        val entity = domain.toEntity()

        assertEquals(domain.connectionId, entity.connectionId)
        assertEquals(domain.userId, entity.userId)
        assertEquals(domain.fromStation, entity.fromStation)
        assertEquals(domain.fromId, entity.fromId)
        assertEquals(domain.toStation, entity.toStation)
        assertEquals(domain.toId, entity.toId)
        assertEquals(domain.lineName, entity.lineName)
        assertEquals(domain.departureTime, entity.departureTime)
        assertEquals(domain.arrivalTime, entity.arrivalTime)
        assertEquals(domain.timestamp, entity.timestamp)
    }
}
