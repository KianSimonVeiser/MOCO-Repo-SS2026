package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection

/**
 * Repräsentiert den kompletten Zustand der Startseite.
 * Ist das Model im MVVM
 */
data class HomeUiState(
    val date: String = "",              // Ausgewähltes Datum
    val time: String = "",              // Ausgewählte Uhrzeit
    val isArrival: Boolean = false,     // Schalter für Abfahrt (false) oder Ankunft (true)
    val onlyDTicket: Boolean = false,   // Filter für Deutschland-Ticket
    val showDatePicker: Boolean = false,// Steuert die Sichtbarkeit des Kalenders
    val showTimePicker: Boolean = false, // Steuert die Sichtbarkeit der Uhr
    val locationNeeded: Boolean = false, // Steuert die Sichtbarkeit des Standort-Dialogs
    val fromTextFieldState: TextFieldState = TextFieldState(),
    val fromSearchResult: List<String> = emptyList(),
    val toTextFieldState: TextFieldState = TextFieldState(),
    val toSearchResult: List<String> = emptyList(),
    val location: String = "",
    val favorites: List<FavoriteConnection> = emptyList() // Favorisierte Verbindungen
)
