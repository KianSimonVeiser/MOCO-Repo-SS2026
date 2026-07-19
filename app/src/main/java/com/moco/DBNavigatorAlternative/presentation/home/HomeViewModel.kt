package com.moco.DBNavigatorAlternative.presentation.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.remote.NearbyStationsRemoteImpl
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
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
 */
class HomeViewModel(
    private val locationRepository: LocationRepository,
    private val dbNavApiService: DBNavApiService,
    private val interactionRepository: InteractionRepository = InteractionRepository(),
    private val userRepository: UserRepository = UserRepository
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
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val dateFormatter =
        SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    init {
        setInitialDateAndTime()
    }

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

        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            userRepository.currentUser.collect { user ->
                if (user != null) {
                    interactionRepository.getFavorites(user.userId).collect { favs ->
                        _uiState.update { it.copy(favorites = favs) }
                    }
                } else {
                    _uiState.update { it.copy(favorites = emptyList()) }
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Startbahnhof
    // ---------------------------------------------------------

    /**
     * Klick auf einen Favoriten füllt die Felder automatisch aus.
     */
    fun onFavoriteClicked(favorite: FavoriteConnection) {
        _uiState.value.fromTextFieldState.edit {
            replace(0, length, favorite.fromStation)
        }
        _uiState.value.toTextFieldState.edit {
            replace(0, length, favorite.toStation)
        }
    }

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

    fun onTimeSelected(hour: Int, minute: Int) {
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
