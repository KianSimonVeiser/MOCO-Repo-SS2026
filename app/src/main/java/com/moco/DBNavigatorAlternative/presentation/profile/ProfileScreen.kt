package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.moco.DBNavigatorAlternative.R
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar

/**
 * Orchestrator-Komponente für die profilbezogenen Ansichten.
 */
@Composable
fun ProfileScreen() {
    var screenState by remember { mutableStateOf("login") }
    var popupMessage by remember { mutableStateOf<String?>(null) }
    var showEmailDialog by remember { mutableStateOf(false) }

    val currentUser by UserRepository.currentUser.collectAsState()

    val currentUsername = currentUser?.username ?: ""
    val currentEmail = currentUser?.email ?: ""

    val currentView = if (currentUser != null) {
        if (screenState == "settings") "settings" else "loggedIn"
    } else {
        screenState
    }

    Scaffold(
        topBar = { 
            val title = when (currentView) {
                "registration" -> stringResource(id = R.string.register_button)
                "settings" -> stringResource(id = R.string.settings_title)
                else -> stringResource(id = R.string.profile_title)
            }
            AppTopBar(title = title)
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (currentView) {
                "login" -> LoginScreen(
                    onLoginSuccess = { _, _ ->
                        popupMessage = "Willkommen zurück!"
                    },
                    onRegisterClick = { screenState = "registration" },
                    onForgotPasswordClick = { showEmailDialog = true }
                )
                "registration" -> RegistrationScreen(
                    onRegisterSuccess = { 
                        popupMessage = "Registrierung erfolgreich."
                        screenState = "login" 
                    },
                    onBackToLogin = { screenState = "login" }
                )
                "loggedIn" -> LoggedInScreen(
                    username = currentUsername,
                    email = currentEmail,
                    onSettingsClick = { screenState = "settings" },
                    onLogoutClick = { 
                        UserRepository.setUser(null)
                        popupMessage = "Abmeldung erfolgreich."
                        screenState = "login" 
                    }
                )
                "settings" -> SettingsScreen(
                    currentUsername = currentUsername,
                    currentEmail = currentEmail,
                    onBackClick = { screenState = "loggedIn" },
                    onAccountDeleted = {
                        popupMessage = "Konto gelöscht."
                        UserRepository.setUser(null)
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
                popupMessage = "Link versendet."
            }
        )
    }
}
