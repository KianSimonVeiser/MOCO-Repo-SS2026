package com.moco.DBNavigatorAlternative.presentation.detail

import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo

data class DetailUiState(
    val isCommentSheetVisible: Boolean = false,
    val isWarningEnabled: Boolean = true,
    val newCommentText: String = "",
    val selectedSegmentId: String = "",
    val isSegmentMenuExpanded: Boolean = false,
    val punctualityInfo: PunctualityInfo? = null
)
