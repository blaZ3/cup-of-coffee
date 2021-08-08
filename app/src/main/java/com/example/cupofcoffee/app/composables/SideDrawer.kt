package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.DrawerValue.Closed
import androidx.compose.material.DrawerValue.Open
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupofcoffee.BuildConfig.VERSION_NAME
import com.example.cupofcoffee.R.string.*
import com.example.cupofcoffee.app.data.models.SubReddit
import kotlinx.coroutines.launch


@Composable
fun SideDrawer(
    selectedSubReddit: SubReddit,
    subReddits: List<SubReddit>,
    startState: DrawerValue = Closed,
    onChangeSubReddit: (subReddit: SubReddit) -> Unit,
    onAddNewSubReddit: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(startState)
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            LazyColumn(content = {
                item {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = stringResource(change_subreddit),
                        style = typography.h5
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.height(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }
                items(subReddits) { subReddit ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(true) {
                                scope.launch {
                                    drawerState.close()
                                    onChangeSubReddit(subReddit)
                                }
                            },
                        style = typography.h6,
                        textAlign = TextAlign.Center,
                        color = if (subReddit.name == selectedSubReddit.name)
                            colors.primary else colors.secondary,
                        text = "r/${subReddit.name}",
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Divider(modifier = Modifier.height(4.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    onAddNewSubReddit()
                                }
                            }) {
                            Text(text = stringResource(add_new))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(modifier = Modifier.height(4.dp))
                    }

                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(version).replace(
                            stringResource(version_placeholder),
                            VERSION_NAME
                        )
                    )
                }
            })
        }, content = content
    )
}


@Composable
@Preview(showBackground = true)
fun SideDrawerPreview() {
    SideDrawer(
        selectedSubReddit = SubReddit("popular"),
        subReddits = listOf(
            SubReddit("popular"),
            SubReddit("bitcoin"),
            SubReddit("videos"),
            SubReddit("pics"),
        ),
        startState = Open,
        onChangeSubReddit = {

        },
        onAddNewSubReddit = {

        },
    ) {
        Text(text = "Main content")
    }
}
