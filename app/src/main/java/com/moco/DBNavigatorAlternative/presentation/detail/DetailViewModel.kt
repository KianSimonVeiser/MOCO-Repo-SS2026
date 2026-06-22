package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.lifecycle.ViewModel
import com.moco.DBNavigatorAlternative.domain.model.Connection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun setConnection(connection: Connection) {
        _uiState.update { currentState ->
            if (currentState.selectedSegmentId.isNotBlank()) {
                currentState
            } else {
                currentState.copy(
                    selectedSegmentId = connection.segments.firstOrNull()?.id.orEmpty()
                )
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
