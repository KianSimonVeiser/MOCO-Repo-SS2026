package com.moco.DBNavigatorAlternative.presentation.generalUse

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moco.DBNavigatorAlternative.presentation.home.HomeScreen
import com.moco.DBNavigatorAlternative.presentation.search.ConnectionSelectionScreen
import com.moco.DBNavigatorAlternative.presentation.detail.DetailScreen
import com.moco.DBNavigatorAlternative.presentation.detail.previewConnection
import com.moco.DBNavigatorAlternative.presentation.detail.previewCommentList
import com.moco.DBNavigatorAlternative.presentation.profile.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController() //zentrales steuerelement für die navigation


    Scaffold(
        bottomBar = {
            // Die BottomBar wird nur EINMAL hier definiert
            //routing logik
            AppBottomBar(
                onAddClick = { navController.navigate("home") },
                onSearchClick = { navController.navigate("search") },
                onChartClick = { navController.navigate("detail") },
                onProfileClick = { navController.navigate("profile") }
            )
        }
    ) { innerPadding ->
        // Definition des NavHost: Das Mapping-System der App-Architektur.
        // Er verknüpft eindeutige Bezeichner (Strings als Routes) mit den
        // entsprechenden View-Komponenten (Composables).
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("search") { ConnectionSelectionScreen() }
            composable("profile") { ProfileScreen() }
            composable("detail") { DetailScreen(
                connection = previewConnection,
                comments = previewCommentList
            ) }
        }
    }
}