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

/**
 * DER HOMESCREEN
 * Dies ist die Hauptseite der App. Sie ist nach dem MVVM-Muster aufgebaut

 */
@Composable
fun HomeScreen(
    onNavigateToSearch: (fromId: String?, toId: String?, date: String, onlyDTicket: Boolean) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory()
    )
) {
    // Wir beobachten den Zustand der Daten (uiState) aus dem ViewModel
    val uiState by viewModel.uiState.collectAsState()
    // Context wird benötigt, um Berechtigungen wie Standort zu prüfen
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    // Sicherer Cast, falls wir nicht in einer Activity sind (z.B. Preview)
    val activity = context as? Activity
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
                    onClick = {
                        onNavigateToSearch(
                            uiState.fromLocation?.locationId,
                            uiState.toLocation?.locationId,
                            uiState.date,
                            uiState.onlyDTicket
                        )
                    }
                )

                // --- 6. SEKTION: MEINE FAVORITEN ---
                FavoritesSection(
                    favorites = uiState.favorites,
                    onFavoriteClick = { viewModel.onFavoriteClicked(it) }
                )
            }
        }
    }
}
