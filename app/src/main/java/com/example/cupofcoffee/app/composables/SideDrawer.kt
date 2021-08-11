package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.DrawerValue.Open
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cupofcoffee.BuildConfig.VERSION_NAME
import com.example.cupofcoffee.R.drawable.ic_remove
import com.example.cupofcoffee.R.string.*
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.data.models.defaultSubNames
import kotlinx.coroutines.launch
import toSubRedditName


@Composable
fun SideDrawer(
    selectedSubReddit: SubReddit,
    subReddits: List<SubReddit>,
    drawerState: DrawerState,
    onChangeSubReddit: (subReddit: SubReddit) -> Unit,
    onAddNewSubReddit: () -> Unit,
    onRemoveSubReddit: (subRedditName: String) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            LazyColumn(content = {
                item {
                    Row(verticalAlignment = CenterVertically) {
                        Text(
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(0.75f),
                            text = stringResource(change_subreddit),
                            style = typography.h5
                        )
                        Button(
                            modifier = Modifier
                                .weight(0.25f)
                                .padding(4.dp),
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                    onAddNewSubReddit()
                                }
                            }) {
                            Text(text = stringResource(add))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(modifier = Modifier.height(4.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(subReddits) { subReddit ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier
                                .weight(0.9f)
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
                            text = subReddit.name.toSubRedditName(),
                        )
                        Row(modifier = Modifier.weight(0.1f)) {
                            if (!defaultSubNames.contains(subReddit.name)) {
                                Icon(
                                    modifier = Modifier
                                        .semantics {
                                            this.testTag = "remove-sub-${subReddit.name}"
                                        }
                                        .clickable(true) {
                                            scope.launch {
                                                drawerState.close()
                                                onRemoveSubReddit(subReddit.name)
                                            }
                                        },
                                    painter = painterResource(id = ic_remove),
                                    contentDescription = stringResource(remove_subreddit)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider()
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
        rememberDrawerState(initialValue = Open),
        onChangeSubReddit = {

        },
        onAddNewSubReddit = {

        },
        onRemoveSubReddit = {

        },
    ) {
        Text(text = "Main content")
    }
}
