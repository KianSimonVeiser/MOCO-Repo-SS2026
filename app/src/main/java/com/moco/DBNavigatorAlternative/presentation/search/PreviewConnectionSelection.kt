package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

/**
 * Vorschau-Komponente für den ConnectionSelectionScreen in Android Studio.
 * Ermöglicht die visuelle Überprüfung des Designs ohne App-Start.
 */
@Preview(showBackground = true)
@Composable
fun ConnectionSelectionScreenPreview() {
    MyApplicationTheme {
        // Rendert den Hauptbildschirm der Verbindungssuche in der Preview
        ConnectionSelectionScreen()
    }
}
