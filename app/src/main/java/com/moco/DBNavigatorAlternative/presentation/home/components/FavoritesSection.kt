package com.moco.DBNavigatorAlternative.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection

/**
 * DIE FAVORITEN-LISTE
 * Zeigt gespeicherte Verbindungen dynamisch aus der Cloud an.
 */
@Composable
fun FavoritesSection(
    favorites: List<FavoriteConnection>,
    onFavoriteClick: (FavoriteConnection) -> Unit,
    modifier: Modifier = Modifier
) {
    if (favorites.isEmpty()) return // Zeige nichts an, wenn keine Favoriten da sind

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
                        onClick = { onFavoriteClick(favorite) }
                    )
                    
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

/**
 * Ein einzelner klickbarer Favoriten-Eintrag.
 */
@Composable
private fun FavoriteItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}
