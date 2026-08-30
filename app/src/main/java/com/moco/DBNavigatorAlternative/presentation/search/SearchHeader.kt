package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.data.api.dto.NearbyLocationDto
import com.moco.DBNavigatorAlternative.R
import com.moco.DBNavigatorAlternative.presentation.generalUse.SearchSection
import com.moco.DBNavigatorAlternative.presentation.home.components.TicketOptionSection
import com.moco.DBNavigatorAlternative.presentation.home.components.DateTimeSection

// # Such-Header
// Das ist der obere Bereich der Suchseite. Hier sind alle Felder drin, 
// um die Suche zu verfeinern (Ort, Datum, Filter).
@Composable
fun SearchHeader(
    uiState: SearchUiState,
    onFromChanged: (TextFieldState) -> Unit,
    onToChanged: (TextFieldState) -> Unit,
    onLocationNeeded: () -> Unit,
    onLocationDismissed: () -> Unit,
    onLocationAccepted: () -> Unit,
    onFromItemSelected: (NearbyLocationDto) -> Unit,
    onToItemSelected: (NearbyLocationDto) -> Unit,
    onToggleDatePicker: (Boolean) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onToggleOnlyDTicket: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hier gibt man Start und Ziel ein
        SearchSection(
            fromTextFieldState = uiState.fromTextFieldState,
            locationNeeded = uiState.locationNeeded,
            toTextFieldState = uiState.toTextFieldState,
            fromSearchResultValue = uiState.fromSearchResult,
            toSearchResultValue = uiState.toSearchResult,
            onFromChanged = onFromChanged,
            onToChange = onToChanged,
            onFromItemSelected = onFromItemSelected,
            onToItemSelected = onToItemSelected,
            onLocationClick = onLocationNeeded,
            onLocationDismissed = onLocationDismissed,
            onLocationAccepted = onLocationAccepted
        )

        
        // Datum und Uhrzeit auswählen
        DateTimeSection(
            dateText = uiState.date,
            showDatePicker = uiState.showDatePicker,
            onDateClick = { onToggleDatePicker(true) },
            onDateSelected = onDateSelected,
            onDismiss = { onToggleDatePicker(false) }
        )

        // Nur Verbindungen mit Deutschlandticket anzeigen
        TicketOptionSection(
            onlyDTicket = uiState.onlyDTicket,
            onToggle = onToggleOnlyDTicket
        )

        // Knöpfe für frühere oder spätere Züge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { /* Frühere Verbindungen laden */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(id = R.string.earlier))
            }
            Button(
                onClick = { /* Spätere Verbindungen laden */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(stringResource(id = R.string.later))
            }
        }
    }
}

