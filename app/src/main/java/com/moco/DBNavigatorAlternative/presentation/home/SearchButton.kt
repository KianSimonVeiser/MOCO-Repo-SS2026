package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * DER HAUPT-BUTTON
 * Ein auffälliger Button zum Starten der Suche, gestaltet nach Material 3 Prinzipien.
 */
@Composable
fun SearchButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {} // Was passiert beim Klick?
) {
    // Ein FilledButton ist der auffälligste Button-Typ (Primär-Aktion)
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp), // Standardhöhe für gute Klickbarkeit
        shape = MaterialTheme.shapes.extraLarge, // Sehr rundes Design (Pillenform)
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,    // Hintergrundfarbe (Primär)
            contentColor = MaterialTheme.colorScheme.onPrimary     // Text/Icon Farbe
        )
    ) {
        // Ein Icon zur visuellen Unterstützung des Buttons
        Icon(Icons.Default.Search, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        // Der Beschriftungstext in Großbuchstaben (Label-Stil)
        Text(
            text = "VERBINDUNGEN SUCHEN",
            style = MaterialTheme.typography.labelLarge
        )
    }
}
