package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import com.example.cupofcoffee.app.views.detail.CommentViewData

@Composable
fun Comment(commentViewData: CommentViewData) {
    Column(
        modifier = Modifier.padding(
            start = 8.dp * commentViewData.nesting,
            top = 4.dp
        )
    ) {
        commentViewData.comment.author?.let {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "u/${commentViewData.comment.author}",
                style = MaterialTheme.typography.body2
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        commentViewData.comment.body?.let {
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .semantics { testTag = "comment-body" },
                text = commentViewData.comment.body,
                style = MaterialTheme.typography.body1
            )
        }
        Divider()
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .semantics { testTag = "comment-score" },
            text = "${commentViewData.comment.score} Score",
            style = MaterialTheme.typography.body2
        )
        Divider()
    }
}
