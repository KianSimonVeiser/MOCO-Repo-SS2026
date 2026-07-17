package com.moco.DBNavigatorAlternative.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Eine einheitliche Karte (Kästchen) für den Profilbereich, 
 * passend zum Design der Reiseplanung (SearchSection).
 */
@Composable
fun ProfileSectionCard(
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            content()
        }
    }
}

/**
 * Ein modernisiertes Eingabefeld im Stil der restlichen App.
 */
@Composable
fun ProfileInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String, 
    isPassword: Boolean = false,
    label: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label ?: placeholder) },
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        shape = MaterialTheme.shapes.medium,
        singleLine = true
    )
}

/**
 * Ein einheitlich gestalteter Button für den Profilbereich.
 */
@Composable
fun ProfileButton(
    text: String, 
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge, fontSize = 18.sp)
    }
}

/**
 * Ein standardisiertes Informations-Popup.
 */
@Composable
fun ProfilePopup(
    text: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        title = {
            Text(text = "Information", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        },
        shape = MaterialTheme.shapes.large
    )
}

/**
 * Ein Dialog zur Eingabe der E-Mail-Adresse für die "Passwort vergessen"-Funktion.
 */
@Composable
fun EmailInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Passwort zurücksetzen", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Geben Sie Ihre E-Mail-Adresse ein, um einen Link zum Zurücksetzen zu erhalten.")
                ProfileInputField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "E-Mail Adresse"
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(email) }) {
                Text("Senden")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        },
        shape = MaterialTheme.shapes.large
    )
}
