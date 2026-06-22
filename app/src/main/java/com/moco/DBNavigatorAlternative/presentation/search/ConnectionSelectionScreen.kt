package com.moco.DBNavigatorAlternative.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.moco.DBNavigatorAlternative.presentation.detail.DetailScreen
import com.moco.DBNavigatorAlternative.presentation.detail.previewCommentList
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppBottomBar
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar

/**
 * Der Hauptbildschirm für die Verbindungsauswahl.
 * Er verwaltet den Wechsel zwischen der Suchergebnisliste und der Detailansicht einer Verbindung.
 */
@Composable
fun ConnectionSelectionScreen() {
    // ViewModel-Instanz über remember erhalten (einfaches MVVM-Pattern)
    val viewModel = remember { SearchViewModel() }
    
    // Status der UI aus dem ViewModel beziehen
    val connections = viewModel.connections
    val selectedConnection = viewModel.selectedConnection

    if (selectedConnection != null) {
        // Zeige die Detailansicht, wenn eine Verbindung ausgewählt wurde
        DetailScreen(
            connection = selectedConnection,
            comments = previewCommentList
        )
        
        // Fängt den Zurück-Button ab, um zur Liste zurückzukehren statt die App zu schließen
        BackHandler {
            viewModel.onConnectionSelected(null)
        }
    } else {
        // Standardansicht: Liste der Suchergebnisse
        Scaffold(
            topBar = { AppTopBar(title = "Verbindungen") },
            bottomBar = { AppBottomBar() }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Der Suchbereich wird als erstes Element in der Liste mitgescrollt
                item {
                    SearchHeader()
                }
                
                // Dynamische Liste der Zugverbindungen
                items(connections) { connection ->
                    ConnectionCard(
                        connection = connection,
                        onClick = { viewModel.onConnectionSelected(connection) }
                    )
                }
            }
        }
    }
}
