package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.PunctualityRepository
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.repository.LocationRepository
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
    private val punctualityRepository: PunctualityRepository =
        PunctualityRepository()
) : ViewModel() {

    // Suchparameter
    var from by mutableStateOf("")
        private set

    var to by mutableStateOf("")
        private set

    var date by mutableStateOf("")
        private set

    var showDatePicker by mutableStateOf(false)
        private set

    // Steuert den Dialog für die Standortabfrage
    var locationNeeded by mutableStateOf(false)
        private set

    private val dateFormatter =
        SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    // Cache für Pünktlichkeitsinformationen
    private val _punctualityCache =
        mutableStateMapOf<String, PunctualityInfo>()

    val punctualityCache: Map<String, PunctualityInfo>
        get() = _punctualityCache

    init {
        date = dateFormatter.format(Date())
    }

    // Suchparameter ändern

    fun onFromChanged(newValue: String) {
        from = newValue
    }

    fun onToChanged(newValue: String) {
        to = newValue
    }

    fun toggleDatePicker(show: Boolean) {
        showDatePicker = show
    }

    fun onDateSelected(millis: Long?) {
        millis?.let {
            date = dateFormatter.format(Date(it))
        }

        showDatePicker = false
    }

    // Standortabfrage

    fun onLocationNeeded() {
        locationNeeded = true
    }

    fun onLocationDismissed() {
        locationNeeded = false
    }

    fun onLocationAccepted() {
        viewModelScope.launch {
            val location = locationRepository.getCurrentLocation()

            if (location != null) {
                from = location
            }

            locationNeeded = false
        }
    }

    // Suchergebnisse

    var connections by mutableStateOf(getMockConnections())
        private set

    var selectedConnection by mutableStateOf<Connection?>(null)
        private set

    fun onConnectionSelected(connection: Connection?) {
        selectedConnection = connection
    }

    // Pünktlichkeitsinformationen

    fun loadPunctualityInfo(connection: Connection) {
        if (_punctualityCache.containsKey(connection.id)) {
            return
        }

        viewModelScope.launch {
            val info =
                punctualityRepository.getPunctualityForConnection(connection)

            _punctualityCache[connection.id] = info
        }
    }

    fun getPunctualityInfo(
        connection: Connection
    ): PunctualityInfo? {
        return _punctualityCache[connection.id]
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
                        extras[
                            ViewModelProvider.AndroidViewModelFactory
                                .APPLICATION_KEY
                        ]
                    )

                    val locationRepository =
                        LocationRepositoryImpl(
                            application.applicationContext
                        )

                    val punctualityRepository =
                        PunctualityRepository()

                    return SearchViewModel(
                        locationRepository = locationRepository,
                        punctualityRepository = punctualityRepository
                    ) as T
                }
            }
    }
}