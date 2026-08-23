package com.moco.DBNavigatorAlternative.presentation.generalUse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomBar(
    selectedItem: Int = -1, // -1 means none selected, 0: Add, 1: Search, 2: Chart, 3: Profile
    onAddClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onChartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                IconButton(
                    onClick = onAddClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (selectedItem == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = if (selectedItem == 0) Modifier.size(30.dp) else Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = onSearchClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Suchen",
                        tint = if (selectedItem == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = if (selectedItem == 1) Modifier.size(30.dp) else Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = onChartClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.AreaChart,
                        contentDescription = "Statistik",
                        tint = if (selectedItem == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = if (selectedItem == 2) Modifier.size(30.dp) else Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = onProfileClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil",
                        tint = if (selectedItem == 3) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = if (selectedItem == 3) Modifier.size(30.dp) else Modifier.size(24.dp)
                    )
                }
            }
        },
    )
}