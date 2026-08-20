package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar
import com.moco.DBNavigatorAlternative.presentation.profile.ProfilePopup

@Composable
fun DetailScreen(
    connectionId: String? = null,
    initialDate: String? = null,
    connection: Connection? = null,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(connection, connectionId) {
        if (connection != null) {
            viewModel.setConnection(connection)
        } else if (connectionId != null) {
            viewModel.loadConnectionById(connectionId, initialDate)
        }
    }

    uiState.connection?.let { currentConnection ->
        DetailScreenContent(
            connection = currentConnection,
            uiState = uiState,
            onFavoriteToggle = { viewModel.onFavoriteToggle(currentConnection) },
            onCommentsClick = viewModel::showCommentSheet,
            onDismissCommentSheet = viewModel::hideCommentSheet,
            onCommentTextChanged = viewModel::onCommentTextChanged,
            onSendComment = { viewModel.submitComment(currentConnection) },
            onSegmentMenuClick = viewModel::showSegmentMenu,
            onSegmentMenuDismiss = viewModel::hideSegmentMenu,
            onSegmentSelected = { viewModel.onSegmentSelected(it, currentConnection) },
            onRatingSelected = { stationId, rating ->
                viewModel.submitRating(stationId, rating)
            }
        )
    }

    // Authentifizierungs-Warnung
    if (uiState.showAuthWarning) {
        ProfilePopup(
            text = "Bitte melde dich an, um Verbindungen zu speichern.",
            onDismiss = { viewModel.dismissAuthWarning() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    connection: Connection,
    uiState: DetailUiState,
    onFavoriteToggle: () -> Unit,
    onCommentsClick: () -> Unit,
    onDismissCommentSheet: () -> Unit,
    onCommentTextChanged: (String) -> Unit,
    onSendComment: () -> Unit,
    onSegmentMenuClick: () -> Unit,
    onSegmentMenuDismiss: () -> Unit,
    onSegmentSelected: (String) -> Unit,
    onRatingSelected: (String, Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val selectedSegment = connection.segments.find { it.id == uiState.selectedSegmentId }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (uiState.isFavorite) "Verbindung gespeichert" else "Verbindung speichern",
                actions = {
                    Switch(
                        checked = uiState.isFavorite,
                        onCheckedChange = { onFavoriteToggle() }
                    )
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 320.dp)
            ) {
                items(connection.segments) { segment ->
                    ConnectionSegmentItem(connectionSegment = segment)
                }
            }

            DetailOverlayCards(
                historicalPunctualityScore = uiState.punctualityInfo?.score,
                bindingLossProbability = uiState.punctualityInfo?.bindingLossProbability,
                onCommentsClick = onCommentsClick,
                stationRating = uiState.stationRating,
                onRatingSelected = { rating -> 
                    selectedSegment?.departureStop?.id?.let { id ->
                        onRatingSelected(id, rating)
                    }
                }
            )
        }

        if (uiState.isCommentSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = onDismissCommentSheet,
                sheetState = sheetState
            ) {
                CommentsBottomSheet(
                    comments = uiState.stationComments,
                    connection = connection,
                    newCommentText = uiState.newCommentText,
                    selectedSegmentId = uiState.selectedSegmentId,
                    segmentMenuExpanded = uiState.isSegmentMenuExpanded,
                    onCommentTextChanged = onCommentTextChanged,
                    onSegmentMenuClick = onSegmentMenuClick,
                    onSegmentMenuDismiss = onSegmentMenuDismiss,
                    onSegmentSelected = onSegmentSelected,
                    onSendClick = onSendComment
                )
            }
        }
    }
}
