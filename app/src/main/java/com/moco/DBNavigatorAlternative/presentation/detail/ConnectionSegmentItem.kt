package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moco.DBNavigatorAlternative.domain.model.ConnectionSegment
import com.moco.DBNavigatorAlternative.domain.model.Stop

/**
 * Komponente zur Darstellung eines einzelnen Verbindungsabschnitts.
 */
@Composable
fun ConnectionSegmentItem(
    connectionSegment: ConnectionSegment,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        ConnectionStop(
            stop = connectionSegment.departureStop
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TrainProgressBar(
                progress = connectionSegment.currentProgress
            )

            TrainCard(
                train = connectionSegment.train
            )

            if (connectionSegment.punctualityScore != null) {
                TrainPunctualityCard(
                    punctualityScore = connectionSegment.punctualityScore
                )
            }
        }

        ConnectionStop(
            stop = connectionSegment.arrivalStop
        )
    }
}

@Composable
private fun ConnectionStop(
    stop: Stop
) {
    Text(
        text = "${stop.time} - ${stop.name} - ${stop.platform ?: ""}",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontSize = 18.sp
    )
}

@Composable
private fun TrainProgressBar(
    progress: Float
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .width(4.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress.coerceIn(0f, 1f))
                .align(Alignment.TopCenter)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}
