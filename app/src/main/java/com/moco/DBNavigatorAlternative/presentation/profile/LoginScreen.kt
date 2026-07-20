package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.R

/**
 * Anmeldebildschirm für registrierte Benutzer.
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoggedIn) {
        LaunchedEffect(Unit) {
            onLoginSuccess(uiState.username, uiState.email)
            viewModel.resetLoginStatus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileSectionCard(title = stringResource(id = R.string.login_button)) {
            ProfileInputField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                placeholder = stringResource(id = R.string.email_label)
            )
            
            ProfileInputField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = stringResource(id = R.string.password_label), 
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ProfileButton(
                    text = stringResource(id = R.string.login_button),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant, // Nutzt das Pastel-Rot
                    onClick = { viewModel.login() }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = stringResource(id = R.string.forgot_password),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onForgotPasswordClick() }
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(id = R.string.no_account_yet),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable { onRegisterClick() }
        )
    }

    uiState.errorMessage?.let {
        ProfilePopup(text = it, onDismiss = { viewModel.clearError() })
    }
}
