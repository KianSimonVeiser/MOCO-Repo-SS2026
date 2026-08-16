package com.moco.DBNavigatorAlternative.presentation.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.data.remote.HttpClientFactory
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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
        
        val dbNavApiServiceInstance: DBNavApiService by lazy {
            NearbyStationsRemoteImpl(HttpClientFactory.client)
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
                    interactionRepository.getFavorites(user.userId)
                        .catch { e -> Log.e(TAG, "Fehler beim Laden der Favoriten", e) }
                        .collect { favs ->
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

    private var searchJobFrom: kotlinx.coroutines.Job? = null
    private var searchJobTo: kotlinx.coroutines.Job? = null

    fun onFromChanged(newVal: TextFieldState) {
        val query = newVal.text.toString()
        searchJobFrom?.cancel()

        searchJobFrom = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(300) // Debounce
            }
            
            val results = if (query.isBlank()) {
                emptyList()
            } else {
                try {
                    dbNavApiService.getStationsByName(query)
                } catch (e: Exception) {
                    Log.e(TAG, "Fehler bei der Stationssuche (Von)", e)
                    emptyList()
                }
            }

            _uiState.update {
                it.copy(
                    fromTextFieldState = newVal,
                    fromSearchResult = results
                )
            }
        }
    }

    fun onFromItemSelected(location: NearbyLocationDto) {
        _uiState.update { it.copy(fromLocation = location, fromSearchResult = emptyList()) }
    }

    fun onToItemSelected(location: NearbyLocationDto) {
        _uiState.update { it.copy(toLocation = location, toSearchResult = emptyList()) }
    }

    // ---------------------------------------------------------
    // Zielbahnhof
    // ---------------------------------------------------------

    fun onToChanged(newVal: TextFieldState) {
        val query = newVal.text.toString()
        searchJobTo?.cancel()

        searchJobTo = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(300) // Debounce
            }

            val results = if (query.isBlank()) {
                emptyList()
            } else {
                try {
                    dbNavApiService.getStationsByName(query)
                } catch (e: Exception) {
                    Log.e(TAG, "Fehler bei der Stationssuche (Zu)", e)
                    emptyList()
                }
            }

            _uiState.update {
                it.copy(
                    toTextFieldState = newVal,
                    toSearchResult = results
                )
            }
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

                val nearestStation = nearbyStations.firstOrNull() ?: return@launch

                if (nearestStation.name.isBlank()) {
                    return@launch
                }

                _uiState.value.fromTextFieldState.edit {
                    replace(
                        start = 0,
                        end = length,
                        text = nearestStation.name
                    )
                }

                _uiState.update {
                    it.copy(
                        locationNeeded = false,
                        fromSearchResult = nearbyStations,
                        fromLocation = nearestStation
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
