package com.moco.DBNavigatorAlternative.presentation.generalUse

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.moco.DBNavigatorAlternative.presentation.home.HomeScreen
import com.moco.DBNavigatorAlternative.presentation.search.ConnectionSelectionScreen
import com.moco.DBNavigatorAlternative.presentation.detail.DetailScreen
import com.moco.DBNavigatorAlternative.presentation.detail.previewConnection
import com.moco.DBNavigatorAlternative.presentation.profile.ProfileScreen
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
            composable("home") { 
                HomeScreen(
                    onNavigateToSearch = { fromId, toId, date, onlyDTicket ->
                        val encFrom = fromId?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: ""
                        val encTo = toId?.let { URLEncoder.encode(it, StandardCharsets.UTF_8.toString()) } ?: ""
                        navController.navigate("search?fromId=$encFrom&toId=$encTo&date=$date&onlyDTicket=$onlyDTicket")
                    }
                ) 
            }
            
            composable(
                route = "search?fromId={fromId}&toId={toId}&date={date}&onlyDTicket={onlyDTicket}",
                arguments = listOf(
                    navArgument("fromId") { defaultValue = ""; type = NavType.StringType },
                    navArgument("toId") { defaultValue = ""; type = NavType.StringType },
                    navArgument("date") { defaultValue = ""; type = NavType.StringType },
                    navArgument("onlyDTicket") { defaultValue = false; type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val fromId = backStackEntry.arguments?.getString("fromId")?.let { 
                    if (it.isBlank()) null else URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                }
                val toId = backStackEntry.arguments?.getString("toId")?.let {
                    if (it.isBlank()) null else URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                }
                val date = backStackEntry.arguments?.getString("date")
                val onlyDTicket = backStackEntry.arguments?.getBoolean("onlyDTicket") ?: false
                
                ConnectionSelectionScreen(
                    initialFromId = fromId,
                    initialToId = toId,
                    initialDate = date,
                    initialOnlyDTicket = onlyDTicket
                )
            }
            
            composable("profile") { ProfileScreen() }
            composable("detail") { DetailScreen(
                connection = previewConnection
            ) }
        }
    }
}
