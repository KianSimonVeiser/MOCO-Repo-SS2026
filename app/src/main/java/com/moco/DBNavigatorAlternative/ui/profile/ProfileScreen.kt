package com.moco.DBNavigatorAlternative.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.ui.generalUse.AppBottomBar

/**
 * Zentraler Container für die Profil-Funktionalität.
 * Verwaltet den Zustand (Anmeldung, Registrierung, Eingeloggt).
 */
@Composable
fun ProfileScreen() {
    var screenState by remember { mutableStateOf("login") }
    var popupMessage by remember { mutableStateOf<String?>(null) }
    var showEmailDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { 
            CustomProfileTopBar(title = if (screenState == "registration") "Registrierung" else "Profil") 
        },
        bottomBar = { AppBottomBar() }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
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
                    onLogoutClick = { 
                        popupMessage = "Erfolgreich Abgemeldet"
                        screenState = "login" 
                    }
                )
            }
        }
    }

    // Custom Popup Dialog based on wireframe
    popupMessage?.let { message ->
        ProfilePopup(
            text = message,
            onDismiss = { popupMessage = null }
        )
    }

    // Email Input Dialog for Forgot Password
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

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
