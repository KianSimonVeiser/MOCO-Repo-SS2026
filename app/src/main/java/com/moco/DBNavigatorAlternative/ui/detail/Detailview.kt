package com.moco.DBNavigatorAlternative.ui.detail

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.model.Connection
import com.moco.DBNavigatorAlternative.ui.generalUse.AppBottomBar
import com.moco.DBNavigatorAlternative.ui.generalUse.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    connection: Connection
) {
    val sheetState = rememberModalBottomSheetState()

    var showCommentSheet by remember {
        mutableStateOf(false)
    }

    var saveConnectionChecked by remember {
        mutableStateOf(true)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Verbindung speichern",
                actions = {
                    Switch(
                        checked = saveConnectionChecked,
                        onCheckedChange = {
                            saveConnectionChecked = it
                        }
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
                onCommentsClick = {
                    showCommentSheet = true
                }
            )
        }

        if (showCommentSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showCommentSheet = false
                },
                sheetState = sheetState
            ) {
                CommentsBottomSheet()
            }
        }
    }
}