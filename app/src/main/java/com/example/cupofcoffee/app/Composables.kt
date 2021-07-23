package com.example.cupofcoffee.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cupofcoffee.Error
import com.example.cupofcoffee.app.data.Post
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.ui.theme.Purple700
import com.google.accompanist.coil.rememberCoilPainter


@Composable
fun EmptyPosts(onReload: () -> Unit) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Text(text = "Empty posts")
        Button(onClick = onReload) {
            Text(text = "Reload posts")
        }
    }
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center
    ) {
        Text(
            text = "Loading...",
            color = Purple700,
            style = typography.h6
        )
    }
}

@Composable
fun PaginationIndicator(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Loading more posts..."
        )
    }
}

@Composable
fun LoadingError(error: Error, onReload: () -> Unit) {
    when (error) {
        Error.NetworkError -> NetworkError(onReload)
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
            text = "Network error",
            style = typography.h5
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Try reloading",
            style = typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onReload) {
            Text(text = "Reload")
        }
    }
}

@Composable
fun Post(post: Post,  log: Log? = null, onPostClicked: (post: Post) -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable {
                onPostClicked(post)
            }
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row {
                post.subreddit?.let {
                    Text(
                        text = "r/${post.subreddit}",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                if (post.isOriginalContent) {
                    Text(
                        text = "[OC]",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
                if (post.over18) {
                    Text(
                        text = "NSFW",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            post.title?.let {
                Text(
                    text = post.title,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = Ellipsis
                )
            }
            if (post.isImage && post.cleanedImageUrl != null) {
                Image(
                    painter = rememberCoilPainter(request = post.cleanedImageUrl),
                    contentDescription = post.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(200.dp)
                        .clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = Crop
                )
            }
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = post.created.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.name.toString(),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}
