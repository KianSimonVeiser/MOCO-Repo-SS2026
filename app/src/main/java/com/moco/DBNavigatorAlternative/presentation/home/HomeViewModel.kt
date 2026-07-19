package com.moco.DBNavigatorAlternative.presentation.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.remote.NearbyStationsRemoteImpl
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.repository.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Das ViewModel verwaltet die Logik des HomeScreens.
 *
 * Es ist von der UI getrennt und verwendet:
 * - LocationRepository für den aktuellen Standort
 * - DBNavApiService für die Suche nach Stationen
 */
class HomeViewModel(
    private val locationRepository: LocationRepository,
    private val dbNavApiService: DBNavApiService
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
        private val client = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KTOR_HTTP", message)
                    }
                }
                level = LogLevel.ALL
            }
        }
        val dbNavApiServiceInstance: DBNavApiService by lazy {
            NearbyStationsRemoteImpl(client)
        }
    }

    private val _uiState = MutableStateFlow(HomeUiState())

    /**
     * Öffentlicher State.
     * Die UI kann ihn lesen, aber nicht direkt verändern.
     */
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val dateFormatter =
        SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    init {
        setInitialDateAndTime()
    }

    /**
     * Setzt beim Start der App das aktuelle Datum
     * und die aktuelle Uhrzeit.
     */
    private fun setInitialDateAndTime() {
        val now = Calendar.getInstance()

        _uiState.update {
            it.copy(
                date = dateFormatter.format(Date()),
                time = String.format(
                    Locale.GERMANY,
                    "%02d:%02d",
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE)
                )
            )
        }
    }

    // ---------------------------------------------------------
    // Startbahnhof
    // ---------------------------------------------------------

    fun onFromChanged(newVal: TextFieldState) {
        val query = newVal.text.toString()

        val possibleStations = listOf(
            "Darmstadt Hbf",
            "Frankfurt(Main)Hbf",
            "Berlin Hbf"
        )

        _uiState.update {
            it.copy(
                fromTextFieldState = newVal,
                fromSearchResult = if (query.isBlank()) {
                    emptyList()
                } else {
                    possibleStations.filter { station ->
                        station.contains(
                            other = query,
                            ignoreCase = true
                        )
                    }
                }
            )
        }
    }

    // ---------------------------------------------------------
    // Zielbahnhof
    // ---------------------------------------------------------

    fun onToChanged(newVal: TextFieldState) {
        val query = newVal.text.toString()

        val possibleStations = listOf(
            "München Hbf",
            "Hamburg Hbf",
            "Köln Hbf"
        )

        _uiState.update {
            it.copy(
                toTextFieldState = newVal,
                toSearchResult = if (query.isBlank()) {
                    emptyList()
                } else {
                    possibleStations.filter { station ->
                        station.contains(
                            other = query,
                            ignoreCase = true
                        )
                    }
                }
            )
        }
    }

    // ---------------------------------------------------------
    // Datum
    // ---------------------------------------------------------

    fun toggleDatePicker(show: Boolean) {
        _uiState.update {
            it.copy(showDatePicker = show)
        }
    }

    fun onDateSelected(millis: Long?) {
        millis ?: return

        val selectedDate = dateFormatter.format(Date(millis))

        _uiState.update {
            it.copy(
                date = selectedDate,
                showDatePicker = false
            )
        }
    }

    // ---------------------------------------------------------
    // Uhrzeit
    // ---------------------------------------------------------

    fun toggleTimePicker(show: Boolean) {
        _uiState.update {
            it.copy(showTimePicker = show)
        }
    }

    fun onTimeSelected(
        hour: Int,
        minute: Int
    ) {
        val selectedTime = String.format(
            Locale.GERMANY,
            "%02d:%02d",
            hour,
            minute
        )

        _uiState.update {
            it.copy(
                time = selectedTime,
                showTimePicker = false
            )
        }
    }

    // ---------------------------------------------------------
    // Ankunft oder Abfahrt
    // ---------------------------------------------------------

    fun toggleArrival(arrival: Boolean) {
        _uiState.update {
            it.copy(isArrival = arrival)
        }
    }

    // ---------------------------------------------------------
    // Deutschlandticket
    // ---------------------------------------------------------

    fun toggleOnlyDTicket(active: Boolean) {
        _uiState.update {
            it.copy(onlyDTicket = active)
        }
    }

    // ---------------------------------------------------------
    // Standort
    // ---------------------------------------------------------

    fun onLocationNeeded() {
        _uiState.update {
            it.copy(locationNeeded = true)
        }
    }

    fun onLocationDismissed() {
        _uiState.update {
            it.copy(locationNeeded = false)
        }
    }

    /**
     * Wird aufgerufen, wenn der Benutzer die Standortnutzung bestätigt.
     *
     * Ablauf:
     * 1. Aktuellen Standort abrufen
     * 2. Breiten- und Längengrad an die DB-API übergeben
     * 3. Nächste Station auswählen
     * 4. Stationsname in das Startfeld schreiben
     */
    fun onLocationAccepted() {

        viewModelScope.launch {
            try {
                val locationObject =
                    locationRepository.getCurrentLocation()

                if (locationObject == null) {
                    return@launch
                }

                val nearbyStations =
                    dbNavApiService.getNearbyStations(
                        locationObject.latitude,
                        locationObject.longitude
                    )

                if (nearbyStations.isEmpty()) {
                    return@launch
                }

                val stationNames = nearbyStations.map { it.name }
                val nearestStation = stationNames.firstOrNull() ?: return@launch

                if (nearestStation.isBlank()) {
                    return@launch
                }

                _uiState.value.fromTextFieldState.edit {
                    replace(
                        start = 0,
                        end = length,
                        text = nearestStation
                    )
                }

                _uiState.update {
                    it.copy(
                        locationNeeded = false,
                        fromSearchResult = stationNames
                    )
                }

            } catch (exception: Exception) {
                Log.e(
                    TAG,
                    "Fehler während der Standort- oder Stationsabfrage",
                    exception
                )
                _uiState.update { it.copy(locationNeeded = false) }
            }
        }
    }
}
