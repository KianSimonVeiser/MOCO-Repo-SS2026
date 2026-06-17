package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchHeader() {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, tonalElevation = 2.dp) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SearchField(label = "Von", placeholder = "Startbahnhof")
            SearchField(label = "Bis", placeholder = "Zielbahnhof")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = false, onClick = {}, label = { Text("Früher") })
                FilterChip(selected = true, onClick = {}, label = { Text("Jetzt") })
                FilterChip(selected = false, onClick = {}, label = { Text("Später") })
            }
        }
    }
}

@Composable
fun SearchField(label: String, placeholder: String) {
    OutlinedTextField(
        value = "", onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        shape = MaterialTheme.shapes.medium
    )
}