package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.moco.DBNavigatorAlternative.domain.model.TrainType

fun TrainType.color(): Color = when (this) {
    TrainType.ICE -> Color(0xFFE53935)
    TrainType.IC -> Color(0xFF1E88E5)
    TrainType.RE -> Color(0xFF43A047)
    TrainType.RB -> Color(0xFF7CB342)
    TrainType.S_BAHN -> Color(0xFF2E7D32)
    TrainType.U_BAHN -> Color(0xFF1565C0)
    TrainType.TRAM -> Color(0xFF8E24AA)
    TrainType.BUS -> Color(0xFFFF8F00)
}

fun punctualityColor(
    score: Float?
): Color {
    if (score == null) return Color.Gray

    val normalized = score
        .coerceIn(0f, 10f)
        .div(10f)

    val red = Color(0xFFE53935)
    val yellow = Color(0xFFFDD835)
    val green = Color(0xFF43A047)

    return if (normalized < 0.5f) {
        lerp(
            start = red,
            stop = yellow,
            fraction = normalized / 0.5f
        )
    } else {
        lerp(
            start = yellow,
            stop = green,
            fraction = (normalized - 0.5f) / 0.5f
        )
    }
}