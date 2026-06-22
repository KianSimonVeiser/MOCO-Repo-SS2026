package com.moco.DBNavigatorAlternative.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * TICKET-FILTER OPTIONEN
 * Erlaubt dem Nutzer, die Ergebnisse z.B. nach Deutschlandticket-Verbindungen zu filtern.
 */
@Composable
fun TicketOptionSection(
    onlyDTicket: Boolean,       // Status des Schalters (Ein/Aus)
    onToggle: (Boolean) -> Unit,// Funktion zum Ändern des Status
    modifier: Modifier = Modifier
) {
    // Eine ElevatedCard gruppiert den Text und den Schalter
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Text links, Switch rechts
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Hauptüberschrift der Option
                Text(
                    text = "Nur D-Ticket",
                    style = MaterialTheme.typography.titleMedium
                )
                // Eine erklärende Unterzeile (Supporting Text)
                Text(
                    text = "Verbindungen mit Deutschlandticket filtern",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Der Material 3 Switch (Schiebeschalter)
            Switch(
                checked = onlyDTicket,
                onCheckedChange = onToggle
            )
        }
    }
}
