package com.moco.DBNavigatorAlternative.presentation.home

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.SearchStateStore
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.data.remote.HttpClientFactory
import com.moco.DBNavigatorAlternative.data.remote.NearbyStationsRemoteImpl
import com.moco.DBNavigatorAlternative.data.local.SettingsPreference
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import com.moco.DBNavigatorAlternative.domain.repository.FavoriteRepository
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

// # ViewModel für die Startseite
// Hier verwalten wir alles, was auf dem Home-Bildschirm passiert.
// Dazu gehört das Laden von Favoriten, die Stationssuche und der Standort.
class HomeViewModel(
    private val locationRepository: LocationRepository,
    private val dbNavApiService: DBNavApiService,
    private val favoriteRepository: FavoriteRepository,
    private val settingsPreference: SettingsPreference? = null
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
        observeSettings()
        observeUserAndSync()
    }

    private fun observeUserAndSync() {
        viewModelScope.launch {
            UserRepository.currentUser.collect { user ->
                if (user != null) {
                    favoriteRepository.syncWithRemote()
                }
            }
        }
    }

    private fun observeSettings() {
        settingsPreference?.let { prefs ->
            viewModelScope.launch {
                prefs.onlyDeutschlandticketConnections.collect { active ->
                    _uiState.update { it.copy(onlyDTicket = active) }
                }
            }
        }
    }

    private fun setInitialDateAndTime() {
        // Werte aus dem globalen Speicher laden, damit sie beim Hin- und Herwechseln bleiben
        _uiState.update {
            it.copy(
                date = SearchStateStore.date,
                time = SearchStateStore.time,
                isArrival = SearchStateStore.isArrival,
                onlyDTicket = SearchStateStore.onlyDTicket
            )
        }

        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getAllFavorites()
                .catch { e -> Log.e(TAG, "Fehler beim Laden der Favoriten", e) }
                .collect { favs ->
                    _uiState.update { it.copy(favorites = favs) }
                }
        }
    }

    // # Startbahnhof

    // Wenn ein Favorit geklickt wird, gehen wir direkt zur Detailansicht
    fun onFavoriteClicked(favorite: FavoriteConnection, onNavigateToDetail: (connectionId: String, date: String) -> Unit) {
        SearchStateStore.fromLocation = null
        SearchStateStore.toLocation = null
        _uiState.update { it.copy(fromLocation = null, toLocation = null) }

        _uiState.value.fromTextFieldState.edit {
            replace(0, length, favorite.fromStation)
        }
        _uiState.value.toTextFieldState.edit {
            replace(0, length, favorite.toStation)
        }
        
        onNavigateToDetail(favorite.connectionId, _uiState.value.date)
    }

    fun onDeleteFavorite(favorite: FavoriteConnection) {
        viewModelScope.launch {
            UserRepository.currentUser.value?.let { user ->
                InteractionRepository().removeFavorite(user.userId, favorite.connectionId)
            }
            favoriteRepository.deleteFavoriteByChecksum(favorite.connectionId)
        }
    }

    private var searchJobFrom: kotlinx.coroutines.Job? = null
    private var searchJobTo: kotlinx.coroutines.Job? = null

    fun onFromChanged(newVal: TextFieldState) {
        val query = newVal.text.toString()
        searchJobFrom?.cancel()

        searchJobFrom = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(300) // Kurz warten, um nicht bei jedem Tastendruck zu suchen
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
        SearchStateStore.fromLocation = location
        _uiState.update { it.copy(fromLocation = location, fromSearchResult = emptyList()) }
    }

    fun onToItemSelected(location: NearbyLocationDto) {
        SearchStateStore.toLocation = location
        _uiState.update { it.copy(toLocation = location, toSearchResult = emptyList()) }
    }

    // # Zielbahnhof

    fun onToChanged(newVal: TextFieldState) {
        val query = newVal.text.toString()
        searchJobTo?.cancel()

        searchJobTo = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(300) // Kurz warten
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

    // # Datum

    fun toggleDatePicker(show: Boolean) {
        _uiState.update {
            it.copy(showDatePicker = show)
        }
    }

    fun onDateSelected(millis: Long?) {
        millis ?: return

        val selectedDate = dateFormatter.format(Date(millis))
        SearchStateStore.date = selectedDate

        _uiState.update {
            it.copy(
                date = selectedDate,
                showDatePicker = false
            )
        }
    }

    // # Uhrzeit

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
        SearchStateStore.time = selectedTime

        _uiState.update {
            it.copy(
                time = selectedTime,
                showTimePicker = false
            )
        }
    }

    // # Ankunft oder Abfahrt

    fun toggleArrival(arrival: Boolean) {
        SearchStateStore.isArrival = arrival
        _uiState.update {
            it.copy(isArrival = arrival)
        }
    }

    // # Deutschlandticket

    fun toggleOnlyDTicket(active: Boolean) {
        SearchStateStore.onlyDTicket = active
        _uiState.update {
            it.copy(onlyDTicket = active)
        }
        viewModelScope.launch {
            settingsPreference?.setOnlyDeutschlandticketConnections(active)
        }
    }

    // # Standort

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

    // Wird aufgerufen, wenn der Nutzer seinen Standort freigibt
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
                
                SearchStateStore.fromLocation = nearestStation

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

