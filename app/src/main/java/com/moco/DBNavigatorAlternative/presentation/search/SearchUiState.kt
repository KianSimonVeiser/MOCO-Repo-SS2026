package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary

/**
 * Repräsentiert den UI-Zustand der Verbindungssuche.
 */
data class SearchUiState(
    val fromTextFieldState: TextFieldState = TextFieldState(),
    val toTextFieldState: TextFieldState = TextFieldState(),
    val fromSearchResult: List<NearbyLocationDto> = emptyList(),
    val toSearchResult: List<NearbyLocationDto> = emptyList(),
    val fromLocation: NearbyLocationDto? = null,
    val toLocation: NearbyLocationDto? = null,
    val date: String = "",
    val showDatePicker: Boolean = false,
    val locationNeeded: Boolean = false,
    val connections: List<Connection> = emptyList(),
    val onlyDTicket: Boolean = false,
    val selectedConnection: Connection? = null,
    val punctualityCache: Map<String, PunctualityInfo> = emptyMap(),
    val stationRatingCache: Map<String, StationRatingSummary> = emptyMap()
)
