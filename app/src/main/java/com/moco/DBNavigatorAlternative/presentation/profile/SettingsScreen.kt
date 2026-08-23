package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.R

/**
 * Einstellungen zur Verwaltung des Benutzerprofils und der persönlichen Daten.
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

        // Profilverwaltung
        ProfileSectionCard(title = stringResource(id = R.string.edit_profile)) {
            ProfileInputField(
                value = uiState.username,
                onValueChange = { viewModel.onUsernameChanged(it) },
                placeholder = stringResource(id = R.string.username_label)
            )
            
            ProfileInputField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                placeholder = "Neues Passwort (optional)",
                isPassword = true
            )
            
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ProfileButton(
                    text = stringResource(id = R.string.save_changes),
                    containerColor = Color(0xFFBA1A1A),
                    contentColor = Color.White,
                    onClick = { viewModel.saveAllSettings() }
                )
            }
        }

        // Benutzerkommentare
        ProfileSectionCard(title = stringResource(id = R.string.comments)) {
            if (uiState.userComments.isEmpty()) {
                Text(
                    text = "Keine Kommentare vorhanden.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            } else {
                uiState.userComments.forEachIndexed { index, comment ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = comment.stationName, style = MaterialTheme.typography.labelLarge)
                            Text(text = comment.content, style = MaterialTheme.typography.bodySmall)
                        }
                        IconButton(onClick = { viewModel.deleteSingleComment(comment.commentId) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Löschen", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    if (index < uiState.userComments.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ProfileButton(
                text = stringResource(id = R.string.delete_comments),
                containerColor = MaterialTheme.colorScheme.secondaryContainer, // Pastel-Gelb
                onClick = { viewModel.deleteUserComments() }
            )
        }

        // Favoritenverwaltung
        ProfileSectionCard(title = stringResource(id = R.string.data_management)) {
            ProfileButton(
                text = stringResource(id = R.string.delete_favorites),
                containerColor = MaterialTheme.colorScheme.secondaryContainer, // Pastel-Gelb
                onClick = { viewModel.deleteFavoriteConnections() }
            )
        }

        // Accountlöschung
        ProfileSectionCard(title = stringResource(id = R.string.danger_zone)) {
            ProfileButton(
                text = stringResource(id = R.string.delete_account),
                containerColor = MaterialTheme.colorScheme.error, // Pastel-Rot (Danger)
                onClick = { viewModel.deleteAccount() }
            )
        }

        ProfileButton(
            text = stringResource(id = R.string.back_to_profile),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            onClick = onBackClick
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (uiState.isSuccess) {
        ProfilePopup(
            text = stringResource(id = R.string.save_changes) + " erfolgreich.",
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
