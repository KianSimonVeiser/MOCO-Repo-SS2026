package com.moco.DBNavigatorAlternative.model

data class Comment (
    val id: String,
    val userId: String,
    val segmentId: String,
    val content: String
)