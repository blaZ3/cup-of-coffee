package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cupofcoffee.R
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.views.detail.CommentViewData
import com.google.accompanist.coil.rememberCoilPainter

@Composable
fun Post(post: Post, onPostClicked: (post: Post) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable(true) { onPostClicked(post) }
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            if (post.postedInfo.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = post.postedInfo,
                    style = MaterialTheme.typography.body2,
                )
            }

            val tagSB = StringBuilder()
            if (post.isOriginalContent) {
                tagSB.append("[${stringResource(R.string.tag_oc)}]")
            }
            if (post.over18) {
                tagSB.append(" ${stringResource(R.string.tag_nsfw)}")
            }
            val tags = tagSB.toString()
            if (tags.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = tags,
                    style = MaterialTheme.typography.body2
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            post.title?.let {
                Text(
                    modifier = Modifier
                        .padding(4.dp)
                        .semantics {
                            testTag = "post-title"
                        },
                    text = post.title,
                    style = MaterialTheme.typography.h5,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (post.isImage && post.cleanedImageUrl != null) {
                Image(
                    painter = rememberCoilPainter(
                        request = post.cleanedImageUrl
                    ),
                    contentDescription = post.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .height(250.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
            Divider()
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "${post.upVotes} Ups",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "${post.downVotes} Downs",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "${post.numComments} Comments",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
fun PostDetail(
    post: Post,
    isLoadingComments: Boolean = false,
    comments: List<CommentViewData>?
) {
    LazyColumn(
        modifier = Modifier.background(MaterialTheme.colors.surface),
    ) {
        item(post.postFullName) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = post.postedInfo,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
                post.title?.let {
                    Text(
                        modifier = Modifier.semantics {
                            testTag = "post-detail-title"
                        },
                        text = post.title,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSurface,
                    )
                }
                if (post.isImage && post.cleanedImageUrl != null) {
                    Image(
                        painter = rememberCoilPainter(
                            request = post.cleanedImageUrl
                        ),
                        contentDescription = post.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .clip(shape = RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.FillWidth
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "${post.upVotes} Ups",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )
                    Text(
                        text = "${post.downVotes} Downs",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )
                    Text(
                        text = "${post.numComments} Comments",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface,
                    )
                }
            }
            Divider(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier.height(4.dp))
        }
        if (isLoadingComments) {
            items(1) {
                Loading(stringResource(R.string.loading_comments))
            }
        } else {
            comments?.let {
                items(comments) { comment ->
                    Comment(commentViewData = comment)
                }
            }
        }
    }
}
