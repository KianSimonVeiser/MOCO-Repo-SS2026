package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar

@Composable
fun ConnectionSelectionScreen() {
    val connections = previewConnections

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Verbindungen"
            )
        },

        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchHeader()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(connections) { connection ->
                    ConnectionCard(
                        connection = connection
                    )
                }
            }
        }
    }
}