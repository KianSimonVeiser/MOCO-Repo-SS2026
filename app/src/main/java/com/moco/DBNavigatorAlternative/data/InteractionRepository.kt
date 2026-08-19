package com.moco.DBNavigatorAlternative.data

import com.google.firebase.firestore.FirebaseFirestore
import com.moco.DBNavigatorAlternative.domain.model.StationComment
import com.moco.DBNavigatorAlternative.domain.model.StationRating
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary
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
    private val commentsCollection = firestore.collection("comments")
    private val ratingsCollection = firestore.collection("ratings")
    private val stationSummariesCollection = firestore.collection("stationSummaries")
    private val countersCollection = firestore.collection("metadata")
    private val favoritesCollection = firestore.collection("favorites")

    suspend fun addComment(comment: StationComment) {
        val counterRef = countersCollection.document("comment_counter")
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

    fun getCommentsForStation(stationId: String, platform: String? = null): Flow<List<StationComment>> = callbackFlow {
        var query = commentsCollection.whereEqualTo("stationId", stationId)
        if (platform != null) {
            query = query.whereEqualTo("platform", platform)
        }
        
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val comments = try {
                    snapshot.toObjects(StationComment::class.java)
                } catch (e: Exception) {
                    emptyList<StationComment>()
                }
                trySend(comments.sortedByDescending { it.timestamp })
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun addRating(rating: StationRating) {
        val docId = "${rating.stationId}_${rating.userId}"
        ratingsCollection.document(docId).set(rating).await()
        updateStationSummary(rating.stationId)
    }

    private suspend fun updateStationSummary(stationId: String) {
        val ratings = ratingsCollection.whereEqualTo("stationId", stationId).get().await()
        val totalRatings = ratings.size()
        if (totalRatings > 0) {
            val sum = ratings.documents.sumOf { it.getLong("rating") ?: 0L }
            val average = sum.toFloat() / totalRatings
            val summary = StationRatingSummary(stationId, average, totalRatings)
            stationSummariesCollection.document(stationId).set(summary).await()
        }
    }

    suspend fun getStationRatingSummary(stationId: String): StationRatingSummary? {
        return stationSummariesCollection.document(stationId).get().await()
            .toObject(StationRatingSummary::class.java)
    }

    suspend fun getCommentsForUser(userId: String): List<StationComment> {
        return try {
            val snapshot = commentsCollection.whereEqualTo("userId", userId).get().await()
            snapshot.toObjects(StationComment::class.java).sortedByDescending { it.timestamp }
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
     * Speichert eine spezifische Verbindung anhand ihrer ID.
     */
    suspend fun addFavorite(favorite: FavoriteConnection) {
        val docId = "${favorite.userId}_${favorite.connectionId}"
        favoritesCollection.document(docId).set(favorite).await()
    }

    /**
     * Entfernt eine Verbindung aus den Favoriten.
     */
    suspend fun removeFavorite(userId: String, connectionId: String) {
        val docId = "${userId}_${connectionId}"
        favoritesCollection.document(docId).delete().await()
    }

    /**
     * Prüft, ob diese exakte Verbindung favorisiert ist.
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
