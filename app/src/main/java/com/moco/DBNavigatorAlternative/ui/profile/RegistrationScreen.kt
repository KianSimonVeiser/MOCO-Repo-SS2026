package com.moco.DBNavigatorAlternative.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        Spacer(modifier = Modifier.height(100.dp))
        ProfileInputField(placeholder = "E-Mail")
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInputField(placeholder = "Passwort", isPassword = true)
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInputField(placeholder = "Passwort wiederholen", isPassword = true)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ProfileButton(
            text = "Konto erstellen",
            color = Color(0xFFD5E8D4),
            onClick = onRegisterSuccess
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Zurück zur Anmeldung",
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.clickable { onBackToLogin() }
        )
    }
}
