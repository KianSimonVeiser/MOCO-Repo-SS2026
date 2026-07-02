package com.moco.DBNavigatorAlternative.data.api


import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * Definition der API-Schnittstelle (Retrofit).
 * Diese Klasse kapselt alle Aufrufe an den lokalen db-vendo-client Server.
 */
interface LocationApiService {
    @GET("locations")
    suspend fun searchLocations(@Query("query") searchTerm: String): List<LocationApiResult>

    @GET("stops/{id}/departures")
    suspend fun getDepartures(
        @Path("id") stationId: String,
        @Query("duration") duration: Int = 30
    ): DeparturesResponse
}

/**
 * Zentrales Singleton zur Bereitstellung des konfigurierten Retrofit-Clients.
 */
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: LocationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)
    }
}

/**
 * Datenmodelle für API-Antworten.
 */
data class LocationApiResult(
    val id: String?,
    val name: String?
)

data class DepartureApiResult(
    val tripId: String?,
    val direction: String?,
    @SerializedName("when") val whenTime: String?,
    val plannedWhen: String?,
    val delay: Int?
)

data class DeparturesResponse(
    val departures: List<DepartureApiResult>? = emptyList()
)

