package com.moco.DBNavigatorAlternative.presentation.search

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.SearchStateStore
import com.moco.DBNavigatorAlternative.data.local.SettingsPreference
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.data.repository.PunctualityRepository
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary
import com.moco.DBNavigatorAlternative.domain.repository.LocationRepository
import com.moco.DBNavigatorAlternative.presentation.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel für die Verbindungssuche.
 * Verwaltet Suchparameter, Standortabfrage, Suchergebnisse, 
 * Pünktlichkeitsinformationen und Bahnhofsbewertungen.
 */
class SearchViewModel(
    private val locationRepository: LocationRepository,
    private val dbNavApiService: DBNavApiService,
    private val punctualityRepository: PunctualityRepository = PunctualityRepository(),
    private val interactionRepository: InteractionRepository = InteractionRepository(),
    private val settingsPreference: SettingsPreference? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    init {
        _uiState.update {
            it.copy(
                date = SearchStateStore.date,
                onlyDTicket = SearchStateStore.onlyDTicket
            )
        }
        observeSettings()
    }

    private fun observeSettings() {
        settingsPreference?.let { prefs ->
            viewModelScope.launch {
                prefs.onlyDeutschlandticketConnections.collect { active ->
                    val oldActive = _uiState.value.onlyDTicket
                    _uiState.update { it.copy(onlyDTicket = active) }
                    
                    // Falls sich der Wert ändert und wir bereits Orte ausgewählt haben, Suche neu triggern
                    if (oldActive != active) {
                        triggerSearch()
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------
    // Suchparameter
    // ---------------------------------------------------------

    private var searchJobFrom: kotlinx.coroutines.Job? = null
    private var searchJobTo: kotlinx.coroutines.Job? = null

    fun onFromChanged(newValue: TextFieldState) {
        val query = newValue.text.toString()
        searchJobFrom?.cancel()
        
        searchJobFrom = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(300)
            }

            val results = if (query.isBlank()) {
                emptyList()
            } else {
                try {
                    dbNavApiService.getStationsByName(query)
                } catch (e: Exception) {
                    Log.e("SearchViewModel", "Fehler bei der Stationssuche (Von)", e)
                    emptyList()
                }
            }

            _uiState.update {
                it.copy(
                    fromTextFieldState = newValue,
                    fromSearchResult = results
                )
            }
        }
    }

    fun onToChanged(newValue: TextFieldState) {
        val query = newValue.text.toString()
        searchJobTo?.cancel()
        
        searchJobTo = viewModelScope.launch {
            if (query.isNotBlank()) {
                kotlinx.coroutines.delay(300)
            }

            val results = if (query.isBlank()) {
                emptyList()
            } else {
                try {
                    dbNavApiService.getStationsByName(query)
                } catch (e: Exception) {
                    Log.e("SearchViewModel", "Fehler bei der Stationssuche (Zu)", e)
                    emptyList()
                }
            }

            _uiState.update {
                it.copy(
                    toTextFieldState = newValue,
                    toSearchResult = results
                )
            }
        }
    }

    // ---------------------------------------------------------
    // Datum
    // ---------------------------------------------------------

    fun toggleDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun onDateSelected(millis: Long?) {
        millis?.let {
            val selectedDate = dateFormatter.format(Date(it))
            SearchStateStore.date = selectedDate
            _uiState.update {
                it.copy(
                    date = selectedDate,
                    showDatePicker = false
                )
            }
        } ?: _uiState.update { it.copy(showDatePicker = false) }
    }

    // ---------------------------------------------------------
    // Standortabfrage
    // ---------------------------------------------------------

    fun onLocationNeeded() {
        _uiState.update { it.copy(locationNeeded = true) }
    }

    fun onLocationDismissed() {
        _uiState.update { it.copy(locationNeeded = false) }
    }

    fun onLocationAccepted() {
        viewModelScope.launch {
            try {
                val locationObject = locationRepository.getCurrentLocation()

                if (locationObject != null) {
                    val nearbyStations = dbNavApiService.getNearbyStations(
                        locationObject.latitude,
                        locationObject.longitude
                    )

                    if (nearbyStations.isNotEmpty()) {
                        val nearestStation = nearbyStations.first()

                        _uiState.value.fromTextFieldState.edit {
                            replace(0, length, nearestStation.name)
                        }
                        
                        SearchStateStore.fromLocation = nearestStation

                        _uiState.update {
                            it.copy(
                                fromSearchResult = nearbyStations,
                                fromLocation = nearestStation
                            )
                        }
                        triggerSearch()
                    }
                }
            } catch (exception: Exception) {
                Log.e("SearchViewModel", "Fehler bei Standortabfrage", exception)
            }
            _uiState.update { it.copy(locationNeeded = false) }
        }
    }

    fun onFromItemSelected(location: NearbyLocationDto) {
        SearchStateStore.fromLocation = location
        _uiState.update { it.copy(fromLocation = location, fromSearchResult = emptyList()) }
        triggerSearch()
    }

    fun onToItemSelected(location: NearbyLocationDto) {
        SearchStateStore.toLocation = location
        _uiState.update { it.copy(toLocation = location, toSearchResult = emptyList()) }
        triggerSearch()
    }

    fun onToggleOnlyDTicket(enabled: Boolean) {
        SearchStateStore.onlyDTicket = enabled
        _uiState.update { it.copy(onlyDTicket = enabled) }
        viewModelScope.launch {
            settingsPreference?.setOnlyDeutschlandticketConnections(enabled)
        }
        triggerSearch()
    }

    fun setInitialSearch(fromId: String?, toId: String?, dateStr: String?, onlyDTicket: Boolean? = null) {
        viewModelScope.launch {
            val currentOnlyDTicket = onlyDTicket ?: _uiState.value.onlyDTicket
            val currentDate = dateStr ?: _uiState.value.date
            
            // Globalen Store aktualisieren, damit die Werte beim Zurückgehen erhalten bleiben
            SearchStateStore.date = currentDate
            onlyDTicket?.let { SearchStateStore.onlyDTicket = it }

            _uiState.update { it.copy(
                date = currentDate,
                onlyDTicket = currentOnlyDTicket
            ) }
            
            if (fromId != null && toId != null) {
                performSearch(fromId, toId, currentDate, currentOnlyDTicket)
            }
        }
    }

    private fun triggerSearch() {
        val state = _uiState.value
        val fromId = state.fromLocation?.locationId ?: return
        val toId = state.toLocation?.locationId ?: return
        performSearch(fromId, toId, state.date, state.onlyDTicket)
    }

    private fun performSearch(fromId: String, toId: String, date: String, onlyDTicket: Boolean) {
        // Umwandlung von dd.MM.yyyy in yyyy-MM-dd für die API
        val isoDate = try {
            val parts = date.split(".")
            if (parts.size == 3) {
                "${parts[2]}-${parts[1]}-${parts[0]}"
            } else {
                "2026-07-21" // Fallback
            }
        } catch (e: Exception) {
            "2026-07-21"
        }
        
        val isoDateTime = "${isoDate}T12:00:00+02:00"

        viewModelScope.launch {
            try {
                val results = dbNavApiService.getConnections(fromId, toId, isoDateTime, onlyDTicket)
                _uiState.update { it.copy(connections = results) }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Fehler bei Verbindungssuche", e)
            }
        }
    }

    fun onConnectionSelected(connection: Connection?) {
        SearchStateStore.selectedConnection = connection
        _uiState.update { it.copy(selectedConnection = connection) }
    }

    fun loadPunctualityInfo(connection: Connection) {
        if (_uiState.value.punctualityCache.containsKey(connection.id)) return

        viewModelScope.launch {
            try {
                val info = punctualityRepository.getPunctualityForConnection(connection)
                _uiState.update {
                    val newCache = it.punctualityCache.toMutableMap()
                    newCache[connection.id] = info
                    it.copy(punctualityCache = newCache)
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Fehler beim Laden der Pünktlichkeit", e)
            }
        }
    }

    fun getPunctualityInfo(connection: Connection): PunctualityInfo? {
        return _uiState.value.punctualityCache[connection.id]
    }

    // ---------------------------------------------------------
    // Bahnhofsbewertungen
    // ---------------------------------------------------------

    fun loadStationRating(stationId: String) {
        if (_uiState.value.stationRatingCache.containsKey(stationId)) return

        viewModelScope.launch {
            try {
                val summary = interactionRepository.getStationRatingSummary(stationId)
                if (summary != null) {
                    _uiState.update {
                        val newCache = it.stationRatingCache.toMutableMap()
                        newCache[stationId] = summary
                        it.copy(stationRatingCache = newCache)
                    }
                }
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Fehler beim Laden der Bahnhofsbewertung für $stationId", e)
            }
        }
    }

    fun getStationRating(stationId: String): StationRatingSummary? {
        return _uiState.value.stationRatingCache[stationId]
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val locationRepository = LocationRepositoryImpl(application.applicationContext)
                val settingsPreference = SettingsPreference(application.applicationContext)
                return SearchViewModel(
                    locationRepository = locationRepository,
                    dbNavApiService = HomeViewModel.dbNavApiServiceInstance,
                    punctualityRepository = PunctualityRepository(),
                    interactionRepository = InteractionRepository(),
                    settingsPreference = settingsPreference
                ) as T
            }
        }
    }
}
