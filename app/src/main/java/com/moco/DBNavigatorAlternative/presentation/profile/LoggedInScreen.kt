package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.R

/**
 * Profilansicht für angemeldete Benutzer.
 */
@Composable
fun LoggedInScreen(
    username: String,
    email: String,
    onSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = stringResource(id = R.string.welcome_back), 
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        ProfileSectionCard(title = stringResource(id = R.string.profile_title)) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(id = R.string.username_label), style = MaterialTheme.typography.labelMedium)
                Text(text = username, style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = stringResource(id = R.string.email_label), style = MaterialTheme.typography.labelMedium)
                Text(text = email, style = MaterialTheme.typography.bodyLarge, fontSize = 18.sp)
            }
        }
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileButton(
                text = stringResource(id = R.string.settings_title),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = onSettingsClick
            )

            ProfileButton(
                text = stringResource(id = R.string.logout_button),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                onClick = onLogoutClick
            )
        }
    }
}
