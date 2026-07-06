package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.moco.DBNavigatorAlternative.data.repository.LocationRepositoryImpl
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.repository.LocationRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel für die Verbindungssuche.
 * Verwaltet den Zustand der Suchergebnisse und die Auswahl einer Verbindung.
 */
class SearchViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    // --- State für die Suchparameter ---
    var from by mutableStateOf("")
        private set
    var to by mutableStateOf("")
        private set
    var date by mutableStateOf("")
        private set
    var showDatePicker by mutableStateOf(false)
        private set
    var locationNeeded by mutableStateOf(false)
        private set

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    init {
        // Initialwerte setzen
        date = dateFormatter.format(Date())
    }

    // --- Aktionen für Suchparameter ---
    fun onFromChanged(newVal: String) { from = newVal }
    fun onToChanged(newVal: String) { to = newVal }
    fun toggleDatePicker(show: Boolean) { showDatePicker = show }
    fun onLocationNeeded() { locationNeeded = true }
    fun onLocationDismissed() { locationNeeded = false }

    fun onLocationAccepted() {
        viewModelScope.launch {
            val locationName = locationRepository.getCurrentLocation()
            if (locationName != null) {
                from = locationName
                locationNeeded = false
            }
        }
    }

    fun onDateSelected(millis: Long?) {
        millis?.let {
            date = dateFormatter.format(Date(it))
        }
        showDatePicker = false
    }

    // --- State für die Ergebnisse -
    // --
    var connections by mutableStateOf(getMockConnections())
        private set

    var selectedConnection by mutableStateOf<Connection?>(null)
        private set

    fun onConnectionSelected(connection: Connection?) {
        selectedConnection = connection
    }

    // Factory für SearchViewModel
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                val repository = LocationRepositoryImpl(application.applicationContext)
                return SearchViewModel(repository) as T
            }
        }
    }
}
