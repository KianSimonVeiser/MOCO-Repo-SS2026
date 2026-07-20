package com.moco.DBNavigatorAlternative.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.R
import com.moco.DBNavigatorAlternative.presentation.detail.DetailScreen
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar

/**
 * Der Hauptbildschirm für die Verbindungsauswahl.
 * Er verwaltet den Wechsel zwischen der Suchergebnisliste und der Detailansicht einer Verbindung.
 */
@Composable
fun ConnectionSelectionScreen(
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
) {
    // Status der UI aus dem ViewModel beziehen
    val connections = viewModel.connections
    val selectedConnection = viewModel.selectedConnection

    if (selectedConnection != null) {
        // Zeige die Detailansicht, wenn eine Verbindung ausgewählt wurde
        DetailScreen(
            connection = selectedConnection
        )
        
        // Fängt den Zurück-Button ab, um zur Liste zurückzukehren statt die App zu schließen
        BackHandler {
            viewModel.onConnectionSelected(null)
        }
    } else {
        // Standardansicht: Liste der Suchergebnisse
        Scaffold(
            topBar = { AppTopBar(title = stringResource(id = R.string.connections_title)) }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Der Suchbereich wird als erstes Element in der Liste mitgescrollt
                item {
                    SearchHeader(viewModel = viewModel)
                }
                
                // Dynamische Liste der Zugverbindungen
                items(connections) { connection ->
                    // Starte das Laden der Pünktlichkeitsdaten und Bahnhofsbewertungen
                    LaunchedEffect(connection.id) {
                        viewModel.loadPunctualityInfo(connection)
                        connection.segments.firstOrNull()?.departureStop?.id?.let {
                            viewModel.loadStationRating(it)
                        }
                    }

                    ConnectionCard(
                        connection = connection,
                        punctualityInfo = viewModel.getPunctualityInfo(connection),
                        stationRating = connection.segments.firstOrNull()?.departureStop?.id?.let {
                            viewModel.getStationRating(it)
                        }
                    ) {
                        viewModel.onConnectionSelected(connection)
                    }
                }
            }
        }
    }
}
