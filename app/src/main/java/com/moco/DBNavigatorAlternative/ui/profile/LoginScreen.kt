package com.moco.DBNavigatorAlternative.ui.profile

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
        Spacer(modifier = Modifier.height(150.dp))
        ProfileInputField(placeholder = "E-Mail")
        Spacer(modifier = Modifier.height(12.dp))
        ProfileInputField(placeholder = "Passwort", isPassword = true)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        ProfileButton(
            text = "Anmelden",
            color = Color(0xFFFFC1C1),
            onClick = onLoginClick
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Neuen Account\nErstellen",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.clickable { onRegisterClick() }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(
            text = "Passwort vergessen?",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp).clickable { onForgotPasswordClick() }
        )
    }
}
