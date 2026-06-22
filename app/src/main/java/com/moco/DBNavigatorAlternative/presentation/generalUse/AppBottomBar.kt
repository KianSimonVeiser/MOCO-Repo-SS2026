package com.moco.DBNavigatorAlternative.presentation.generalUse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppBottomBar(
    onAddClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onChartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = onAddClick
                ) {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = "Hinzufügen"
                    )
                }

                IconButton(
                    onClick = onSearchClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Suchen"
                    )
                }

                IconButton(
                    onClick = onChartClick
                ) {
                    Icon(
                        imageVector = Icons.Default.AreaChart,
                        contentDescription = "Statistik"
                    )
                }

                IconButton(
                    onClick = onProfileClick
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profil"
                    )
                }
            }
        }
    )
}