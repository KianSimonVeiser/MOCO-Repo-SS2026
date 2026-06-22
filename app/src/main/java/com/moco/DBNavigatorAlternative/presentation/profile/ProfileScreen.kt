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

/**
 * Zentraler Container für die Profil-Funktionalität.
 * Diese Komponente fungiert als Orchestrator und verwaltet den Navigationszustand 
 * zwischen Login, Registrierung und dem angemeldeten Profil-Bereich.
 * Zudem werden hier zentrale UI-Elemente wie Popups und Dialoge gesteuert.
 */
@Composable
fun ProfileScreen() {
    // screenState bestimmt, welche Ansicht aktuell gerendert wird ("login", "registration", "loggedIn")
    var screenState by remember { mutableStateOf("login") }
    
    // popupMessage hält den Text für das Erfolgs-Popup. Wenn null, wird kein Popup angezeigt.
    var popupMessage by remember { mutableStateOf<String?>(null) }
    
    // showEmailDialog steuert die Sichtbarkeit des Dialogs zum Zurücksetzen des Passworts
    var showEmailDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { 
            // Spezifische TopBar für das Profil laut Wireframe
            CustomProfileTopBar(title = if (screenState == "registration") "Registrierung" else "Profil") 
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            // State-basierte Navigation innerhalb des Profil-Bereichs
            when (screenState) {
                "login" -> LoginScreen(
                    onLoginClick = { 
                        popupMessage = "Anmeldung erfolgreich"
                        screenState = "loggedIn"
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
                    // Platzhalterdaten für den angemeldeten Nutzer
                    username = "Max Mustermann",
                    email = "max@mustermann.de",
                    onLogoutClick = { 
                        popupMessage = "Erfolgreich Abgemeldet"
                        screenState = "login" 
                    }
                )
            }
        }
    }

    // Zeigt ein Informations-Popup an, falls eine Nachricht gesetzt wurde
    popupMessage?.let { message ->
        ProfilePopup(
            text = message,
            onDismiss = { popupMessage = null }
        )
    }

    // Zeigt den E-Mail-Eingabedialog an, wenn "Passwort vergessen" geklickt wurde
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
 * Entspricht dem Design des Wireframes mit spezifischer Hintergrundfarbe und zentriertem Text.
 * 
 * @param title Der im Header anzuzeigende Text.
 */
@Composable
fun CustomProfileTopBar(title: String) {
    Surface(
        color = Color(0xFFE2D9FF), // Hellviolette Hintergrundfarbe aus dem Wireframe
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
