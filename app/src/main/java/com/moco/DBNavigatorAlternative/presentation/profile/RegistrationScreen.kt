package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.R

/**
 * Registrierungsbildschirm zur Erstellung neuer Benutzerkonten.
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
        ProfileSectionCard(title = stringResource(id = R.string.register_button)) {
            ProfileInputField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                placeholder = stringResource(id = R.string.username_label)
            )
            
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
            
            ProfileInputField(
                value = uiState.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                placeholder = stringResource(id = R.string.confirm_password_label), 
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ProfileButton(
                    text = stringResource(id = R.string.register_button),
                    containerColor = MaterialTheme.colorScheme.tertiary, // Nutzt das Pastel-Grün
                    onClick = { viewModel.registerUser() }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.already_have_account),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }

    uiState.errorMessage?.let {
        ProfilePopup(text = it, onDismiss = { viewModel.clearError() })
    }
}
