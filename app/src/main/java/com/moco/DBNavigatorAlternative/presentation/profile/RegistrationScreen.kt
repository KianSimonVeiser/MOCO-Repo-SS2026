package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

    if (uiState.isSuccess) {
        LaunchedEffect(Unit) {
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileSectionCard(title = "Registrierung") {
            ProfileInputField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                placeholder = "Nutzername"
            )
            
            ProfileInputField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = "E-Mail"
            )
            
            ProfileInputField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = "Passwort", 
                isPassword = true
            )
            
            ProfileInputField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                placeholder = "Passwort wiederholen", 
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                ProfileButton(
                    text = "Konto erstellen",
                    onClick = { viewModel.registerUser() }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Bereits ein Konto? Zurück zur Anmeldung",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }

    uiState.errorMessage?.let {
        ProfilePopup(text = it, onDismiss = { viewModel.clearError() })
    }
}
