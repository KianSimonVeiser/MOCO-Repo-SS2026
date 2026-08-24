package com.moco.DBNavigatorAlternative.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.domain.model.LineComment
import com.moco.DBNavigatorAlternative.domain.model.TrainType

@Composable
fun CommentsBottomSheet(
    comments: List<LineComment>,
    connection: Connection,
    newCommentText: String,
    selectedSegmentId: String,
    segmentMenuExpanded: Boolean,
    onCommentTextChanged: (String) -> Unit,
    onSegmentMenuClick: () -> Unit,
    onSegmentMenuDismiss: () -> Unit,
    onSegmentSelected: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedSegment = connection.segments.firstOrNull { segment ->
        segment.id == selectedSegmentId
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Kommentare zu ${selectedSegment?.train?.line ?: "Linie"}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            if (comments.isEmpty()) {
                item {
                    Text(
                        "Noch keine Kommentare vorhanden.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(comments) { comment ->
                    CommentRow(comment = comment)
                }
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box {
                    OutlinedButton(
                        onClick = onSegmentMenuClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedSegment != null) 
                                "Linie: ${selectedSegment.train.line}" 
                                else "Linie auswählen"
                        )
                    }

                    DropdownMenu(
                        expanded = segmentMenuExpanded,
                        onDismissRequest = onSegmentMenuDismiss
                    ) {
                        connection.segments
                            .filter { it.train.type != TrainType.WALK }
                            .forEach { segment ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Linie: ${segment.train.line}")
                                    },
                                    onClick = {
                                        onSegmentSelected(segment.id)
                                    }
                                )
                            }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newCommentText,
                    onValueChange = onCommentTextChanged,
                    placeholder = { Text("Hier kommentieren...") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = onSendClick, enabled = newCommentText.isNotBlank()) {
                            Icon(Icons.Default.Send, contentDescription = "Senden")
                        }
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp)) // Padding für BottomSheet
    }
}
