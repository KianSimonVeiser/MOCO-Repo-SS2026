package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Der Bildschirm für den angemeldeten Benutzer.
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
            text = "Willkommen zurück!", 
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        ProfileSectionCard(title = "Dein Profil") {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "Nutzername", style = MaterialTheme.typography.labelMedium)
                Text(text = username, style = MaterialTheme.typography.bodyLarge, fontSize = 20.sp)
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = "E-Mail Adresse", style = MaterialTheme.typography.labelMedium)
                Text(text = email, style = MaterialTheme.typography.bodyLarge, fontSize = 18.sp)
            }
        }
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            ProfileButton(
                text = "Einstellungen",
                onClick = onSettingsClick
            )

            ProfileButton(
                text = "Abmelden",
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                onClick = onLogoutClick
            )
        }
    }
}
