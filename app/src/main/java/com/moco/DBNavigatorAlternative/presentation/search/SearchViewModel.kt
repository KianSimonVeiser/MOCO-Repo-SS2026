package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.moco.DBNavigatorAlternative.domain.model.Connection

/**
 * ViewModel für die Verbindungssuche.
 * Verwaltet den Zustand der Suchergebnisse und die Auswahl einer Verbindung.
 */
class SearchViewModel : ViewModel() {
    
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
