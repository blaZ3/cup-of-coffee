package com.example.cupofcoffee.app.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cupofcoffee.R.string.*

@Composable
fun AddSubRedditDialog(
    onDismissRequest: () -> Unit,
    onSubRedditAdded: (subReddit: String) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .height(300.dp)
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Center
            ) {

                val subRedditName = remember {
                    mutableStateOf("")
                }

                Text(
                    text = stringResource(add_new_sub),
                    style = typography.h5
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    value = subRedditName.value,
                    modifier = Modifier.padding(16.dp),
                    placeholder = { Text(text = stringResource(add_sub_placeholder)) },
                    onValueChange = {
                        subRedditName.value = it
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = {
                    onSubRedditAdded(subRedditName.value)
                }) {
                    Text(text = stringResource(add))
                }
            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun AddSubRedditDialogPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        AddSubRedditDialog(onDismissRequest = {}, onSubRedditAdded = {})
    }
}
