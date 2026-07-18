package com.moco.DBNavigatorAlternative.data

import com.google.firebase.firestore.FirebaseFirestore
import com.moco.DBNavigatorAlternative.domain.model.StationComment
import com.moco.DBNavigatorAlternative.domain.model.StationRating
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary
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
                // toObjects fängt Fehler nun ab, falls noch "Leichen" im Cache liegen
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
}
