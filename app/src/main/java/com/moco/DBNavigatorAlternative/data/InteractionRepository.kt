package com.moco.DBNavigatorAlternative.data

import com.google.firebase.firestore.FirebaseFirestore
import com.moco.DBNavigatorAlternative.domain.model.LineComment
import com.moco.DBNavigatorAlternative.domain.model.LineRating
import com.moco.DBNavigatorAlternative.domain.model.LineRatingSummary
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class InteractionRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val commentsCollection = firestore.collection("lineComments")
    private val ratingsCollection = firestore.collection("lineRatings")
    private val summariesCollection = firestore.collection("lineSummaries")
    private val countersCollection = firestore.collection("metadata")
    private val favoritesCollection = firestore.collection("favorites")

    suspend fun addComment(comment: LineComment) {
        val counterRef = countersCollection.document("line_comment_counter")
        val now = Date()
        val formattedDate = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY).format(now)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(counterRef)
            val currentCount = snapshot.getLong("count") ?: 0L
            val nextId = currentCount + 1
            
            transaction.set(counterRef, mapOf("count" to nextId))
            
            val newDocRef = commentsCollection.document(nextId.toString())
            val commentWithData = comment.copy(
                commentId = nextId.toString(),
                dateText = formattedDate,
                timestamp = now.time
            )
            transaction.set(newDocRef, commentWithData)
        }.await()
    }

    fun getCommentsForLine(lineId: String): Flow<List<LineComment>> = callbackFlow {
        val query = commentsCollection.whereEqualTo("lineId", lineId)
        
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val comments = try {
                    snapshot.toObjects(LineComment::class.java)
                } catch (e: Exception) {
                    emptyList<LineComment>()
                }
                trySend(comments.sortedByDescending { it.timestamp })
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun addRating(rating: LineRating) {
        val docId = "${rating.lineId}_${rating.userId}"
        ratingsCollection.document(docId).set(rating).await()
        updateLineSummary(rating.lineId)
    }

    private suspend fun updateLineSummary(lineId: String) {
        val ratings = ratingsCollection.whereEqualTo("lineId", lineId).get().await()
        val totalRatings = ratings.size()
        if (totalRatings > 0) {
            val sum = ratings.documents.sumOf { it.getLong("rating") ?: 0L }
            val average = sum.toFloat() / totalRatings
            val summary = LineRatingSummary(lineId, average, totalRatings)
            summariesCollection.document(lineId).set(summary).await()
        }
    }

    suspend fun getLineRatingSummary(lineId: String): LineRatingSummary? {
        return summariesCollection.document(lineId).get().await()
            .toObject(LineRatingSummary::class.java)
    }

    suspend fun getCommentsForUser(userId: String): List<LineComment> {
        return try {
            val snapshot = commentsCollection.whereEqualTo("userId", userId).get().await()
            snapshot.toObjects(LineComment::class.java).sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteComment(commentId: String) {
        commentsCollection.document(commentId).delete().await()
    }

    suspend fun clearAllUserComments(userId: String) {
        try {
            val snapshot = commentsCollection.whereEqualTo("userId", userId).get().await()
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Speichert einen neuen Favoriten ab.
     */
    suspend fun addFavorite(favorite: FavoriteConnection) {
        val docId = "${favorite.userId}_${favorite.connectionId}"
        favoritesCollection.document(docId).set(favorite).await()
    }

    /**
     * Löscht einen Favoriten wieder.
     */
    suspend fun removeFavorite(userId: String, connectionId: String) {
        val docId = "${userId}_${connectionId}"
        favoritesCollection.document(docId).delete().await()
    }

    /**
     * Schaut nach, ob eine Verbindung schon in den Favoriten ist.
     */
    suspend fun isFavorite(userId: String, connectionId: String): Boolean {
        val docId = "${userId}_${connectionId}"
        return try {
            favoritesCollection.document(docId).get().await().exists()
        } catch (e: Exception) {
            false
        }
    }


    fun getFavorites(userId: String): Flow<List<FavoriteConnection>> = callbackFlow {
        val listener = favoritesCollection.whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val favorites = snapshot.toObjects(FavoriteConnection::class.java)
                    trySend(favorites.sortedByDescending { it.timestamp })
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun clearAllFavorites(userId: String) {
        try {
            val snapshot = favoritesCollection.whereEqualTo("userId", userId).get().await()
            val batch = firestore.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
