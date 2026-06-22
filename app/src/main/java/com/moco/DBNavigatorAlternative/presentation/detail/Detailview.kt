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
import com.moco.DBNavigatorAlternative.domain.model.Comment
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.presentation.generalUse.AppTopBar

@Composable
fun DetailScreen(
    connection: Connection,
    comments: List<Comment>,
    viewModel: DetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(connection) {
        viewModel.setConnection(connection)
    }

    DetailScreenContent(
        connection = connection,
        comments = comments,
        uiState = uiState,
        onWarningEnabledChanged = viewModel::onWarningEnabledChanged,
        onCommentsClick = viewModel::showCommentSheet,
        onDismissCommentSheet = viewModel::hideCommentSheet,
        onCommentTextChanged = viewModel::onCommentTextChanged,
        onSegmentMenuClick = viewModel::showSegmentMenu,
        onSegmentMenuDismiss = viewModel::hideSegmentMenu,
        onSegmentSelected = viewModel::onSegmentSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    connection: Connection,
    comments: List<Comment>,
    uiState: DetailUiState,
    onWarningEnabledChanged: (Boolean) -> Unit,
    onCommentsClick: () -> Unit,
    onDismissCommentSheet: () -> Unit,
    onCommentTextChanged: (String) -> Unit,
    onSegmentMenuClick: () -> Unit,
    onSegmentMenuDismiss: () -> Unit,
    onSegmentSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Verbindung speichern",
                actions = {
                    Switch(
                        checked = uiState.isWarningEnabled,
                        onCheckedChange = onWarningEnabledChanged
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
                contentPadding = PaddingValues(
                    bottom = 320.dp
                )
            ) {
                items(connection.segments) { segment ->
                    ConnectionSegmentItem(
                        connectionSegment = segment
                    )
                }
            }

            DetailOverlayCards(
                historicalPunctualityScore = 3.5f,
                onCommentsClick = onCommentsClick
            )
        }

        if (uiState.isCommentSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = onDismissCommentSheet,
                sheetState = sheetState
            ) {
                CommentsBottomSheet(
                    comments = comments,
                    connection = connection,
                    newCommentText = uiState.newCommentText,
                    selectedSegmentId = uiState.selectedSegmentId,
                    segmentMenuExpanded = uiState.isSegmentMenuExpanded,
                    onCommentTextChanged = onCommentTextChanged,
                    onSegmentMenuClick = onSegmentMenuClick,
                    onSegmentMenuDismiss = onSegmentMenuDismiss,
                    onSegmentSelected = onSegmentSelected
                )
            }
        }
    }
}
