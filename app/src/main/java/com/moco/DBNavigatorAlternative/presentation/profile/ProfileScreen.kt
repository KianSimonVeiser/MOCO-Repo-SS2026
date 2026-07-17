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
 */
@Composable
fun ProfileScreen() {
    var screenState by remember { mutableStateOf("login") }
    var popupMessage by remember { mutableStateOf<String?>(null) }
    var showEmailDialog by remember { mutableStateOf(false) }
    
    var currentUsername by remember { mutableStateOf("") }
    var currentEmail by remember { mutableStateOf("") }

    Scaffold(
        topBar = { 
            CustomProfileTopBar(
                title = when(screenState) {
                    "registration" -> "Registrierung"
                    "settings" -> "Einstellungen"
                    else -> "Profil"
                }
            ) 
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (screenState) {
                "login" -> LoginScreen(
                    onLoginSuccess = { username, email ->
                        currentUsername = username
                        currentEmail = email
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
                    username = currentUsername,
                    email = currentEmail,
                    onSettingsClick = { screenState = "settings" },
                    onLogoutClick = { 
                        popupMessage = "Erfolgreich Abgemeldet"
                        screenState = "login" 
                    }
                )
                "settings" -> SettingsScreen(
                    currentUsername = currentUsername,
                    currentEmail = currentEmail,
                    onBackClick = { screenState = "loggedIn" },
                    onAccountDeleted = {
                        popupMessage = "Dein Konto wurde gelöscht"
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
