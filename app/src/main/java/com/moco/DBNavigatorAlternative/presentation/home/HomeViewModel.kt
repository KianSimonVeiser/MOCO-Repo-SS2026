package com.moco.DBNavigatorAlternative.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

/**
 * Das ViewModel verwaltet die Logik des HomeScreens
 * Es ist entkoppelt von der UI und beobachtet das model
 */
class HomeViewModel : ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState())

    // Öffentlicher State - die UI kann diesen nur lesen/beobachten
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)

    init {
        // Initialwerte setzen (heutiges Datum und aktuelle Uhrzeit)
        val now = Calendar.getInstance()
        _uiState.update {
            it.copy(
                date = dateFormatter.format(Date()),
                time = String.format(Locale.GERMANY, "%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
            )
        }
    }

    // --- Funktionen für die UI-Interaktionen ---

    fun onFromChanged(newVal: String) { _uiState.update { it.copy(from = newVal) } }

    fun onToChanged(newVal: String) { _uiState.update { it.copy(to = newVal) } }

    fun toggleDatePicker(show: Boolean) { _uiState.update { it.copy(showDatePicker = show) } }

    fun onDateSelected(millis: Long?) {
        millis?.let {
            val selectedDate = dateFormatter.format(Date(it))
            _uiState.update { it.copy(date = selectedDate, showDatePicker = false) }
        }
    }

    fun toggleTimePicker(show: Boolean) { _uiState.update { it.copy(showTimePicker = show) } }

    fun onTimeSelected(hour: Int, minute: Int) {
        val selectedTime = String.format(Locale.GERMANY, "%02d:%02d", hour, minute)
        _uiState.update { it.copy(time = selectedTime, showTimePicker = false) }
    }

    fun toggleArrival(arrival: Boolean) { _uiState.update { it.copy(isArrival = arrival) } }

    fun toggleOnlyDTicket(active: Boolean) { _uiState.update { it.copy(onlyDTicket = active) } }
}