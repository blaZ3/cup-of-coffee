package com.example.cupofcoffee.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.ContentScale.Companion.FillWidth
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.cupofcoffee.Error
import com.example.cupofcoffee.Error.NetworkError
import com.example.cupofcoffee.R.string.*
import com.example.cupofcoffee.app.data.models.Post
import com.example.cupofcoffee.app.views.detail.CommentViewData
import com.google.accompanist.coil.rememberCoilPainter


@Composable
fun EmptyPosts(onReload: () -> Unit) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Text(text = stringResource(empty_posts))
        Button(onClick = onReload) {
            Text(text = stringResource(reload_posts))
        }
    }
}

@Composable
fun Loading(msg: String? = null) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("loading.json"),
    )
    val progress by animateLottieCompositionAsState(composition)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp),
            composition = composition,
            progress = progress
        )
        Text(
            text = msg ?: "Loading",
            style = typography.body1
        )
    }
}

@Composable
fun PaginationIndicator(modifier: Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("loading.json"),
    )
    val progress by animateLottieCompositionAsState(composition)

    Card(
        modifier = modifier.padding(8.dp)
    ) {
        LottieAnimation(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp),
            composition = composition,
            progress = progress
        )
    }
}

@Composable
fun LoadingError(error: Error, onReload: () -> Unit) {
    when (error) {
        NetworkError -> NetworkError(onReload)
    }
}

@Composable
fun NetworkError(onReload: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Text(
            text = stringResource(network_error),
            style = typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(msg_reload),
            style = typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onReload) {
            Text(text = stringResource(reload))
        }
    }
}

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
                    style = typography.body2,
                )
            }

            val tagSB = StringBuilder()
            if (post.isOriginalContent) {
                tagSB.append("[${stringResource(tag_oc)}]")
            }
            if (post.over18) {
                tagSB.append(" ${stringResource(tag_nsfw)}")
            }
            val tags = tagSB.toString()
            if (tags.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = tags,
                    style = typography.body2
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
                    style = typography.h5,
                    maxLines = 2,
                    overflow = Ellipsis,
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
                    contentScale = Crop
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
                    style = typography.body2
                )
                Text(
                    text = "${post.downVotes} Downs",
                    style = typography.body2
                )
                Text(
                    text = "${post.numComments} Comments",
                    style = typography.body2
                )
            }
        }
    }
}

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
                style = typography.body2
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        commentViewData.comment.body?.let {
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .semantics { testTag = "comment-body" },
                text = commentViewData.comment.body,
                style = typography.body1
            )
        }
        Divider()
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .semantics { testTag = "comment-score" },
            text = "${commentViewData.comment.score} Score",
            style = typography.body2
        )
        Divider()
    }
}

@Composable
fun PostDetail(
    post: Post,
    isLoadingComments: Boolean = false,
    comments: List<CommentViewData>?
) {
    Column {
        LazyColumn {
            item(post.postFullName) {
                Column(modifier = Modifier.padding(4.dp)) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = post.postedInfo,
                        style = typography.body2
                    )
                    post.title?.let {
                        Text(
                            modifier = Modifier.semantics {
                                testTag = "post-detail-title"
                            },
                            text = post.title,
                            style = typography.h5
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
                            contentScale = FillWidth
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
                            style = typography.body2
                        )
                        Text(
                            text = "${post.downVotes} Downs",
                            style = typography.body2
                        )
                        Text(
                            text = "${post.numComments} Comments",
                            style = typography.body2
                        )
                    }
                }
                Divider(modifier = Modifier.height(4.dp))
                Spacer(modifier = Modifier.height(4.dp))
            }
            if (isLoadingComments) {
                items(1) { Loading(stringResource(loading_comments)) }
            } else {
                comments?.let {
                    items(comments) { comment ->
                        Comment(commentViewData = comment)
                    }
                }
            }
        }
    }
}
