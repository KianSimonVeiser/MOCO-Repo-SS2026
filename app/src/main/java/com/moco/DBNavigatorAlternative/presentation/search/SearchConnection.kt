package com.moco.DBNavigatorAlternative.presentation.search

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppNavigation
import com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme

class SearchConnection : ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                AppNavigation()
                ConnectionSelectionScreen()
            }
        }
    }
}