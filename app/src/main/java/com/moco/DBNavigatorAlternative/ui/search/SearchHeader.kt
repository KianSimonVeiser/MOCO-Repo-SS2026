package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.runtime.Composable
import com.moco.DBNavigatorAlternative.ui.home.SearchSection
import com.moco.DBNavigatorAlternative.ui.home.DateTimeSection
import com.moco.DBNavigatorAlternative.ui.home.ArrivalDepartureSection
import com.moco.DBNavigatorAlternative.ui.home.SearchButton
import com.moco.DBNavigatorAlternative.ui.home.TicketOptionSection

@Composable
fun SearchHeader() {
    SearchSection()
    DateTimeSection()
    ArrivalDepartureSection()
    TicketOptionSection()
    SearchButton()
}