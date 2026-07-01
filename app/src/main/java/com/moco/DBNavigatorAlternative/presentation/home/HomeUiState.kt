package com.moco.DBNavigatorAlternative.presentation.home

/**
 * Repräsentiert den kompletten Zustand der Startseite.
 */
data class HomeUiState(
    val from: String = "",
    val to: String = "",
    val date: String = "",
    val time: String = "",
    val isArrival: Boolean = false,
    val onlyDTicket: Boolean = false,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
)
