package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moco.DBNavigatorAlternative.ui.generalUse.AppBottomBar
import com.moco.DBNavigatorAlternative.ui.generalUse.AppTopBar

@Composable
fun ConnectionSelectionScreen() {
    val connections = getMockConnections()

    Scaffold(
        topBar = { AppTopBar(title = "Verbindungen") },
        bottomBar = { AppBottomBar() }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchHeader()
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(connections) { connection ->
                    ConnectionCard(connection = connection)
                }
            }
        }
    }
}