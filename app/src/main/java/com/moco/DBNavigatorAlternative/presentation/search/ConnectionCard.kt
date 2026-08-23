package com.moco.DBNavigatorAlternative.presentation.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary
import com.moco.DBNavigatorAlternative.domain.model.TrainType
import com.moco.DBNavigatorAlternative.presentation.theme.*

/**
 * Eine Karte, die eine einzelne Zugverbindung in der Ergebnisliste darstellt.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConnectionCard(
    connection: Connection,
    punctualityInfo: PunctualityInfo?,
    stationRating: StationRatingSummary?,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Kopfzeile mit Abfahrts-/Ankunftszeit und Pünktlichkeits-Score
            val currentScore = punctualityInfo?.score
            val isCritical = currentScore != null && currentScore < 5.0f
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(
                        text = "${connection.segments.firstOrNull()?.departureStop?.time} → ${connection.segments.lastOrNull()?.arrivalStop?.time}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isCritical) Color(0xFFBA1A1A) else MaterialTheme.colorScheme.onSurface
                    )
                    
                    // Anzeige der Bahnhofsbewertung
                    if (stationRating != null) {
                        Text(
                            text = "Bahnhof: %.1f ★ (%d)".format(stationRating.averageRating, stationRating.reviewCount),
                            fontSize = 12.sp,
                            color = Color(0xFF6B5E00) // Muted Gold
                        )
                    }
                }
                ScoreBadge(currentScore?.toDouble())
            }

            // Auflistung der genutzten Züge/Linien
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                connection.segments.forEach { segment ->
                    TrainBadge(segment.train.line, segment.train.type)
                }
            }

            // Hinweis zur aufgehobenen Zugbindung mit Wahrscheinlichkeit (dynamisch ab 50%)
            if (punctualityInfo != null && punctualityInfo.bindingLossProbability >= 0.5f) {
                val probPercent = (punctualityInfo.bindingLossProbability * 100).toInt()
                BindingHint(probPercent)
            }
        }
    }
}

/**
 * Kleines farbiges Abzeichen für den Pünktlichkeits-Score (Ampelsystem).
 */
@Composable
fun ScoreBadge(score: Double?) {
    val (color, onColor) = when {
        score == null -> Color(0xFF73777F) to Color.White
        score >= 8.0 -> Color(0xFF426915) to Color.White // Clean Green
        score >= 5.0 -> Color(0xFF6B5E00) to Color.White // Clean Yellow/Gold
        else -> Color(0xFFBA1A1A) to Color.White // Clean Red
    }
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = score?.let { "%.1f".format(it) } ?: "...",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold,
            color = onColor
        )
    }
}

@Composable
fun TrainBadge(name: String, type: TrainType) {
    val color = when(type) {
        TrainType.ICE -> Color(0xFFDFE2EB)
        TrainType.RE, TrainType.RB -> Color(0xFFBA1A1A).copy(alpha = 0.1f) // Light Red for Regio
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    val textColor = if (type == TrainType.ICE) Color(0xFF43474E) else if (type == TrainType.RE || type == TrainType.RB) Color(0xFFBA1A1A) else MaterialTheme.colorScheme.onSecondaryContainer

    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(name, Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium, color = textColor)
    }
}

@Composable
fun BindingHint(probPercent: Int) {
    Surface(
        color = Color(0xFF426915).copy(0.1f), 
        shape = RoundedCornerShape(8.dp), 
        border = BorderStroke(1.dp, Color(0xFF426915))
    ) {
        Text(
            text = "Zugbindung aufgehoben ($probPercent% Wahrscheinlichkeit)", 
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), 
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF426915)
        )
    }
}
