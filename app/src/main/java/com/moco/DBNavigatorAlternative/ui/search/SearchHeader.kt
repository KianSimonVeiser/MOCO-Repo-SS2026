package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.ui.home.SearchSection
import com.moco.DBNavigatorAlternative.ui.home.DateTimeSection
import com.moco.DBNavigatorAlternative.ui.home.ArrivalDepartureSection
import com.moco.DBNavigatorAlternative.ui.home.SearchButton
import com.moco.DBNavigatorAlternative.ui.home.TicketOptionSection

/**
 * Der SearchHeader bündelt alle UI-Komponenten für die Verbindungssuche.
 * Er integriert die Sektionen für Start/Ziel, Datum/Uhrzeit, Reisende und Ticket-Optionen.
 */
@Composable
fun SearchHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        // Abstand zwischen den einzelnen Such-Sektionen
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Eingabebereich für Start und Ziel
        SearchSection()
        
        // Auswahl für Datum und Uhrzeit
        DateTimeSection()
        
        // Auswahl zwischen Abfahrt und Ankunft
        ArrivalDepartureSection()
        
        // Optionen für Reisende und Ticket-Klassen
        TicketOptionSection()
        
        // Button zum Auslösen der Suche
        SearchButton()
    }
}
