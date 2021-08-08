package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.DrawerValue.Closed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cupofcoffee.BuildConfig.VERSION_NAME
import com.example.cupofcoffee.R.string.*
import kotlinx.coroutines.launch


@Composable
fun SideDrawer(
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(Closed)
    val scope = rememberCoroutineScope()
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            LazyColumn(content = {
                item {
                    Text(text = stringResource(change_subreddit))
                    Button(onClick = { scope.launch { drawerState.close() } }) {
                        Text(text = "Close")
                    }
                    Divider(modifier = Modifier.height(4.dp))
                }
                items(4) {
                    Text(text = "item")
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    Divider(modifier = Modifier.height(4.dp))
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
