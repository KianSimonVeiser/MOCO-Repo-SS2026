package com.moco.DBNavigatorAlternative.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.ui.theme.MyApplicationTheme

@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = { HomeBottomBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SearchSection(modifier = Modifier.padding(top = 16.dp))
            DateTimeSection(modifier = Modifier.padding(top = 16.dp))
            ArrivalDepartureSection(modifier = Modifier.padding(top = 16.dp))
            TicketOptionSection(modifier = Modifier.padding(top = 16.dp))
            SearchButton(modifier = Modifier.padding(top = 16.dp))
            FavoritesSection(modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen()
    }
}
