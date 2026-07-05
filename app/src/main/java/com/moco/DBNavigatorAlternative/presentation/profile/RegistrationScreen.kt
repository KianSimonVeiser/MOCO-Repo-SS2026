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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Der Registrierungsbildschirm zum Erstellen eines neuen Benutzerkontos.
 */
@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Automatische Weiterleitung bei Erfolg
    if (uiState.isSuccess) {
        LaunchedEffect(Unit) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        
        ProfileInputField(
            value = uiState.username,
            onValueChange = { viewModel.onUsernameChanged(it) },
            placeholder = "Nutzername"
        )
        Spacer(modifier = Modifier.height(12.dp))
        
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
        Spacer(modifier = Modifier.height(12.dp))
        
        ProfileInputField(
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChanged(it) },
            placeholder = "Passwort wiederholen", 
            isPassword = true
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (uiState.isLoading) {
            CircularProgressIndicator(color = Color.Black)
        } else {
            ProfileButton(
                text = "Konto erstellen",
                color = Color(0xFFD5E8D4),
                onClick = { viewModel.registerUser() }
            )
        }

        // Fehlermeldung anzeigen
        uiState.errorMessage?.let {
            ProfilePopup(text = it, onDismiss = { viewModel.clearError() })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Zurück zur Anmeldung",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }
}
