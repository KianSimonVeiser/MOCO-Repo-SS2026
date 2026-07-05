package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Der Login-Bildschirm für die Benutzeranmeldung.
 * 
 * @param onLoginSuccess Callback für erfolgreiche Anmeldung. Übergibt den Benutzernamen.
 * @param onRegisterClick Callback zum Wechseln auf die Registrierungsseite.
 * @param onForgotPasswordClick Callback zum Öffnen des "Passwort vergessen" Dialogs.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (String, String) -> Unit, // Name und Email für den LoggedIn-Screen
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Reaktion auf erfolgreichen Login
    if (uiState.isLoggedIn) {
        LaunchedEffect(Unit) {
            onLoginSuccess(uiState.username, uiState.email)
            viewModel.resetLoginStatus() // Status für das nächste Mal zurücksetzen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        
        ProfileInputField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            placeholder = "E-Mail"
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        ProfileInputField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            placeholder = "Passwort", 
            isPassword = true
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.Black)
        } else {
            ProfileButton(
                text = "Anmelden",
                color = Color(0xFFFFC1C1),
                onClick = { viewModel.login() }
            )
        }

        // Fehlermeldungen (z.B. Falsches Passwort)
        uiState.errorMessage?.let {
            ProfilePopup(text = it, onDismiss = { viewModel.clearError() })
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Neuen Account\nErstellen",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.clickable { onRegisterClick() }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "Passwort vergessen?",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp).clickable { onForgotPasswordClick() }
        )
    }
}
