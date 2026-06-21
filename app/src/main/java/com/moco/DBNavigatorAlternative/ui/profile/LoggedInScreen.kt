package com.moco.DBNavigatorAlternative.ui.profile

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
        Spacer(modifier = Modifier.height(100.dp))
        Text("Angemeldet", style = MaterialTheme.typography.headlineLarge)
        
        Spacer(modifier = Modifier.height(40.dp))
        
        // Nutzer Informationen
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

        ProfileButton(
            text = "Abmelden",
            color = Color(0xFFD9D9D9),
            onClick = onLogoutClick
        )
    }
}
