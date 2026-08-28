package com.moco.DBNavigatorAlternative.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.data.api.DBNavApiService
import com.moco.DBNavigatorAlternative.data.repository.PunctualityRepository
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.TrainType
import com.moco.DBNavigatorAlternative.domain.model.LineComment
import com.moco.DBNavigatorAlternative.domain.model.LineRating
import com.moco.DBNavigatorAlternative.domain.model.FavoriteConnection
import com.moco.DBNavigatorAlternative.domain.repository.FavoriteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(
    private val dbNavApiService: DBNavApiService,
    private val punctualityRepository: PunctualityRepository = PunctualityRepository(),
    private val interactionRepository: InteractionRepository = InteractionRepository(),
    private val userRepository: UserRepository = UserRepository,
    private val favoriteRepository: FavoriteRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var commentsJob: Job? = null

    /**
     * Initialisiert die Verbindungsinformationen und prüft den Favoritenstatus.
     */
    fun setConnection(connection: Connection) {
        viewModelScope.launch {
            val punctualityInfo = punctualityRepository.getPunctualityForConnection(connection)
            
            // Segmente mit den geladenen Scores aktualisieren
            val updatedSegments = connection.segments.map { segment ->
                val segmentScore = punctualityInfo.details?.find { it.train == segment.train.line }?.score
                segment.copy(punctualityScore = segmentScore)
            }
            val updatedConnection = connection.copy(segments = updatedSegments)

            // Favoriten-Status lokal prüfen
            val isFav = favoriteRepository?.isFavorite(connection.id)?.firstOrNull() ?: false

            _uiState.update { currentState ->
                val nonWalkSegments = updatedConnection.segments.filter { it.train.type != TrainType.WALK }
                val defaultSegmentId = nonWalkSegments.firstOrNull()?.id ?: updatedConnection.segments.firstOrNull()?.id.orEmpty()
                
                val selectedId = currentState.selectedSegmentId.ifBlank {
                    defaultSegmentId
                }
                
                currentState.copy(
                    connection = updatedConnection,
                    selectedSegmentId = selectedId,
                    punctualityInfo = punctualityInfo,
                    isFavorite = isFav
                )
            }
            
            updatedConnection.segments.firstOrNull()?.let { segment ->
                observeComments(segment.train.line)
                loadRating(segment.train.line)
            }
        }
    }

    /**
     * Lädt eine Verbindung anhand der ID aus den Favoriten und aktualisiert sie via API.
     */
    fun loadConnectionById(connectionId: String, initialDate: String? = null) {
        viewModelScope.launch {
            val fav = favoriteRepository?.getFavoriteByChecksum(connectionId)
            if (fav != null) {
                // Versuche die Verbindung aktuell über die API abzurufen
                
                val dateToUse = if (!initialDate.isNullOrBlank()) {
                    // Konvertiere dd.MM.yyyy -> yyyy-MM-dd
                    try {
                        val parts = initialDate.split(".")
                        "${parts[2]}-${parts[1]}-${parts[0]}T12:00:00+02:00"
                    } catch (e: Exception) {
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+02:00", Locale.GERMANY).format(Date())
                    }
                } else {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+02:00", Locale.GERMANY).format(Date())
                }
                
                try {
                    val connections = dbNavApiService.getConnections(
                        fromId = fav.fromId,
                        toId = fav.toId,
                        dateTime = dateToUse,
                        onlyDTicket = false
                    )
                    
                    // Finde die exakte Verbindung per Checksum/ID wieder
                    val liveConnection = connections.find { it.id == connectionId }
                    
                    if (liveConnection != null) {
                        setConnection(liveConnection)
                    } else {
                        // Falls nicht gefunden (z.B. Zeit zu weit weg), nimm die erste Ähnliche oder Fallback
                        val bestMatch = connections.firstOrNull { it.segments.firstOrNull()?.departureStop?.time == fav.departureTime }
                            ?: connections.firstOrNull()
                        
                        if (bestMatch != null) {
                            setConnection(bestMatch)
                        } else {
                            showFallback(fav)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("DetailViewModel", "Fehler beim API-Refresh des Favoriten", e)
                    showFallback(fav)
                }
            }
        }
    }

    private fun showFallback(fav: FavoriteConnection) {
        val fallbackConnection = Connection(
            id = fav.connectionId,
            totalDurationMinutes = 0,
            transferCount = 0,
            segments = listOf(
                com.moco.DBNavigatorAlternative.domain.model.ConnectionSegment(
                    id = "fav_seg",
                    departureStop = com.moco.DBNavigatorAlternative.domain.model.Stop(
                        id = fav.fromId,
                        name = fav.fromStation,
                        time = fav.departureTime
                    ),
                    arrivalStop = com.moco.DBNavigatorAlternative.domain.model.Stop(
                        id = fav.toId,
                        name = fav.toStation,
                        time = fav.arrivalTime
                    ),
                    train = com.moco.DBNavigatorAlternative.domain.model.Train(
                        id = "fav_train",
                        type = com.moco.DBNavigatorAlternative.domain.model.TrainType.RB,
                        line = fav.lineName
                    ),
                    currentProgress = 0f
                )
            )
        )
        setConnection(fallbackConnection)
    }

    private fun observeComments(lineId: String) {
        commentsJob?.cancel()
        commentsJob = viewModelScope.launch {
            interactionRepository.getCommentsForLine(lineId)
                .catch { e -> Log.e("DetailViewModel", "Fehler beim Laden der Kommentare", e) }
                .collect { comments ->
                    _uiState.update { it.copy(lineComments = comments) }
                }
        }
    }

    private suspend fun loadRating(lineId: String) {
        try {
            val summary = interactionRepository.getLineRatingSummary(lineId)
            _uiState.update { it.copy(lineRating = summary) }
        } catch (e: Exception) {
            Log.e("DetailViewModel", "Fehler beim Laden der Bewertung", e)
        }
    }

    fun onWarningEnabledChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(isWarningEnabled = isEnabled) }
    }

    /**
     * Toggled den Favoriten-Status der exakten Verbindung lokal.
     */
    fun onFavoriteToggle(connection: Connection) {
        val currentState = _uiState.value
        val isCurrentlyFav = currentState.isFavorite
        
        val firstSeg = connection.segments.firstOrNull()
        val lastSeg = connection.segments.lastOrNull()

        viewModelScope.launch {
            if (isCurrentlyFav) {
                favoriteRepository?.deleteFavoriteByChecksum(connection.id)
                _uiState.update { it.copy(isFavorite = false) }
            } else {
                val userId = userRepository.currentUser.value?.userId ?: "local_user"
                val favorite = FavoriteConnection(
                    connectionId = connection.id,
                    userId = userId,
                    fromStation = firstSeg?.departureStop?.name.orEmpty(),
                    fromId = firstSeg?.departureStop?.id.orEmpty(),
                    toStation = lastSeg?.arrivalStop?.name.orEmpty(),
                    toId = lastSeg?.arrivalStop?.id.orEmpty(),
                    lineName = firstSeg?.train?.line.orEmpty(),
                    departureTime = firstSeg?.departureStop?.time.orEmpty(),
                    arrivalTime = lastSeg?.arrivalStop?.time.orEmpty()
                )
                favoriteRepository?.insertFavorite(favorite)
                _uiState.update { it.copy(isFavorite = true) }
            }
        }
    }

    fun dismissAuthWarning() {
        _uiState.update { it.copy(showAuthWarning = false) }
    }

    fun showCommentSheet() {
        _uiState.update { it.copy(isCommentSheetVisible = true) }
    }

    fun hideCommentSheet() {
        _uiState.update { it.copy(isCommentSheetVisible = false, isSegmentMenuExpanded = false) }
    }

    fun onCommentTextChanged(commentText: String) {
        _uiState.update { it.copy(newCommentText = commentText) }
    }

    fun submitComment(connection: Connection) {
        val currentState = _uiState.value
        val segment = connection.segments.find { it.id == currentState.selectedSegmentId } ?: return
        val currentUser = userRepository.currentUser.value
        
        val userId = currentUser?.userId ?: "anonymous"
        val username = currentUser?.username ?: "Anonymer Reisender"

        viewModelScope.launch {
            val comment = LineComment(
                lineId = segment.train.line,
                lineName = segment.train.line,
                userId = userId,
                username = username,
                content = currentState.newCommentText
            )
            interactionRepository.addComment(comment)
            _uiState.update { it.copy(newCommentText = "") }
        }
    }

    fun submitRating(lineId: String, rating: Int) {
        val currentUser = userRepository.currentUser.value ?: return

        viewModelScope.launch {
            interactionRepository.addRating(
                LineRating(
                    lineId = lineId,
                    userId = currentUser.userId,
                    rating = rating
                )
            )
            loadRating(lineId)
        }
    }

    fun showSegmentMenu() {
        _uiState.update { it.copy(isSegmentMenuExpanded = true) }
    }

    fun hideSegmentMenu() {
        _uiState.update { it.copy(isSegmentMenuExpanded = false) }
    }

    fun onSegmentSelected(segmentId: String, connection: Connection) {
        _uiState.update { it.copy(selectedSegmentId = segmentId, isSegmentMenuExpanded = false) }
        
        val segment = connection.segments.find { it.id == segmentId }
        segment?.let {
            observeComments(it.train.line)
            viewModelScope.launch { loadRating(it.train.line) }
        }
    }
}
