package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.LineRatingSummary

import com.moco.DBNavigatorAlternative.data.SearchStateStore

/**
 * Repräsentiert den UI-Zustand der Verbindungssuche.
 */
data class SearchUiState(
    val fromTextFieldState: TextFieldState = SearchStateStore.fromTextFieldState,
    val toTextFieldState: TextFieldState = SearchStateStore.toTextFieldState,
    val fromSearchResult: List<NearbyLocationDto> = emptyList(),
    val toSearchResult: List<NearbyLocationDto> = emptyList(),
    val fromLocation: NearbyLocationDto? = SearchStateStore.fromLocation,
    val toLocation: NearbyLocationDto? = SearchStateStore.toLocation,
    val date: String = SearchStateStore.date,
    val showDatePicker: Boolean = false,
    val locationNeeded: Boolean = false,
    val connections: List<Connection> = emptyList(),
    val onlyDTicket: Boolean = SearchStateStore.onlyDTicket,
    val selectedConnection: Connection? = null,
    val punctualityCache: Map<String, PunctualityInfo> = emptyMap(),
    val stationRatingCache: Map<String, Any> = emptyMap() // Speichert jetzt verschiedene Rating-Typen (generisch)
)
