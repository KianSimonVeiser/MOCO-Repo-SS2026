package com.moco.DBNavigatorAlternative.ui.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.moco.DBNavigatorAlternative.model.Connection
import com.moco.DBNavigatorAlternative.ui.detail.DetailScreen
import com.moco.DBNavigatorAlternative.ui.generalUse.AppBottomBar
import com.moco.DBNavigatorAlternative.ui.generalUse.AppTopBar

@Composable
fun ConnectionSelectionScreen() {
    val connections = getMockConnections()
    var selectedConnection by remember { mutableStateOf<Connection?>(null) }

    if (selectedConnection != null) {
        // Zeige Detailansicht, wenn eine Verbindung ausgewählt wurde
        DetailScreen(connection = selectedConnection!!)
        
        // Erlaube Zurückgehen zur Liste
        BackHandler {
            selectedConnection = null
        }
    } else {
        // Zeige die Liste der Verbindungen
        Scaffold(
            topBar = { AppTopBar(title = "Verbindungen") },
            bottomBar = { AppBottomBar() }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    SearchHeader()
                }
                items(connections) { connection ->
                    ConnectionCard(
                        connection = connection,
                        onClick = { selectedConnection = connection }
                    )
                }
            }
        }
    }
}
