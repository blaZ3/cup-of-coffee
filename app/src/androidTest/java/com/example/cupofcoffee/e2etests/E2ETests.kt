package com.example.cupofcoffee.e2etests


import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cupofcoffee.MainActivity
import com.example.cupofcoffee.utils.AppNetworkRule
import com.example.cupofcoffee.utils.postDetailsResponse
import com.example.cupofcoffee.utils.postsResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit.MILLISECONDS

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class E2ETests {
    private val composeRule = createAndroidComposeRule<MainActivity>()
    private val appNetworkRule = AppNetworkRule()

    @Rule
    @JvmField
    val rule: RuleChain = RuleChain
        .outerRule(appNetworkRule)
        .around(composeRule)

    @Before
    fun setup() {
        //queue some responses with delay
        appNetworkRule.enqueueResponse(postsResponse)
        appNetworkRule.enqueueResponse(postDetailsResponse)
    }

    @Test
    fun testHomeLoadsPostsAndClickingOnOneGoesToDetailView() = runBlockingTest {
        //home view
        composeRule.onNodeWithText("How does one make sense of such error")
            .assertIsDisplayed()

        composeRule.onNodeWithText("How does one make sense of such error").performClick()


        //detail view
        composeRule.onNode(hasTestTag("post-detail-title")).assertIsDisplayed()
        composeRule.onAllNodes(
            hasText(
                "It means it works but it's still shown in red as an error because you are " +
                        "using Android and not Flutter plugins"
            )
        ).filter(hasTestTag("comment-body"))
            .onFirst()
            .assertIsDisplayed()
    }

}
