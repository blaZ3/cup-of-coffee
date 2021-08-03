package com.example.cupofcoffee.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.cupofcoffee.app.Post
import com.example.cupofcoffee.app.PostDetail
import com.example.cupofcoffee.app.views.detail.flatten
import com.example.cupofcoffee.ui.theme.CupOfCoffeeTheme
import com.example.cupofcoffee.utils.comments
import com.example.cupofcoffee.utils.post
import org.junit.Rule
import org.junit.Test

class PostViewTests {

    @get:Rule
    val composeTestRule = createComposeRule()


    @Test
    fun testPostIsShownCorrectly() {
        composeTestRule.setContent {
            CupOfCoffeeTheme {
                Post(post = post, onPostClicked = {})
            }
        }

        composeTestRule.onNodeWithText("Post title").assertIsDisplayed()
        composeTestRule.onNodeWithText("50 Ups").assertIsDisplayed()
        composeTestRule.onNodeWithText("50 Downs").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 Comments").assertIsDisplayed()
    }


    @Test
    fun testPostDetailIsShownCorrectly() {
        composeTestRule.setContent {
            CupOfCoffeeTheme {
                PostDetail(post = post, comments = comments.flatten())
            }
        }

        //post
        composeTestRule.onNodeWithText("Post title").assertIsDisplayed()
        composeTestRule.onNodeWithText("50 Ups").assertIsDisplayed()
        composeTestRule.onNodeWithText("50 Downs").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 Comments").assertIsDisplayed()
        //post

        //comments
        composeTestRule.onNodeWithText("Comment 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 Score").assertIsDisplayed()

        composeTestRule.onNodeWithText("Comment 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("20 Score").assertIsDisplayed()

        composeTestRule.onNodeWithText("Reply 1 for comment 2").assertIsDisplayed()
        composeTestRule.onNodeWithText("30 Score").assertIsDisplayed()
        //comments
    }

}
