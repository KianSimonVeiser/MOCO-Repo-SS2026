package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.moco.DBNavigatorAlternative.domain.model.Connection
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel für die Verbindungssuche.
 * Verwaltet den Zustand der Suchergebnisse und die Auswahl einer Verbindung.
 */
class SearchViewModel : ViewModel() {

    // --- State für die Suchparameter (ähnlich wie im HomeViewModel) ---
    var from by mutableStateOf("")
        private set
    var to by mutableStateOf("")
        private set
    var date by mutableStateOf("")
        private set
    var showDatePicker by mutableStateOf(false)
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
    fun onDateSelected(millis: Long?) {
        millis?.let {
            date = dateFormatter.format(Date(it))
        }
        showDatePicker = false
    }

    // --- State für die Ergebnisse ---
    // Liste der gefundenen Verbindungen (aktuell Mock-Daten)
    var connections by mutableStateOf(getMockConnections())
        private set

    // Die aktuell für die Detailansicht ausgewählte Verbindung
    var selectedConnection by mutableStateOf<Connection?>(null)
        private set

    /**
     * Wählt eine Verbindung aus oder setzt die Auswahl zurück.
     */
    fun onConnectionSelected(connection: Connection?) {
        selectedConnection = connection
    }
}
