package com.moco.DBNavigatorAlternative.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.model.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConnectionCard(connection: Connection) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${connection.segments.firstOrNull()?.departureStop?.time} → ${connection.segments.lastOrNull()?.arrivalStop?.time}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (connection.isLate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
                ScoreBadge(connection.displayScore)
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                connection.segments.forEach { segment ->
                    TrainBadge(segment.train.line, segment.train.type)
                }
            }

            if (connection.shouldShowBindingHint) BindingHint()
        }
    }
}

@Composable
fun ScoreBadge(score: Double) {
    val color = when {
        score >= 8.0 -> Color(0xFF76B82A)
        score >= 5.0 -> Color(0xFFFFD700)
        else -> Color(0xFFE2104E)
    }
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text("%.1f".format(score), Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TrainBadge(name: String, type: TrainType) {
    val color = when(type) {
        TrainType.ICE -> Color.LightGray
        TrainType.RE, TrainType.RB -> Color(0xFFE2104E)
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(name, Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun BindingHint() {
    Surface(color = Color(0xFF76B82A).copy(0.2f), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Color(0xFF76B82A))) {
        Text("Zugbindung aufgehoben", Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.bodySmall)
    }
}