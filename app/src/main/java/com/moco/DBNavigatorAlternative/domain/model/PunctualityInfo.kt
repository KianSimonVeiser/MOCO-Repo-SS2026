package com.moco.DBNavigatorAlternative.domain.model

/**
 * Repräsentiert die Pünktlichkeitsdaten für eine Verbindung.
 * Basiert auf historischen Statistiken der Deutschen Bahn.
 */
data class PunctualityInfo(
    val score: Float,
    val bindingLossProbability: Float,
    val details: List<SegmentPunctuality>? = null
)

data class SegmentPunctuality(
    val train: String,
    val score: Float,
    val delay: Float
)
