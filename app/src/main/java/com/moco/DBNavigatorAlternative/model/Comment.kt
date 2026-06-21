package com.moco.DBNavigatorAlternative.model

data class Comment (
    val id: String,
    val user: User,
    val segmentId: String,
    val content: String
)