package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Der Login-Bildschirm für die Benutzeranmeldung.
 * Implementiert das Design gemäß dem Wireframe "Anmeldung.png".
 * 
 * @param onLoginClick Callback für den Anmelde-Button.
 * @param onRegisterClick Callback zum Wechseln auf die Registrierungsseite.
 * @param onForgotPasswordClick Callback zum Öffnen des "Passwort vergessen" Dialogs.
 */
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Abstand zum Header
        Spacer(modifier = Modifier.height(150.dp))
        
        // Eingabefeld für E-Mail
        ProfileInputField(placeholder = "E-Mail")
        Spacer(modifier = Modifier.height(12.dp))
        
        // Eingabefeld für Passwort (mit maskierter Eingabe)
        ProfileInputField(placeholder = "Passwort", isPassword = true)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Rötlicher Anmelde-Button laut Wireframe
        ProfileButton(
            text = "Anmelden",
            color = Color(0xFFFFC1C1),
            onClick = onLoginClick
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Option zur Erstellung eines neuen Kontos
        Text(
            text = "Neuen Account\nErstellen",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.clickable { onRegisterClick() }
        )
        
        // Schiebt den nachfolgenden Inhalt ganz nach unten
        Spacer(modifier = Modifier.weight(1f))
        
        // Passwort vergessen Option am unteren Bildschirmrand
        Text(
            text = "Passwort vergessen?",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp).clickable { onForgotPasswordClick() }
        )
    }
}
