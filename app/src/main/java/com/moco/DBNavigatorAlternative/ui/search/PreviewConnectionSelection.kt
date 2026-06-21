package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moco.DBNavigatorAlternative.ui.theme.MyApplicationTheme

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
