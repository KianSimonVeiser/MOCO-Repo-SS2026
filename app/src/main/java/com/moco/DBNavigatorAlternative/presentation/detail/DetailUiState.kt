package com.moco.DBNavigatorAlternative.presentation.detail

import com.moco.DBNavigatorAlternative.domain.model.PunctualityInfo
import com.moco.DBNavigatorAlternative.domain.model.StationComment
import com.moco.DBNavigatorAlternative.domain.model.StationRatingSummary

data class DetailUiState(
    val isCommentSheetVisible: Boolean = false,
    val isWarningEnabled: Boolean = true,
    val newCommentText: String = "",
    val selectedSegmentId: String = "",
    val isSegmentMenuExpanded: Boolean = false,
    val punctualityInfo: PunctualityInfo? = null,
    val stationComments: List<StationComment> = emptyList(),
    val stationRating: StationRatingSummary? = null
)
