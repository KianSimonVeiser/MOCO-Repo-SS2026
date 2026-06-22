package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Der Bildschirm für den angemeldeten Benutzer.
 * Zeigt die Profilinformationen und ermöglicht die Abmeldung.
 * Implementiert das Design gemäß dem Wireframe "Angemeldet.png".
 * 
 * @param username Der anzuzeigende Nutzername des angemeldeten Benutzers.
 * @param email Die anzuzeigende E-Mail-Adresse des angemeldeten Benutzers.
 * @param onLogoutClick Callback für die Abmeldung.
 */
@Composable
fun LoggedInScreen(
    username: String,
    email: String,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Großer Titel zur Bestätigung des Login-Status
        Spacer(modifier = Modifier.height(100.dp))
        Text("Angemeldet", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Sektion zur Anzeige der Nutzerinformationen (Nutzername und E-Mail)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Nutzername:", fontWeight = FontWeight.Bold)
            Text(text = username, fontSize = 20.sp)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(text = "E-Mail Adresse:", fontWeight = FontWeight.Bold)
            Text(text = email, fontSize = 20.sp)
        }
        
        Spacer(modifier = Modifier.height(64.dp))

        // Grauer Abmelde-Button laut Design-Vorgabe
        ProfileButton(
            text = "Abmelden",
            color = Color(0xFFD9D9D9),
            onClick = onLogoutClick
        )
    }
}
