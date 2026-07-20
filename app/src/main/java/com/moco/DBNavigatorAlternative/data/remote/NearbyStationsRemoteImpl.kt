package com.moco.DBNavigatorAlternative.data.remote

import android.util.Log
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.api.dto.CoordinatesDto
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyAreaDto
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class NearbyStationsRemoteImpl(
    private val client: HttpClient
) : DBNavApiService {

    /*
     * Wir nutzen einen frischen Client ohne Plugins (wie ContentNegotiation),
     * da die DB-API extrem empfindlich auf zusätzliche Header (wie Accept: application/json)
     * reagiert und sonst mit 405 Method Not Allowed antwortet.
     */
    private val cleanClient = HttpClient()

    companion object {
        private const val TAG = "NearbyStationsRemote"

        private const val NEARBY_URL =
            "https://app.services-bahn.de/mob/location/nearby"

        private const val SEARCH_URL =
            "https://app.services-bahn.de/mob/location/search"

        private const val DB_CONTENT_TYPE =
            "application/x.db.vendo.mob.location.v3+json"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    override suspend fun getNearbyStations(
        latitude: Double,
        longitude: Double,
        radius: Int,
        maxResults: Int
    ): List<NearbyLocationDto> {

        val correlationId = createCorrelationId()

        val requestBody = NearbyRequestDto(
            area = NearbyAreaDto(
                coordinates = CoordinatesDto(
                    longitude = longitude,
                    latitude = latitude
                ),
                radius = radius
            ),
            maxResults = maxResults,
            products = listOf(
                "HOCHGESCHWINDIGKEITSZUEGE",
                "INTERCITYUNDEUROCITYZUEGE",
                "INTERREGIOUNDSCHNELLZUEGE",
                "NAHVERKEHRSONSTIGEZUEGE",
                "SBAHNEN",
                "BUSSE",
                "SCHIFFE",
                "UBAHN",
                "STRASSENBAHN",
                "ANRUFPFLICHTIGEVERKEHRE"
            )
        )

        val requestJson = json.encodeToString(requestBody)
        val bodyBytes = requestJson.toByteArray(Charsets.UTF_8)

        Log.d(TAG, "Starte Nearby-Request")

        val response = cleanClient.post(NEARBY_URL) {
            method = HttpMethod.Post

            headers[HttpHeaders.Accept] = DB_CONTENT_TYPE
            headers[HttpHeaders.UserAgent] = "curl/8.18.0"
            headers["X-Correlation-ID"] = correlationId
            headers[HttpHeaders.CacheControl] = "no-cache"

            setBody(
                object : OutgoingContent.ByteArrayContent() {
                    override val contentType: ContentType =
                        ContentType.parse(DB_CONTENT_TYPE)
                    override val contentLength: Long =
                        bodyBytes.size.toLong()
                    override fun bytes(): ByteArray =
                        bodyBytes
                }
            )
        }

        val responseText = response.bodyAsText()

        Log.d(TAG, "HTTP-Status: ${response.status}")

        if (!response.status.isSuccess()) {
            Log.e(TAG, "Nearby-Request fehlgeschlagen: ${response.status} - $responseText")
            return emptyList()
        }

        return try {
            json.decodeFromString<List<NearbyLocationDto>>(responseText)
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Dekodieren der NearbyStations", e)
            emptyList()
        }
    }

    override suspend fun getStationsByName(name: String): List<String> {
        if (name.isBlank()) return emptyList()

        val correlationId = createCorrelationId()
        val requestBody = SearchItemsRequest(searchTerm = name.trim())
        val requestJson = json.encodeToString(requestBody)
        val bodyBytes = requestJson.toByteArray(Charsets.UTF_8)

        Log.d(TAG, "Starte Suche für: $name")

        val response = cleanClient.post(SEARCH_URL) {
            method = HttpMethod.Post

            headers[HttpHeaders.Accept] = DB_CONTENT_TYPE
            headers[HttpHeaders.UserAgent] = "curl/8.18.0"
            headers["X-Correlation-ID"] = correlationId
            headers[HttpHeaders.CacheControl] = "no-cache"

            setBody(
                object : OutgoingContent.ByteArrayContent() {
                    override val contentType: ContentType =
                        ContentType.parse(DB_CONTENT_TYPE)
                    override val contentLength: Long =
                        bodyBytes.size.toLong()
                    override fun bytes(): ByteArray =
                        bodyBytes
                }
            )
        }

        val responseText = response.bodyAsText()

        if (!response.status.isSuccess()) {
            Log.e(TAG, "Suche fehlgeschlagen: ${response.status} - $responseText")
            return emptyList()
        }

        return try {
            val locations = json.decodeFromString<List<NearbyLocationDto>>(responseText)
            locations.map { it.name }
        } catch (e: Exception) {
            Log.e(TAG, "Fehler beim Dekodieren der Suche", e)
            emptyList()
        }
    }

    private fun createCorrelationId(): String {
        return "${UUID.randomUUID()}_${UUID.randomUUID()}"
    }

    @Serializable
    private data class SearchItemsRequest(
        val locationTypes: List<String> = listOf("ST"),
        val searchTerm: String,
        val maxResults: Int = 10
    )
}
