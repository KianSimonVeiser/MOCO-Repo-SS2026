package com.moco.DBNavigatorAlternative.ui.generalUse

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moco.DBNavigatorAlternative.ui.theme.MyApplicationTheme

/**
 * Diese Datei dient nur dazu, die gesamte App-Struktur inklusive
 * Navigation in Android Studio testbar zu machen.
 */
@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    MyApplicationTheme {
        // Wir rufen einfach die zentrale Navigation auf
        AppNavigation()
    }
}