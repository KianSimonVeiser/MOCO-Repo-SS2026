package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Der Registrierungsbildschirm zum Erstellen eines neuen Benutzerkontos.
 * Implementiert das Design gemäß dem Wireframe "Registrierung.png".
 * 
 * @param onRegisterSuccess Callback, wenn die Registrierung erfolgreich abgeschlossen wurde.
 * @param onBackToLogin Callback, um zum Login-Bildschirm zurückzukehren.
 */
@Composable
fun RegistrationScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Abstand zum Header
        Spacer(modifier = Modifier.height(80.dp))
        
        // Eingabefeld für den gewünschten Nutzernamen
        ProfileInputField(placeholder = "Nutzername")
        Spacer(modifier = Modifier.height(12.dp))
        
        // Eingabefeld für E-Mail
        ProfileInputField(placeholder = "E-Mail")
        Spacer(modifier = Modifier.height(12.dp))
        
        // Eingabefeld für das Passwort
        ProfileInputField(placeholder = "Passwort", isPassword = true)
        Spacer(modifier = Modifier.height(12.dp))
        
        // Passwortbestätigung zur Vermeidung von Tippfehlern
        ProfileInputField(placeholder = "Passwort wiederholen", isPassword = true)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Gräulicher/Grünlicher Button zum Erstellen des Kontos laut Wireframe
        ProfileButton(
            text = "Konto erstellen",
            color = Color(0xFFD5E8D4),
            onClick = onRegisterSuccess
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Option zur Rückkehr zum Login-Screen
        Text(
            text = "Zurück zur Anmeldung",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }
}
