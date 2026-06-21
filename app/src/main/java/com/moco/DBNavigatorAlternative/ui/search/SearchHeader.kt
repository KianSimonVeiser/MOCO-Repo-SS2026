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

@Composable
fun SearchHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchSection()
        DateTimeSection()
        ArrivalDepartureSection()
        TicketOptionSection()
        SearchButton()
    }
}
