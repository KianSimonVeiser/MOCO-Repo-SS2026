package com.moco.DBNavigatorAlternative.presentation.generalUse

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

    // Beobachtet die aktuelle Route, um den Zustand der BottomBar zu aktualisieren
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedIndex = when {
        currentRoute == "home" -> 0
        currentRoute?.startsWith("search") == true -> 1
        currentRoute == "detail" -> 2
        currentRoute == "profile" -> 3
        else -> -1
    }

    Scaffold(
        bottomBar = {
            // Die BottomBar wird nur EINMAL hier definiert
            //routing logik
            AppBottomBar(
                selectedItem = selectedIndex,
                onAddClick = { 
                    if (currentRoute != "home") {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onSearchClick = { 
                    if (currentRoute != "search") {
                        navController.navigate("search")
                    }
                },
                onChartClick = { 
                    if (currentRoute != "detail") {
                        navController.navigate("detail")
                    }
                },
                onProfileClick = { 
                    if (currentRoute != "profile") {
                        navController.navigate("profile")
                    }
                }
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
                    },
                    onNavigateToDetail = { connectionId, date ->
                        navController.navigate("detail?connectionId=$connectionId&date=$date")
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
                    initialOnlyDTicket = onlyDTicket,
                    onNavigateToDetail = { navController.navigate("detail") }
                )
            }
            
            composable("profile") { ProfileScreen() }
            composable(
                route = "detail?connectionId={connectionId}&date={date}",
                arguments = listOf(
                    navArgument("connectionId") { defaultValue = ""; type = NavType.StringType },
                    navArgument("date") { defaultValue = ""; type = NavType.StringType }
                )
            ) { backStackEntry ->
                val connectionId = backStackEntry.arguments?.getString("connectionId") ?: ""
                val date = backStackEntry.arguments?.getString("date")
                DetailScreen(
                    connectionId = if (connectionId.isBlank()) null else connectionId,
                    initialDate = date
                )
            }
            
            // Behalte die alte Detail-Route ohne Argumente für die BottomBar bei
            composable("detail") {
                val connection = com.moco.DBNavigatorAlternative.data.SearchStateStore.selectedConnection ?: previewConnection
                DetailScreen(connection = connection)
            }
        }
    }
}
