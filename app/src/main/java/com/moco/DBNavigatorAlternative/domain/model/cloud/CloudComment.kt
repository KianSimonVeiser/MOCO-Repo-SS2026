package com.moco.DBNavigatorAlternative.domain.model.cloud

/**
 * Modell zur Speicherung von Nutzerkommentaren in Firebase Firestore.
 */
data class CloudComment(
    val commentId: String = "",
    val userId: String = "",
    val username: String = "",
    val targetId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
