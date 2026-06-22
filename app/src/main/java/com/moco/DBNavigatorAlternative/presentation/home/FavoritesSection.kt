package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * DIE FAVORITEN-LISTE
 * Zeigt gespeicherte Verbindungen in einer übersichtlichen Karte an.
 */
@Composable
fun FavoritesSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Große Überschrift für die Favoriten
        Text(
            text = "Meine Favoriten",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Eine ElevatedCard fasst alle Favoriten-Einträge zusammen
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                // Einzelne Favoriten-Einträge mit Trennlinien dazwischen
                FavoriteItem("Darmstadt Hbf → Frankfurt Hbf")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                
                FavoriteItem("Berlin Hbf → München Hbf")
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                
                FavoriteItem("Hamburg Hbf → Köln Hbf")
            }
        }
    }
}

/**
 * EIN EINZELNER FAVORIT
 * Nutzt das Material 3 'ListItem', um Text und Icon perfekt auszurichten.
 */
@Composable
fun FavoriteItem(text: String) {
    ListItem(
        // Der Name der Verbindung
        headlineContent = { Text(text) },
        // Das Stern-Icon links (Gold gefärbt)
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700)
            )
        },
        // Wir machen den Hintergrund transparent, da die Karte schon eine Farbe hat
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}
