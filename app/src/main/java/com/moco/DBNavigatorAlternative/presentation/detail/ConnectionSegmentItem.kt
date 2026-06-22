package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

            TrainPunctualityCard(
                punctualityScore = connectionSegment.punctualityScore
            )
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
        text = "${stop.time} - ${stop.name} - ${stop.platform}",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 10.dp),
        fontSize = 20.sp
    )
}

@Composable
private fun TrainProgressBar(
    progress: Float
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .width(6.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.LightGray)
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