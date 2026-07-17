package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Der Einstellungsbildschirm der Anwendung.
 */
@Composable
fun SettingsScreen(
    currentUsername: String,
    currentEmail: String,
    viewModel: SettingsViewModel = viewModel(),
    onBackClick: () -> Unit,
    onAccountDeleted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.initUserData(currentUsername, currentEmail)
    }

    if (uiState.isDeleted) {
        LaunchedEffect(Unit) {
            onAccountDeleted()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // SEKTION: Profil bearbeiten
        ProfileSectionCard(title = "Profil bearbeiten") {
            ProfileInputField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                placeholder = "Anzeigename"
            )
            
            ProfileInputField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = "Neues Passwort (optional)",
                isPassword = true
            )
            
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                ProfileButton(
                    text = "Änderungen speichern",
                    onClick = { viewModel.saveAllSettings() }
                )
            }
        }

        // SEKTION: Datenverwaltung
        ProfileSectionCard(title = "Datenverwaltung") {
            ProfileButton(
                text = "Meine Kommentare löschen",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = { viewModel.deleteUserComments() }
            )

            ProfileButton(
                text = "Favoriten löschen",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = { viewModel.deleteFavoriteConnections() }
            )
        }

        // Gefahrenzone
        ProfileSectionCard(title = "Gefahrenzone") {
            ProfileButton(
                text = "Konto löschen",
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                onClick = { viewModel.deleteAccount() }
            )
        }

        ProfileButton(
            text = "Zurück zum Profil",
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onBackClick
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (uiState.isSuccess) {
        ProfilePopup(
            text = "Änderungen erfolgreich gespeichert",
            onDismiss = { viewModel.clearStatus() }
        )
    }

    uiState.errorMessage?.let { error ->
        ProfilePopup(
            text = error,
            onDismiss = { viewModel.clearStatus() }
        )
    }
}
