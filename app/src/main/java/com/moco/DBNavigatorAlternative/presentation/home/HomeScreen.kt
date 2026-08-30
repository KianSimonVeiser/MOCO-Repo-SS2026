package com.moco.DBNavigatorAlternative.presentation.home

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar
import com.moco.DBNavigatorAlternative.presentation.generalUse.SearchButton
import com.moco.DBNavigatorAlternative.presentation.generalUse.SearchSection
import com.moco.DBNavigatorAlternative.presentation.home.components.ArrivalDepartureSection
import com.moco.DBNavigatorAlternative.presentation.home.components.DateTimeSection
import com.moco.DBNavigatorAlternative.presentation.home.components.FavoritesSection
import com.moco.DBNavigatorAlternative.presentation.home.components.TicketOptionSection
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

// # Startseite der App
// Hier passiert die eigentliche Reiseplanung. Wir haben Felder für Start und Ziel,
// die Auswahl für Datum und Uhrzeit, Optionen für Tickets und die Favoriten.
@Composable
fun HomeScreen(
    onNavigateToSearch: (fromId: String?, toId: String?, date: String, onlyDTicket: Boolean) -> Unit,
    onNavigateToDetail: (connectionId: String, date: String) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory()
    )
) {
    // Den aktuellen Zustand aus dem ViewModel laden
    val uiState by viewModel.uiState.collectAsState()
    // Den Context holen, um später nach Berechtigungen zu fragen
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    // Falls wir in einer Activity sind, speichern wir uns die Referenz
    val activity = context as? Activity
    MyApplicationTheme {
        // Grundgerüst der Seite mit oberer Leiste
        Scaffold(
            topBar = {
                AppTopBar(title = "DB-Navigator-Alternative")
            }
        ) { innerPadding ->
            // Alle Sektionen schön untereinander auflisten
            Column(
                modifier = Modifier
                    .padding(innerPadding) // Abstand nach oben zur Leiste
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Alles scrollbar machen
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp) // Ein bisschen Platz zwischen den Elementen
            ) {
                // # Suche (Von / Bis)
                SearchSection(
                    fromTextFieldState = uiState.fromTextFieldState,
                    toTextFieldState = uiState.toTextFieldState,
                    locationNeeded = uiState.locationNeeded,
                    fromSearchResultValue = uiState.fromSearchResult,
                    toSearchResultValue = uiState.toSearchResult,
                    onFromChanged = { viewModel.onFromChanged(it) },
                    onToChange = { viewModel.onToChanged(it) },
                    onFromItemSelected = { viewModel.onFromItemSelected(it) },
                    onToItemSelected = { viewModel.onToItemSelected(it) },
                    onLocationDismissed = { viewModel.onLocationDismissed() },
                    onLocationClick = { viewModel.onLocationNeeded() },
                    onLocationAccepted = { viewModel.onLocationAccepted() }
                )

                // # Datum wählen
                DateTimeSection(
                    dateText = uiState.date,
                    showDatePicker = uiState.showDatePicker,
                    onDateClick = { viewModel.toggleDatePicker(true) },
                    onDateSelected = { viewModel.onDateSelected(it) },
                    onDismiss = { viewModel.toggleDatePicker(false) }
                )

                // # Uhrzeit und Abfahrt/Ankunft
                ArrivalDepartureSection(
                    isArrival = uiState.isArrival,
                    timeText = uiState.time,
                    showTimePicker = uiState.showTimePicker,
                    onArrivalChange = { viewModel.toggleArrival(it) },
                    onTimeClick = { viewModel.toggleTimePicker(true) },
                    onTimeSelected = { h, m -> viewModel.onTimeSelected(h, m) },
                    onDismiss = { viewModel.toggleTimePicker(false) }
                )

                // # Ticket-Optionen (z.B. D-Ticket)
                TicketOptionSection(
                    onlyDTicket = uiState.onlyDTicket,
                    onToggle = { viewModel.toggleOnlyDTicket(it) }
                )

                // # Der Such-Button
                SearchButton(
                    onClick = {
                        onNavigateToSearch(
                            uiState.fromLocation?.locationId,
                            uiState.toLocation?.locationId,
                            uiState.date,
                            uiState.onlyDTicket
                        )
                    }
                )

                // # Favoriten-Liste
                FavoritesSection(
                    favorites = uiState.favorites,
                    onFavoriteClick = { viewModel.onFavoriteClicked(it, onNavigateToDetail) },
                    onDeleteFavorite = { viewModel.onDeleteFavorite(it) }
                )
            }
        }
    }
}


@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(
            onNavigateToSearch = { _, _, _, _ -> },
            onNavigateToDetail = { _, _ -> }
        )
    }
}
