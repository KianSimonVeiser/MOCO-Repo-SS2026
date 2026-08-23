    package com.moco.DBNavigatorAlternative.data.remote
    
    import android.util.Log
    import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
    import com.moco.DBNavigatorAlternative.data.api.dto.*
    import com.moco.DBNavigatorAlternative.domain.model.*
    import io.ktor.client.HttpClient
    import io.ktor.client.request.post
    import io.ktor.client.request.setBody
    import io.ktor.client.statement.bodyAsText
    import io.ktor.http.ContentType
    import io.ktor.http.HttpHeaders
    import io.ktor.http.HttpMethod
    import io.ktor.http.content.OutgoingContent
    import io.ktor.http.isSuccess
    import kotlinx.serialization.Serializable
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
    
            private const val SEARCH_URL =
                "https://app.services-bahn.de/mob/location/search"
                
            private const val JOURNEYS_URL =
                "https://app.services-bahn.de/mob/angebote/fahrplan"
    
            private const val DB_CONTENT_TYPE =
                "application/x.db.vendo.mob.location.v3+json"
                
            private const val DB_JOURNEY_CONTENT_TYPE =
                "application/x.db.vendo.mob.verbindungssuche.v9+json"
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
    
            val response = cleanClient.post(NEARBY_URL) {
                method = HttpMethod.Post
                headers[HttpHeaders.Accept] = DB_CONTENT_TYPE
                headers[HttpHeaders.UserAgent] = "okhttp/4.12.0"
                headers["X-Correlation-ID"] = correlationId
                headers[HttpHeaders.CacheControl] = "no-cache"
    
                setBody(object : OutgoingContent.ByteArrayContent() {
                    override val contentType: ContentType = ContentType.parse(DB_CONTENT_TYPE)
                    override val contentLength: Long = bodyBytes.size.toLong()
                    override fun bytes(): ByteArray = bodyBytes
                })
            }
    
            val responseText = response.bodyAsText()
            if (!response.status.isSuccess()) return emptyList()
    
            return try {
                json.decodeFromString<List<NearbyLocationDto>>(responseText)
            } catch (e: Exception) {
                emptyList()
            }
        }
    
        override suspend fun getStationsByName(name: String): List<NearbyLocationDto> {
            if (name.isBlank()) return emptyList()
    
            val correlationId = createCorrelationId()
            val requestBody = SearchItemsRequest(searchTerm = name.trim())
            val requestJson = json.encodeToString(requestBody)
            val bodyBytes = requestJson.toByteArray(Charsets.UTF_8)
    
            val response = cleanClient.post(SEARCH_URL) {
                method = HttpMethod.Post
                headers[HttpHeaders.Accept] = DB_CONTENT_TYPE
                headers[HttpHeaders.UserAgent] = "okhttp/4.12.0"
                headers["X-Correlation-ID"] = correlationId
                headers[HttpHeaders.CacheControl] = "no-cache"
    
                setBody(object : OutgoingContent.ByteArrayContent() {
                    override val contentType: ContentType = ContentType.parse(DB_CONTENT_TYPE)
                    override val contentLength: Long = bodyBytes.size.toLong()
                    override fun bytes(): ByteArray = bodyBytes
                })
            }
    
            val responseText = response.bodyAsText()
            if (!response.status.isSuccess()) return emptyList()
    
            return try {
                json.decodeFromString<List<NearbyLocationDto>>(responseText)
            } catch (e: Exception) {
                emptyList()
            }
        }
    
        override suspend fun getConnections(
            fromId: String,
            toId: String,
            dateTime: String,
            onlyDTicket: Boolean
        ): List<Connection> {
            val correlationId = createCorrelationId()
            
            val requestBody = JourneyRequestDto(
                fahrverguenstigungen = if (onlyDTicket) {
                    FahrverguenstigungenDto(
                        deutschlandTicketVorhanden = true,
                        nurDeutschlandTicketVerbindungen = true
                    )
                } else null,
                reiseHin = ReiseHinRequestDto(
                    wunsch = WunschDto(
                        abgangsLocationId = fromId,
                        zielLocationId = toId,
                        verkehrsmittel = listOf("ALL"),
                        zeitWunsch = ZeitWunschDto(
                            reiseDatum = dateTime,
                            zeitPunktArt = "ABFAHRT"
                        )
                    )
                )
            )
            
            val requestJson = json.encodeToString(requestBody)
            val bodyBytes = requestJson.toByteArray(Charsets.UTF_8)
    
            val response = cleanClient.post(JOURNEYS_URL) {
                method = HttpMethod.Post
                headers[HttpHeaders.Accept] = DB_JOURNEY_CONTENT_TYPE
                headers[HttpHeaders.UserAgent] = "okhttp/4.12.0"
                headers["X-Correlation-ID"] = correlationId
                headers["x-feature-reiseketten-enabled"] = "false"
    
                setBody(object : OutgoingContent.ByteArrayContent() {
                    override val contentType: ContentType = ContentType.parse(DB_JOURNEY_CONTENT_TYPE)
                    override val contentLength: Long = bodyBytes.size.toLong()
                    override fun bytes(): ByteArray = bodyBytes
                })
            }
    
            val responseText = response.bodyAsText()
            if (!response.status.isSuccess()) {
                Log.e(TAG, "Journeys failed: ${response.status} - $responseText")
                return emptyList()
            }
    
            return try {
                val responseDto = json.decodeFromString<JourneyResponseDto>(responseText)
                mapToDomainConnections(responseDto, onlyDTicket)
            } catch (e: Exception) {
                Log.e(TAG, "Mapping journeys failed", e)
                emptyList()
            }
        }
    
        private fun mapToDomainConnections(dto: JourneyResponseDto, onlyDTicket: Boolean): List<Connection> {
            val mapped = dto.verbindungen.mapIndexed { index, container ->
                val v = container.verbindung
                Connection(
                    id = v.checksum ?: "conn_$index",
                    totalDurationMinutes = (v.reiseDauer / 60).toInt(),
                    transferCount = v.umstiegeAnzahl,
                    segments = v.verbindungsAbschnitte.map { abschnitt ->
                        ConnectionSegment(
                            id = UUID.randomUUID().toString(),
                            departureStop = Stop(
                                id = abschnitt.abgangsOrt.evaNr ?: "",
                                name = abschnitt.abgangsOrt.name,
                                time = extractTime(abschnitt.abgangsDatum),
                                platform = abschnitt.abgangsOrt.plattform ?: ""
                            ),
                            arrivalStop = Stop(
                                id = abschnitt.ankunftsOrt.evaNr ?: "",
                                name = abschnitt.ankunftsOrt.name,
                                time = extractTime(abschnitt.ankunftsDatum),
                                platform = abschnitt.ankunftsOrt.plattform ?: ""
                            ),
                            train = Train(
                                id = abschnitt.zugNummer ?: UUID.randomUUID().toString(),
                                type = if (abschnitt.typ?.uppercase() == "FUSSWEG" || abschnitt.typ?.uppercase() == "WALK") {
                                    TrainType.WALK
                                } else {
                                    mapToTrainType(abschnitt.produktGattung)
                                },
                                line = abschnitt.mitteltext ?: abschnitt.typ
                            ),
                            currentProgress = 0f // Standardwert
                        )
                    }
                )
            }
    
            return if (onlyDTicket) {
                mapped.filter { connection ->
                    connection.segments.none { segment ->
                        segment.train.type == TrainType.ICE || segment.train.type == TrainType.IC
                    }
                }
            } else {
                mapped
            }
        }
    
        private fun extractTime(isoDateTime: String): String {
            // "2026-07-21T12:14:00+02:00" -> "12:14"
            return try {
                isoDateTime.split("T")[1].substring(0, 5)
            } catch (e: Exception) {
                ""
            }
        }
    
        private fun mapToTrainType(gattung: String?): TrainType {
            return when (gattung?.uppercase()) {
                "ICE" -> TrainType.ICE
                "IC", "EC" -> TrainType.IC
                "RE", "IR" -> TrainType.RE
                "RB" -> TrainType.RB
                "SBAHN", "S" -> TrainType.S_BAHN
                "UBAHN", "U" -> TrainType.U_BAHN
                "STRASSENBAHN", "TRAM", "STR" -> TrainType.TRAM
                "BUS" -> TrainType.BUS
                else -> TrainType.RB
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
