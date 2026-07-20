package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.R
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary
import com.moco.DBNavigatorAlternative.presentation.theme.Gold

/**
 * Modernisierte Overlay-Sektion für die Detailansicht.
 * Nutzt ein einheitliches Card-Design (ElevatedCard) passend zur restlichen App.
 */
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
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Kombinierte Karte für Pünktlichkeit und Bewertung
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Pünktlichkeits-Zeile
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.historical_punctuality),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        TrainPunctualityCard(punctualityScore = historicalPunctualityScore)
                    }

                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

                    // Bewertungs-Zeile
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.station_rating),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = if (index < (stationRating?.averageRating?.toInt() ?: 0)) 
                                            Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = Gold,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { onRatingSelected(index + 1) }
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (stationRating != null) "%.1f".format(stationRating.averageRating) else "0.0",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        
                        Text(
                            text = "${stationRating?.reviewCount ?: 0} Stimmen",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            // Kompakter Kommentar-Button
            Button(
                onClick = onCommentsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.comments), 
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
