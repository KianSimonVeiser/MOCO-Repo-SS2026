package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailOverlayCards(
    historicalPunctualityScore: Float?,
    onCommentsClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HistoricalPunctualityCard(
                punctualityScore = historicalPunctualityScore
            )

            CommentsCard(
                onClick = onCommentsClick
            )
        }
    }
}

@Composable
private fun HistoricalPunctualityCard(
    punctualityScore: Float?
) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .height(90.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Historische Pünktlichkeit",
                    fontSize = 20.sp
                )

                TrainPunctualityCard(
                    punctualityScore = punctualityScore
                )
            }
        }
    }
}

@Composable
private fun CommentsCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .height(90.dp),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Kommentare",
                fontSize = 20.sp
            )
        }
    }
}