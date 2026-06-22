package com.moco.DBNavigatorAlternative.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Ein spezialisiertes Eingabefeld für die Profil-Screens.
 * Zeichnet sich durch einen grauen Hintergrund und einen schwarzen Rahmen aus (Wireframe-Design).
 * 
 * @param placeholder Der Text, der angezeigt wird, wenn das Feld leer ist.
 * @param isPassword Wenn true, wird die Eingabe maskiert (z.B. für Passwörter).
 */
@Composable
fun ProfileInputField(placeholder: String, isPassword: Boolean = false) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text(placeholder, color = Color.Gray, fontSize = 18.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFD9D9D9),
            focusedContainerColor = Color(0xFFD9D9D9),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

/**
 * Ein einheitlich gestalteter Button für den Profilbereich.
 * Nutzt eine Box mit Hintergrundfarbe und Rahmen, um das spezifische Design des Wireframes umzusetzen.
 * 
 * @param text Die Beschriftung des Buttons.
 * @param color Die Hintergrundfarbe des Buttons.
 * @param onClick Die Aktion, die beim Klicken ausgeführt wird.
 */
@Composable
fun ProfileButton(text: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 22.sp, color = Color.Black)
    }
}

/**
 * Ein modales Informations-Popup.
 * Entspricht dem Design des "Profil Pop-Ups.png" Wireframes mit grauem Hintergrund 
 * und einem Schließen-Button oben rechts.
 * 
 * @param text Die im Popup anzuzeigende Nachricht.
 * @param onDismiss Callback zum Schließen des Popups.
 */
@Composable
fun ProfilePopup(
    text: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFE5E5E5), RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            // Schließen-Icon oben rechts
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd).size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Schließen",
                    tint = Color.Black
                )
            }

            // Der eigentliche Textinhalt des Popups
            Text(
                text = text,
                fontSize = 18.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        }
    }
}

/**
 * Ein Dialog zur Eingabe der E-Mail-Adresse für die "Passwort vergessen"-Funktion.
 * 
 * @param onDismiss Callback zum Abbrechen/Schließen.
 * @param onConfirm Callback, wenn die E-Mail gesendet werden soll.
 */
@Composable
fun EmailInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Passwort zurücksetzen", 
                    fontSize = 20.sp, 
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Nutzt das standardisierte Eingabefeld
                ProfileInputField(placeholder = "E-Mail Adresse")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Nutzt den standardisierten Button
                ProfileButton(
                    text = "Senden",
                    color = Color(0xFFD9D9D9),
                    onClick = { onConfirm("") } // E-Mail könnte hier aus einem State gelesen werden
                )
            }
        }
    }
}
