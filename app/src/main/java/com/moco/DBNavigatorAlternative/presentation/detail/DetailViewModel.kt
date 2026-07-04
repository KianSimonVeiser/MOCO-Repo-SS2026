package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moco.DBNavigatorAlternative.data.PunctualityRepository
import com.moco.DBNavigatorAlternative.domain.model.Connection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val punctualityRepository: PunctualityRepository = PunctualityRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun setConnection(connection: Connection) {
        viewModelScope.launch {
            val punctualityInfo = punctualityRepository.getPunctualityForConnection(connection)
            
            _uiState.update { currentState ->
                if (currentState.selectedSegmentId.isNotBlank()) {
                    currentState.copy(punctualityInfo = punctualityInfo)
                } else {
                    currentState.copy(
                        selectedSegmentId = connection.segments.firstOrNull()?.id.orEmpty(),
                        punctualityInfo = punctualityInfo
                    )
                }
            }
        }
    }

    fun onWarningEnabledChanged(isEnabled: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isWarningEnabled = isEnabled)
        }
    }

    fun showCommentSheet() {
        _uiState.update { currentState ->
            currentState.copy(isCommentSheetVisible = true)
        }
    }

    fun hideCommentSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                isCommentSheetVisible = false,
                isSegmentMenuExpanded = false
            )
        }
    }

    fun onCommentTextChanged(commentText: String) {
        _uiState.update { currentState ->
            currentState.copy(newCommentText = commentText)
        }
    }

    fun showSegmentMenu() {
        _uiState.update { currentState ->
            currentState.copy(isSegmentMenuExpanded = true)
        }
    }

    fun hideSegmentMenu() {
        _uiState.update { currentState ->
            currentState.copy(isSegmentMenuExpanded = false)
        }
    }

    fun onSegmentSelected(segmentId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedSegmentId = segmentId,
                isSegmentMenuExpanded = false
            )
        }
    }
}
