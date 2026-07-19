package com.moco.DBNavigatorAlternative.presentation.search

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.repository.PunctualityRepository
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
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
 * Verwaltet Suchparameter, Standortabfrage,
 * Suchergebnisse und Pünktlichkeitsinformationen.
 */
class SearchViewModel(
    private val locationRepository: LocationRepository,
    private val dbNavApiService: DBNavApiService,
    private val punctualityRepository: PunctualityRepository =
        PunctualityRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val dateFormatter =
        SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    init {
        _uiState.update {
            it.copy(
                date = dateFormatter.format(Date()),
                connections = getMockConnections()
            )
        }
    }

    // ---------------------------------------------------------
    // Suchparameter
    // ---------------------------------------------------------

    fun onFromChanged(newValue: TextFieldState) {
        val query = newValue.text.toString()
        val results = if (query.isBlank()) {
            emptyList()
        } else {
            listOf("Darmstadt Hbf", "Frankfurt(Main)Hbf", "Berlin Hbf")
                .filter { it.contains(query, ignoreCase = true) }
        }

        _uiState.update {
            it.copy(
                fromTextFieldState = newValue,
                fromSearchResult = results
            )
        }
    }

    fun onToChanged(newValue: TextFieldState) {
        val query = newValue.text.toString()
        val results = if (query.isBlank()) {
            emptyList()
        } else {
            listOf("München Hbf", "Hamburg Hbf", "Köln Hbf")
                .filter { it.contains(query, ignoreCase = true) }
        }

        _uiState.update {
            it.copy(
                toTextFieldState = newValue,
                toSearchResult = results
            )
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
                val locationObject =
                    locationRepository.getCurrentLocation()

                if (locationObject != null) {
                    val nearbyStations =
                        dbNavApiService.getNearbyStations(
                            locationObject.latitude,
                            locationObject.longitude
                        )

                    if (nearbyStations.isNotEmpty()) {
                        val stationNames = nearbyStations.map { it.name }
                        val nearestStation = stationNames.first()

                        _uiState.value.fromTextFieldState.edit {
                            replace(0, length, nearestStation)
                        }

                        _uiState.update {
                            it.copy(
                                fromSearchResult = stationNames
                            )
                        }
                    }
                }
            } catch (exception: Exception) {
                Log.e(
                    "SearchViewModel",
                    "Fehler während der Standort- oder Stationsabfrage",
                    exception
                )
            }
            _uiState.update { it.copy(locationNeeded = false) }
        }
    }

    // ---------------------------------------------------------
    // Suchergebnisse & Pünktlichkeit
    // ---------------------------------------------------------

    fun onConnectionSelected(connection: Connection?) {
        _uiState.update { it.copy(selectedConnection = connection) }
    }

    fun loadPunctualityInfo(connection: Connection) {
        if (_uiState.value.punctualityCache.containsKey(connection.id)) {
            return
        }

        viewModelScope.launch {
            val info =
                punctualityRepository.getPunctualityForConnection(connection)

            _uiState.update {
                val newCache = it.punctualityCache.toMutableMap()
                newCache[connection.id] = info
                it.copy(punctualityCache = newCache)
            }
        }
    }

    fun getPunctualityInfo(connection: Connection): PunctualityInfo? {
        return _uiState.value.punctualityCache[connection.id]
    }

    companion object {
        val Factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val application = checkNotNull(
                        extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    )

                    val locationRepository =
                        LocationRepositoryImpl(application.applicationContext)

                    return SearchViewModel(
                        locationRepository = locationRepository,
                        dbNavApiService = HomeViewModel.dbNavApiServiceInstance
                    ) as T
                }
            }
    }
}
