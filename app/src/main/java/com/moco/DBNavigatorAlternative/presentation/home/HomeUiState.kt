package com.moco.DBNavigatorAlternative.presentation.home

/**
 * Repräsentiert den kompletten Zustand der Startseite.
 * Ist das Model im MVVM
 * ist persistent
 */
data class HomeUiState(
    val from: String = "",              // Startbahnhof
    val to: String = "",                // Zielbahnhof
    val date: String = "",              // Ausgewähltes Datum
    val time: String = "",              // Ausgewählte Uhrzeit
    val isArrival: Boolean = false,     // Schalter für Abfahrt (false) oder Ankunft (true)
    val onlyDTicket: Boolean = false,   // Filter für Deutschland-Ticket
    val showDatePicker: Boolean = false,// Steuert die Sichtbarkeit des Kalenders
    val showTimePicker: Boolean = false // Steuert die Sichtbarkeit der Uhr
)