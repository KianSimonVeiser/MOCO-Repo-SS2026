package com.moco.DBNavigatorAlternative.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// # Ticket-Optionen
// Hier kann man Filter einstellen, zum Beispiel um nur Verbindungen fürs D-Ticket zu sehen.
@Composable
fun TicketOptionSection(
    onlyDTicket: Boolean,       // Ist der Filter an oder aus?
    onToggle: (Boolean) -> Unit,// Was passieren soll, wenn man den Schalter umlegt
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Nur D-Ticket",
                    style = MaterialTheme.typography.titleMedium
                )
                // Kurze Erklärung, was die Option macht
                Text(
                    text = "Verbindungen mit Deutschlandticket filtern",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Der Schalter für die Option
            Switch(
                checked = onlyDTicket,
                onCheckedChange = onToggle
            )
        }
    }
}

