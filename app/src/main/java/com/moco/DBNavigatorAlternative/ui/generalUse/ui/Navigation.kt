package com.moco.DBNavigatorAlternative.ui.generalUse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moco.DBNavigatorAlternative.ui.home.HomeScreen
import com.moco.DBNavigatorAlternative.ui.search.ConnectionSelectionScreen
import com.moco.DBNavigatorAlternative.ui.detail.DetailScreen
import com.moco.DBNavigatorAlternative.ui.detail.previewConnection
import com.moco.DBNavigatorAlternative.ui.detail.previewCommentList

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
            composable("detail") { DetailScreen(
                connection = previewConnection,
                comments = previewCommentList
            ) }
            composable("profile") {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Profil (Noch nicht fertig)")
                }
            }
        }
    }
}