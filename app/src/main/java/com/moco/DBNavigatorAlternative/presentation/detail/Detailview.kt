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

// # Detailansicht einer Verbindung
// Hier zeigen wir alle Infos zu einer bestimmten Reise.
// Pünktlichkeit, Bewertungen und die Möglichkeit, Kommentare zu schreiben.
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
            onRatingSelected = { lineId, rating ->
                viewModel.submitRating(lineId, rating)
            }
        )
    }


    // Wenn der Nutzer nicht eingeloggt ist, zeigen wir einen Hinweis
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
                connection = connection,
                selectedSegmentId = uiState.selectedSegmentId,
                historicalPunctualityScore = uiState.punctualityInfo?.score,
                bindingLossProbability = uiState.punctualityInfo?.bindingLossProbability,
                onCommentsClick = onCommentsClick,
                lineRating = uiState.lineRating,
                onRatingSelected = { rating -> 
                    selectedSegment?.train?.line?.let { lineId ->
                        onRatingSelected(lineId, rating)
                    }
                },
                isSegmentMenuExpanded = uiState.isSegmentMenuExpanded,
                onSegmentMenuClick = onSegmentMenuClick,
                onSegmentMenuDismiss = onSegmentMenuDismiss,
                onSegmentSelected = onSegmentSelected
            )
        }

        if (uiState.isCommentSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = onDismissCommentSheet,
                sheetState = sheetState
            ) {
                CommentsBottomSheet(
                    comments = uiState.lineComments,
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

// --- Vorschau-Daten und Previews ---

val previewConnection = Connection(
    id = "conn001",
    totalDurationMinutes = 180,
    transferCount = 1,
    segments = listOf(
        com.moco.DBNavigatorAlternative.domain.model.ConnectionSegment(
            id = "cs1",
            departureStop = com.moco.DBNavigatorAlternative.domain.model.Stop(
                id = "FFM",
                name = "Frankfurt Hbf",
                time = "14:30",
                platform = "7"
            ),
            arrivalStop = com.moco.DBNavigatorAlternative.domain.model.Stop(
                id = "KAS",
                name = "Kassel-Wilhelmshöhe",
                time = "15:50",
                platform = "3"
            ),
            train = com.moco.DBNavigatorAlternative.domain.model.Train(
                id = "ice572",
                type = com.moco.DBNavigatorAlternative.domain.model.TrainType.ICE,
                line = "ICE 572"
            ),
            currentProgress = 1f,
            punctualityScore = 9.3f
        ),
        com.moco.DBNavigatorAlternative.domain.model.ConnectionSegment(
            id = "cs2",
            departureStop = com.moco.DBNavigatorAlternative.domain.model.Stop(
                id = "KAS",
                name = "Kassel-Wilhelmshöhe",
                time = "16:00",
                platform = "5"
            ),
            arrivalStop = com.moco.DBNavigatorAlternative.domain.model.Stop(
                id = "BER",
                name = "Berlin Hbf",
                time = "17:30",
                platform = "11"
            ),
            train = com.moco.DBNavigatorAlternative.domain.model.Train(
                id = "re21",
                type = com.moco.DBNavigatorAlternative.domain.model.TrainType.RE,
                line = "RE 21"
            ),
            currentProgress = 0.5f
        )
    )
)

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    com.moco.DBNavigatorAlternative.presentation.theme.MyApplicationTheme {
        DetailScreen(
            connection = previewConnection
        )
    }
}
