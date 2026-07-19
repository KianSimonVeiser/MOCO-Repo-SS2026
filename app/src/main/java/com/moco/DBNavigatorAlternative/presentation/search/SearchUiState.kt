package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary

/**
 * Repräsentiert den UI-Zustand der Verbindungssuche.
 */
data class SearchUiState(
    val fromTextFieldState: TextFieldState = TextFieldState(),
    val toTextFieldState: TextFieldState = TextFieldState(),
    val fromSearchResult: List<String> = emptyList(),
    val toSearchResult: List<String> = emptyList(),
    val date: String = "",
    val showDatePicker: Boolean = false,
    val locationNeeded: Boolean = false,
    val connections: List<Connection> = emptyList(),
    val selectedConnection: Connection? = null,
    val punctualityCache: Map<String, PunctualityInfo> = emptyMap(),
    val stationRatingCache: Map<String, StationRatingSummary> = emptyMap() // NEU: Cache für Bewertungen
)
