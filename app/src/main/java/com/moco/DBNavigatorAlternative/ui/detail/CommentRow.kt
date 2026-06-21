package com.moco.DBNavigatorAlternative.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moco.DBNavigatorAlternative.model.Comment

@Composable
fun CommentRow(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Text(
            text = comment.user.username,
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}