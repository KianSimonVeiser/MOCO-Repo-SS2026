import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.domain.model.Comment
import com.moco.DBNavigatorAlternative.domain.model.Connection
import com.moco.DBNavigatorAlternative.presentation.detail.CommentRow


@Composable
fun CommentsBottomSheet(
    comments: List<Comment>,
    connectionSegments: Connection,
    modifier: Modifier = Modifier
) {
    var newCommentText by remember {
        mutableStateOf("")
    }

    var selectedSegmentId by remember {
        mutableStateOf(connectionSegments.segments.firstOrNull()?.id.orEmpty())
    }

    var segmentMenuExpanded by remember {
        mutableStateOf(false)
    }

    val selectedSegment = connectionSegments.segments.firstOrNull { segment ->
        segment.id == selectedSegmentId
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "${comments.size} Kommentare",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                vertical = 8.dp
            )
        ) {
            items(comments) { comment ->
                CommentRow(
                    comment = comment
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            OutlinedTextField(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                label = {
                    Text("Kommentar schreiben")
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box {
                OutlinedButton(
                    onClick = {
                        segmentMenuExpanded = true
                    }
                ) {
                    Text(
                        text = selectedSegment?.train?.line
                            ?: "Verbindungsabschnitt auswählen"
                    )
                }

                DropdownMenu(
                    expanded = segmentMenuExpanded,
                    onDismissRequest = {
                        segmentMenuExpanded = false
                    }
                ) {
                    connectionSegments.segments.forEach { segment ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${segment.train.line}: ${segment.departureStop.name} → ${segment.arrivalStop.name}"
                                )
                            },
                            onClick = {
                                selectedSegmentId = segment.id
                                segmentMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}