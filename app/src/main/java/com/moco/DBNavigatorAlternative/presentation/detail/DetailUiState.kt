package com.moco.DBNavigatorAlternative.presentation.detail

import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.LineComment
import com.moco.DBNavigatorAlternative.domain.model.LineRatingSummary

data class DetailUiState(
    val connection: Connection? = null,
    val isCommentSheetVisible: Boolean = false,
    val isWarningEnabled: Boolean = true,
    val isFavorite: Boolean = false,
    val newCommentText: String = "",
    val selectedSegmentId: String = "",
    val isSegmentMenuExpanded: Boolean = false,
    val punctualityInfo: PunctualityInfo? = null,
    val lineComments: List<LineComment> = emptyList(),
    val lineRating: LineRatingSummary? = null,
    val showAuthWarning: Boolean = false // NEU: Steuert das Warn-Popup für nicht angemeldete Nutzer
)
