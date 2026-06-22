package com.moco.DBNavigatorAlternative.presentation.detail

data class DetailUiState(
    val isCommentSheetVisible: Boolean = false,
    val isWarningEnabled: Boolean = true,
    val newCommentText: String = "",
    val selectedSegmentId: String = "",
    val isSegmentMenuExpanded: Boolean = false
)
