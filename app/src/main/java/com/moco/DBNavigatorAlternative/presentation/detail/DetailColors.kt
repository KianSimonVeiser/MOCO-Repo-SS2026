package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.ui.graphics.Color
import com.moco.DBNavigatorAlternative.domain.model.TrainType

/**
 * Definiert die Farben für die verschiedenen Zugtypen.
 */
fun TrainType.color(): Color = when (this) {
    TrainType.ICE -> Color.LightGray
    TrainType.IC -> Color.LightGray
    TrainType.RE -> Color(0xFFE2104E)
    TrainType.RB -> Color(0xFFE2104E)
    TrainType.S_BAHN -> Color(0xFF43A047)
    TrainType.U_BAHN -> Color(0xFF1565C0)
    TrainType.TRAM -> Color(0xFF8E24AA)
    TrainType.BUS -> Color(0xFFFF8F00)
}

/**
 * Liefert die Status-Farbe für den Pünktlichkeitsscore (Ampelsystem).
 * Grün >= 8.0, Gelb >= 5.0, Rot < 5.0.
 */
fun punctualityColor(
    score: Float?
): Color {
    if (score == null) return Color.Gray

    return when {
        score >= 8.0f -> Color(0xFF76B82A) // Grün
        score >= 5.0f -> Color(0xFFFFD700) // Gelb
        else -> Color(0xFFE2104E)         // Rot
    }
}
