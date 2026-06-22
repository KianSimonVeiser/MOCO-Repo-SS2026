package com.moco.DBNavigatorAlternative.domain.model

import com.moco.DBNavigatorAlternative.domain.model.User

data class Comment (
    val id: String,
    val user: User,
    val segmentId: String,
    val content: String
)