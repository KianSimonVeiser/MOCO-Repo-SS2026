package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

/**
 * DER HOMESCREEN
 * Dies ist die Hauptseite der App. Sie ist nach dem MVVM-Muster aufgebaut

 */
@Composable
fun HomeScreen(
    // Das ViewModel wird hier automatisch bereitgestellt
    viewModel: HomeViewModel = viewModel()
) {
    // Wir beobachten den Zustand der Daten (uiState) aus dem ViewModel
    val uiState by viewModel.uiState.collectAsState()

    MyApplicationTheme {
        // Scaffold ist das Grundgerüst, das z.B. Platz für die TopBar bietet
        Scaffold(
            topBar = {
                // Wir nutzen hier wieder die originale AppTopBar aus deinem Projekt
                AppTopBar(title = "DB-Navigator-Alternative")
            }
        ) { innerPadding ->
            // Column ordnet alle Sektionen untereinander an
            Column(
                modifier = Modifier
                    .padding(innerPadding) // Verhindert Überlappung mit der TopBar
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Macht den gesamten Screen scrollbar
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp) // Großer Abstand für ein luftiges Design
            ) {
                // --- 1. SEKTION: DIE SUCHE (VON/BIS) ---
                SearchSection(
                    fromValue = uiState.from,
                    toValue = uiState.to,
                    onFromChange = { viewModel.onFromChanged(it) },
                    onToChange = { viewModel.onToChanged(it) }
                )

                // --- 2. SEKTION: DAS DATUM ---
                DateTimeSection(
                    dateText = uiState.date,
                    showDatePicker = uiState.showDatePicker,
                    onDateClick = { viewModel.toggleDatePicker(true) },
                    onDateSelected = { viewModel.onDateSelected(it) },
                    onDismiss = { viewModel.toggleDatePicker(false) }
                )

                // --- 3. SEKTION: UHRZEIT & ABFAHRT/ANKUNFT ---
                ArrivalDepartureSection(
                    isArrival = uiState.isArrival,
                    timeText = uiState.time,
                    showTimePicker = uiState.showTimePicker,
                    onArrivalChange = { viewModel.toggleArrival(it) },
                    onTimeClick = { viewModel.toggleTimePicker(true) },
                    onTimeSelected = { h, m -> viewModel.onTimeSelected(h, m) },
                    onDismiss = { viewModel.toggleTimePicker(false) }
                )

                // --- 4. SEKTION: ZUSATZOPTIONEN (D-TICKET) ---
                TicketOptionSection(
                    onlyDTicket = uiState.onlyDTicket,
                    onToggle = { viewModel.toggleOnlyDTicket(it) }
                )

                // --- 5. SEKTION: DER SUCH-BUTTON ---
                SearchButton(
                    onClick = { /* Hier würde die Suche ausgelöst werden */ }
                )

                // --- 6. SEKTION: MEINE FAVORITEN ---
                FavoritesSection()
            }
        }
    }
}
