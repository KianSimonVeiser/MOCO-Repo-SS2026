package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.domain.model.Train

@Composable
fun TrainCard(
    train: Train
) {
    Card(
        modifier = Modifier.padding(horizontal = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = train.type.color()
        )
    ) {
        Text(
            text = train.line,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun TrainPunctualityCard(
    punctualityScore: Float?
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = punctualityColor(punctualityScore)
        )
    ) {
        Text(
            text = punctualityScore
                ?.let { "%.1f".format(it) }
                ?: "Keine Daten",
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}