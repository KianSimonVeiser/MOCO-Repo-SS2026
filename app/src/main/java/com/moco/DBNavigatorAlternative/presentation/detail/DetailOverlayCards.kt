package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary

@Composable
fun DetailOverlayCards(
    historicalPunctualityScore: Float?,
    bindingLossProbability: Float? = null,
    onCommentsClick: () -> Unit,
    stationRating: StationRatingSummary? = null,
    onRatingSelected: (Int) -> Unit = {}
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

            StationRatingCard(
                summary = stationRating,
                onRatingSelected = onRatingSelected
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
private fun StationRatingCard(
    summary: StationRatingSummary?,
    onRatingSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .height(90.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Bahnhofsbewertung",
                    fontSize = 20.sp
                )
                
                Text(
                    text = if (summary != null) "%.1f ★".format(summary.averageRating) else "N/A",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFFFD700)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < (summary?.averageRating?.toInt() ?: 0)) 
                            Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onRatingSelected(index + 1) }
                    )
                }
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
