package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.models.*
import com.example.cupofcoffee.app.data.network.RedditApi
import com.example.cupofcoffee.app.data.network.RedditService
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.views.detail.PostDetailAction
import com.example.cupofcoffee.app.views.detail.PostDetailAction.ReLoadPostAndComments
import com.example.cupofcoffee.app.views.detail.PostDetailModel
import com.example.cupofcoffee.app.views.detail.PostDetailViewState
import com.example.cupofcoffee.helpers.coroutine.TestManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.start
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Response.success

@ExperimentalCoroutinesApi
class PostDetailModelTests {

    private val api = mock<RedditApi>()
    private val log = mock<Log>()
    private val postRepository = PostRepository(RedditService(api = api), log = log)

    private val model = PostDetailModel(postRepository, TestCoroutineDispatcher())

    private val postResult = ApiResult(
        resultType = ResultType.Listing,
        data = Data(
            modhash = "modhash",
            children = listOf(
                DataChild.PostData(
                    data = Post(
                        title = "Test post"
                    )
                )
            )
        )
    )
    private val commentResult = ApiResult(
        resultType = ResultType.Listing,
        data = Data(
            modhash = "modhash",
            children = listOf(
                DataChild.CommentData(
                    data = Comment(
                        body = "comment"
                    )
                )
            )
        )
    )

    @Test
    fun `test model init emits error when subreddit is null`() = runBlockingTest {
        val post = Post(
            title = "Post title",
            permalink = "xyz",
            subreddit = null,
            postFullName = "t3_sdfjghdk"
        )

        val actions = mutableListOf<PostDetailAction>()
        val viewStates = mutableListOf<PostDetailViewState>()

        val actionJob = launch { model.actions.toList(actions) }
        val viewStateJob = launch { model.viewState.toList(viewStates) }

        model.init(
            TestManagedCoroutineScope(TestCoroutineScope()),
            post
        )

        assertThat(viewStates.size).isEqualTo(2)
        assertThat(viewStates[1].showLoadingError).isEqualTo(true)

        actionJob.cancel()
        viewStateJob.cancel()
    }

    @Test
    fun `test model init emits error when postfullname is null`() = runBlockingTest {
        val post = Post(
            title = "Post title",
            permalink = "xyz",
            subreddit = "subreddit",
            postFullName = null
        )

        val actions = mutableListOf<PostDetailAction>()
        val viewStates = mutableListOf<PostDetailViewState>()

        val actionJob = launch { model.actions.toList(actions) }
        val viewStateJob = launch { model.viewState.toList(viewStates) }

        model.init(
            TestManagedCoroutineScope(TestCoroutineScope()),
            post
        )

        assertThat(viewStates.size).isEqualTo(2)
        assertThat(viewStates[1].showLoadingError).isEqualTo(true)

        actionJob.cancel()
        viewStateJob.cancel()
    }


    @Test
    fun `test model init emit view state with correct values when subreddit and postfullname is not null`() =
        runBlockingTest {
            val post = Post(
                title = "Post title",
                permalink = "xyz",
                subreddit = "subreddit",
                postFullName = "t3_shortName"
            )

            val actions = mutableListOf<PostDetailAction>()
            val viewStates = mutableListOf<PostDetailViewState>()

            val actionJob = launch { model.actions.toList(actions) }
            val viewStateJob = launch { model.viewState.toList(viewStates) }

            model.init(
                TestManagedCoroutineScope(TestCoroutineScope()),
                post
            )

            assertThat(viewStates.size).isEqualTo(2)
            assertThat(viewStates[1].showLoadingError).isEqualTo(false)
            assertThat(viewStates[1].isLoadingComments).isEqualTo(true)
            assertThat(viewStates[1].subReddit).isEqualTo(post.subreddit)
            assertThat(viewStates[1].postShortName).isEqualTo("shortName")

            actionJob.cancel()
            viewStateJob.cancel()
        }


    @Test
    fun `test model init emit view state with loading and makes api call on init`() =
        runBlockingTest {

            whenever(api.getPostDetail(any(), any(), any())).thenReturn(
                success(
                    listOf(
                        postResult,
                        commentResult
                    )
                )
            )

            val post = Post(
                title = "Post title",
                permalink = "xyz",
                subreddit = "subreddit",
                postFullName = "t3_shortName"
            )

            val actions = mutableListOf<PostDetailAction>()
            val viewStates = mutableListOf<PostDetailViewState>()

            val actionJob = launch { model.actions.toList(actions) }
            val viewStateJob = launch { model.viewState.toList(viewStates) }

            model.init(
                TestManagedCoroutineScope(TestCoroutineScope()),
                post
            )

            assertThat(viewStates.size).isEqualTo(3)
            assertThat(viewStates.start().showLoadingError).isEqualTo(false)
            assertThat(viewStates.start().isLoadingComments).isEqualTo(true)
            assertThat(viewStates.start().subReddit).isEqualTo(post.subreddit)
            assertThat(viewStates.start().postShortName).isEqualTo("shortName")

            verify(api).getPostDetail(eq("subreddit"), eq("shortName"), any())

            assertThat(viewStates.last().isLoadingComments).isEqualTo(false)
            assertThat(viewStates.last().showLoadingError).isEqualTo(false)
            assertThat(viewStates.last().comments).isNotEmpty()

            actionJob.cancel()
            viewStateJob.cancel()
        }

    @Test
    fun `test model init emit view state with loading and makes api call twice on reload after init`() =
        runBlockingTest {

            whenever(api.getPostDetail(any(), any(), any())).thenReturn(
                success(
                    listOf(
                        postResult,
                        commentResult
                    )
                )
            )

            val post = Post(
                title = "Post title",
                permalink = "xyz",
                subreddit = "subreddit",
                postFullName = "t3_shortName"
            )

            val actions = mutableListOf<PostDetailAction>()
            val viewStates = mutableListOf<PostDetailViewState>()

            val actionJob = launch { model.actions.toList(actions) }
            val viewStateJob = launch { model.viewState.toList(viewStates) }

            model.init(
                TestManagedCoroutineScope(TestCoroutineScope()),
                post
            )

            model.actions.emit(ReLoadPostAndComments)

            assertThat(viewStates.size).isEqualTo(5)
            assertThat(viewStates.start().showLoadingError).isEqualTo(false)
            assertThat(viewStates.start().isLoadingComments).isEqualTo(true)
            assertThat(viewStates.start().subReddit).isEqualTo(post.subreddit)
            assertThat(viewStates.start().postShortName).isEqualTo("shortName")

            verify(api, times(2))
                .getPostDetail(eq("subreddit"), eq("shortName"), any())

            assertThat(viewStates.last().isLoadingComments).isEqualTo(false)
            assertThat(viewStates.last().showLoadingError).isEqualTo(false)
            assertThat(viewStates.last().comments).isNotEmpty()

            actionJob.cancel()
            viewStateJob.cancel()
        }
}
