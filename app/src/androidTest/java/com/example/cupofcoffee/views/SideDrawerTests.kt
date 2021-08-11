package com.example.cupofcoffee.views

import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.cupofcoffee.app.composables.SideDrawer
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.data.models.defaultSubs
import org.junit.Rule
import org.junit.Test

class SideDrawerTests {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val subs = listOf(
        SubReddit("Sub1"),
        SubReddit("Sub2"),
        SubReddit("Sub3"),
        SubReddit("Sub4"),
    )

    @Test
    fun testSubsAreShown() {
        composeTestRule.setContent {
            SideDrawer(
                startState = DrawerValue.Open,
                selectedSubReddit = subs.first(),
                subReddits = subs,
                onChangeSubReddit = {},
                onAddNewSubReddit = { },
                onRemoveSubReddit = {}
            ) {
                Text(text = "This is test content")
            }
        }

        subs.forEach { sub ->
            composeTestRule.onNodeWithText("r/${sub.name}").assertIsDisplayed()
            composeTestRule.onNodeWithText("r/${sub.name}").assertIsEnabled()
        }
    }


    @Test
    fun testRemoveSubIconIsShowForAllNonDefaultSubs() {
        composeTestRule.setContent {
            SideDrawer(
                startState = DrawerValue.Open,
                selectedSubReddit = subs.first(),
                subReddits = defaultSubs.toMutableList().also { it.addAll(subs) },
                onChangeSubReddit = {},
                onAddNewSubReddit = { },
                onRemoveSubReddit = {}
            ) {
                Text(text = "This is test content")
            }
        }

        defaultSubs.forEach { sub ->
            composeTestRule.onNodeWithTag("remove-sub-${sub.name}").assertDoesNotExist()
        }
        subs.forEach { sub ->
            composeTestRule.onNodeWithTag("remove-sub-${sub.name}").assertIsDisplayed()
        }
    }
}
