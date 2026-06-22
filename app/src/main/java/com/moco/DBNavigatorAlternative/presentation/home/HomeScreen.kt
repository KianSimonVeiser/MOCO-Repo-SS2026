package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppBottomBar
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

/**
 * Der HomeScreen ist nun der "Einstiegspunkt".
 * Er verknüpft die UI mit dem HomeViewModel .
 */
@Composable
fun HomeScreen(
    // Das ViewModel wird hier automatisch instanziiert
    viewModel: HomeViewModel = viewModel()
) {
    // Wir beobachten den Zustand des ViewModels.
    // Jede Änderung im ViewModel triggert hier ein Neuzeichnen der UI.
    val uiState by viewModel.uiState.collectAsState()

    MyApplicationTheme {
        HomeScreenContent(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

/**
 * Der eigentliche Inhalt der Startseite.
 * Wir übergeben den uiState und das viewModel an die Unter-Komponenten.
 */
@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    viewModel: HomeViewModel
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "DB-Navigator Alternative"
            )
        }
        // Hinweis: Die BottomBar wird jetzt zentral über die AppNavigation gesteuert
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Wir geben nur die benötigten Daten und Aktionen an die Sektionen weiter
            // Das nennt man "State Hoisting"
            SearchSection(
                fromValue = uiState.from,
                toValue = uiState.to,
                onFromChange = { viewModel.onFromChanged(it) },
                onToChange = { viewModel.onToChanged(it) },
                modifier = Modifier.padding(top = 16.dp)
            )

            DateTimeSection(
                dateText = uiState.date,
                showDatePicker = uiState.showDatePicker,
                onDateClick = { viewModel.toggleDatePicker(true) },
                onDateSelected = { viewModel.onDateSelected(it) },
                onDismiss = { viewModel.toggleDatePicker(false) },
                modifier = Modifier.padding(top = 16.dp)
            )

            ArrivalDepartureSection(
                isArrival = uiState.isArrival,
                timeText = uiState.time,
                showTimePicker = uiState.showTimePicker,
                onArrivalChange = { viewModel.toggleArrival(it) },
                onTimeClick = { viewModel.toggleTimePicker(true) },
                onTimeSelected = { h, m -> viewModel.onTimeSelected(h, m) },
                onDismiss = { viewModel.toggleTimePicker(false) },
                modifier = Modifier.padding(top = 16.dp)
            )

            TicketOptionSection(
                onlyDTicket = uiState.onlyDTicket,
                onToggle = { viewModel.toggleOnlyDTicket(it) },
                modifier = Modifier.padding(top = 16.dp)
            )

            SearchButton(
                modifier = Modifier.padding(top = 16.dp)
            )

            FavoritesSection(
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}