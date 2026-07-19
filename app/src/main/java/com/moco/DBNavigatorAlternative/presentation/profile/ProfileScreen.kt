package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.data.UserRepository

/**
 * Zentraler Container für die Profil-Funktionalität.
 */
@Composable
fun ProfileScreen() {
    var screenState by remember { mutableStateOf("login") }
    var popupMessage by remember { mutableStateOf<String?>(null) }
    var showEmailDialog by remember { mutableStateOf(false) }

    // Beobachte den globalen Nutzerstatus hiermit
    val currentUser by UserRepository.currentUser.collectAsState()

    // NEU: Variablen für die Anzeige definieren
    val currentUsername = currentUser?.username ?: ""
    val currentEmail = currentUser?.email ?: ""

    // LOGIK ANPASSEN: Erlaubt den Wechsel zu "settings", auch wenn man eingeloggt ist
    val currentView = if (currentUser != null) {
        if (screenState == "settings") "settings" else "loggedIn"
    } else {
        screenState
    }

    Scaffold(
        topBar = { 
            CustomProfileTopBar(title = if (currentView == "registration") "Registrierung" else "Profil")
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (currentView) {
                "login" -> LoginScreen(
                    onLoginSuccess = { _, _ ->
                        popupMessage = "Anmeldung erfolgreich"
                        // Der Wechsel zu loggedIn passiert automatisch durch currentUser State
                    },
                    onRegisterClick = { screenState = "registration" },
                    onForgotPasswordClick = { showEmailDialog = true }
                )
                "registration" -> RegistrationScreen(
                    onRegisterSuccess = { 
                        popupMessage = "Registrierung Erfolgreich"
                        screenState = "login" 
                    },
                    onBackToLogin = { screenState = "login" }
                )
                "loggedIn" -> LoggedInScreen(
                    username = currentUser?.username ?: "",
                    email = currentUser?.email ?: "",
                    onSettingsClick = { screenState = "settings" },
                    onLogoutClick = { 
                        UserRepository.setUser(null)
                        popupMessage = "Erfolgreich Abgemeldet"
                        screenState = "login" 
                    }
                )
                "settings" -> SettingsScreen(
                    currentUsername = currentUsername, // Nutzt die neue Variable
                    currentEmail = currentEmail,       // Nutzt die neue Variable
                    onBackClick = { screenState = "loggedIn" },
                    onAccountDeleted = {
                        popupMessage = "Dein Konto wurde gelöscht"
                        UserRepository.setUser(null) // Nutzer abmelden
                        screenState = "login"
                    }
                )
            }
        }
    }

    popupMessage?.let { message ->
        ProfilePopup(
            text = message,
            onDismiss = { popupMessage = null }
        )
    }

    if (showEmailDialog) {
        EmailInputDialog(
            onDismiss = { showEmailDialog = false },
            onConfirm = {
                showEmailDialog = false
                popupMessage = "Ihnen Wurde eine E-Mail zur Passwort-Änderung geschickt"
            }
        )
    }
}

/**
 * Eine benutzerdefinierte Top-AppBar für den Profilbereich.
 */
@Composable
fun CustomProfileTopBar(title: String) {
    Surface(
        color = Color(0xFFE2D9FF),
        modifier = Modifier.fillMaxWidth().height(100.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}
