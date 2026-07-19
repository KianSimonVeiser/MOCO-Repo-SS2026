package com.moco.DBNavigatorAlternative.data.remote

import android.util.Log
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.api.dto.CoordinatesDto
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyAreaDto
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class NearbyStationsRemoteImpl(
    private val client: HttpClient
) : DBNavApiService {

    private val cleanClient = HttpClient()

    companion object {
        private const val TAG = "NearbyStationsRemote"

        private const val NEARBY_URL =
            "https://app.services-bahn.de/mob/location/nearby"

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

        /*
         * DTO ausdrücklich selbst in JSON umwandeln.
         * Damit wissen wir exakt, was gesendet wird.
         */
        val requestJson = json.encodeToString(requestBody)
        val bodyBytes = requestJson.toByteArray(Charsets.UTF_8)

        Log.d(TAG, "Starte Nearby-Request")
        Log.d(TAG, "URL: $NEARBY_URL")
        Log.d(TAG, "Methode: POST")
        Log.d(TAG, "Correlation-ID: $correlationId")
        Log.d(TAG, "Request-Body: $requestJson")
        Log.d(TAG, "Body-Länge: ${bodyBytes.size} Bytes")

        val response = cleanClient.post(NEARBY_URL) {
            method = HttpMethod.Post
            
            headers[HttpHeaders.Accept] = DB_CONTENT_TYPE
            headers[HttpHeaders.UserAgent] = "curl/8.18.0"
            headers["X-Correlation-ID"] = correlationId
            headers[HttpHeaders.CacheControl] = "no-cache"
            
            setBody(object : OutgoingContent.ByteArrayContent() {
                override val contentType: ContentType = ContentType.parse(DB_CONTENT_TYPE)
                override val contentLength: Long = bodyBytes.size.toLong()
                override fun bytes(): ByteArray = bodyBytes
            })
        }

        val responseText = response.bodyAsText()

        Log.d(TAG, "HTTP-Status: ${response.status}")
        Log.d(TAG, "Response Content-Type: ${response.contentType()}")
        Log.d(TAG, "Response-Body: $responseText")

        if (!response.status.isSuccess()) {
            throw IllegalStateException(
                "Nearby-Request fehlgeschlagen: " +
                        "${response.status}; Body: $responseText"
            )
        }

        /*
         * Auch die Antwort zunächst selbst deserialisieren.
         * Dadurch umgehen wir mögliche Probleme bei der Erkennung
         * des speziellen DB-Content-Types.
         */
        val locations =
            json.decodeFromString<List<NearbyLocationDto>>(
                responseText
            )

        Log.d(TAG, "Stationen erfolgreich geladen: ${locations.size}")

        locations.forEachIndexed { index, location ->
            Log.d(
                TAG,
                "Station $index: " +
                        "name=${location.name}, " +
                        "distance=${location.distance}"
            )
        }

        return locations
    }

    private fun createCorrelationId(): String {
        return "${UUID.randomUUID()}_${UUID.randomUUID()}"
    }
}