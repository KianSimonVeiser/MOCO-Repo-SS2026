package com.moco.DBNavigatorAlternative.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection

// # Favoriten
// Hier zeigen wir die gespeicherten Verbindungen an.
@Composable
fun FavoritesSection(
    favorites: List<FavoriteConnection>,
    onFavoriteClick: (FavoriteConnection) -> Unit,
    onDeleteFavorite: (FavoriteConnection) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (favorites.isEmpty()) return // Wenn keine Favoriten da sind, zeigen wir nichts an

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Meine Favoriten",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                favorites.forEachIndexed { index, favorite ->
                    FavoriteItem(
                        text = "${favorite.fromStation} → ${favorite.toStation}",
                        onClick = { onFavoriteClick(favorite) },
                        onDeleteClick = { onDeleteFavorite(favorite) }
                    )
                    
                    // Trennlinie zwischen den Favoriten
                    if (index < favorites.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

// Ein einzelner Eintrag in der Favoritenliste
@Composable
private fun FavoriteItem(
    text: String, 
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        
        // Button zum Löschen des Favoriten
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Default.Delete, 
                contentDescription = "Löschen",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

