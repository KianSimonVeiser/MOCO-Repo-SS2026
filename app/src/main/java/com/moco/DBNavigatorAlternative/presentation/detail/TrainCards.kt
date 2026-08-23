package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.domain.model.Train

/**
 * Komponente zur Anzeige des Zugtyps und der Liniennummer.
 */
@Composable
fun TrainCard(
    train: Train
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = train.type.color()
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = train.line,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

/**
 * Komponente zur Anzeige des Pünktlichkeits-Scores.
 */
@Composable
fun TrainPunctualityCard(
    punctualityScore: Float?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = punctualityColor(punctualityScore)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = punctualityScore
                ?.let { "%.1f".format(it) }
                ?: "N/A",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
