package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moco.DBNavigatorAlternative.data.InteractionRepository
import com.moco.DBNavigatorAlternative.data.UserRepository
import com.moco.DBNavigatorAlternative.data.repository.PunctualityRepository
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.StationComment
import com.moco.DBNavigatorAlternative.domain.model.StationRating
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val punctualityRepository: PunctualityRepository = PunctualityRepository(),
    private val interactionRepository: InteractionRepository = InteractionRepository(),
    private val userRepository: UserRepository = UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private var commentsJob: Job? = null

    fun setConnection(connection: Connection) {
        viewModelScope.launch {
            val punctualityInfo = punctualityRepository.getPunctualityForConnection(connection)
            
            _uiState.update { currentState ->
                val firstSegment = connection.segments.firstOrNull()
                val selectedId = currentState.selectedSegmentId.ifBlank {
                    firstSegment?.id.orEmpty()
                }
                
                currentState.copy(
                    selectedSegmentId = selectedId,
                    punctualityInfo = punctualityInfo
                )
            }
            
            connection.segments.firstOrNull()?.departureStop?.let { stop ->
                observeComments(stop.id, stop.platform)
                loadRating(stop.id)
            }
        }
    }

    private fun observeComments(stationId: String, platform: String?) {
        commentsJob?.cancel()
        commentsJob = viewModelScope.launch {
            interactionRepository.getCommentsForStation(stationId, platform).collect { comments ->
                _uiState.update { it.copy(stationComments = comments) }
            }
        }
    }

    private suspend fun loadRating(stationId: String) {
        val summary = interactionRepository.getStationRatingSummary(stationId)
        _uiState.update { it.copy(stationRating = summary) }
    }

    fun onWarningEnabledChanged(isEnabled: Boolean) {
        _uiState.update { it.copy(isWarningEnabled = isEnabled) }
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
        
        // Nur angemeldete Nutzer dürfen kommentieren (oder wir nutzen Platzhalter falls nicht)
        val userId = currentUser?.userId ?: "anonymous"
        val username = currentUser?.username ?: "Anonymer Reisender"

        viewModelScope.launch {
            val comment = StationComment(
                stationId = segment.departureStop.id,
                stationName = segment.departureStop.name,
                platform = segment.departureStop.platform,
                userId = userId,
                username = username,
                content = currentState.newCommentText
            )
            interactionRepository.addComment(comment)
            _uiState.update { it.copy(newCommentText = "") }
        }
    }

    fun submitRating(stationId: String, rating: Int) {
        val currentUser = userRepository.currentUser.value ?: return // Nur angemeldete Nutzer

        viewModelScope.launch {
            interactionRepository.addRating(
                StationRating(
                    stationId = stationId,
                    userId = currentUser.userId,
                    rating = rating
                )
            )
            loadRating(stationId)
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
        segment?.departureStop?.let { stop ->
            observeComments(stop.id, stop.platform)
            viewModelScope.launch { loadRating(stop.id) }
        }
    }
}
