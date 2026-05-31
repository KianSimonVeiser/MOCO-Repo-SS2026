package com.moco.DBNavigatorAlternative.model

data class Connection(
    val id: String,
    val segments: List<ConnectionSegment>,
    val totalDurationMinutes: Int,
    val transferCount: Int
)

data class ConnectionSegment(
    val departureStop: Stop,
    val arrivalStop: Stop,
    val train: Train,
    val currentProgress: Float,
    val punctualityScore: Float? = null
)

data class Stop(
    val id: String,
    val name: String,
    val time: String,
    val platform: String? = null
)

data class Train(
    val id: String,
    val type: TrainType,
    val line: String
)

enum class TrainType {
    ICE,
    IC,
    RE,
    RB,
    S_BAHN,
    U_BAHN,
    TRAM,
    BUS
}