package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection

import com.moco.DBNavigatorAlternative.data.SearchStateStore

/**
 * Repräsentiert den kompletten Zustand der Startseite.
 * Ist das Model im MVVM
 */
data class HomeUiState(
    val date: String = SearchStateStore.date,              // Ausgewähltes Datum
    val time: String = SearchStateStore.time,              // Ausgewählte Uhrzeit
    val isArrival: Boolean = SearchStateStore.isArrival,     // Schalter für Abfahrt (false) oder Ankunft (true)
    val onlyDTicket: Boolean = SearchStateStore.onlyDTicket,   // Filter für Deutschland-Ticket
    val showDatePicker: Boolean = false, // Steuert die Sichtbarkeit des Kalenders
    val showTimePicker: Boolean = false, // Steuert die Sichtbarkeit der Uhr
    val locationNeeded: Boolean = false, // Steuert die Sichtbarkeit des Standort-Dialogs
    val fromTextFieldState: TextFieldState = SearchStateStore.fromTextFieldState,
    val fromSearchResult: List<NearbyLocationDto> = emptyList(),
    val toTextFieldState: TextFieldState = SearchStateStore.toTextFieldState,
    val toSearchResult: List<NearbyLocationDto> = emptyList(),
    val fromLocation: NearbyLocationDto? = SearchStateStore.fromLocation,
    val toLocation: NearbyLocationDto? = SearchStateStore.toLocation,
    val location: String = "",
    val favorites: List<FavoriteConnection> = emptyList() // Favorisierte Verbindungen
)
