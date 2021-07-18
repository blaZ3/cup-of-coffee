package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.*
import com.example.cupofcoffee.app.data.ResultKind.Listing
import com.example.cupofcoffee.app.data.network.RedditApi
import com.example.cupofcoffee.app.data.network.RedditService
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.helpers.coroutine.TestManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response.error
import retrofit2.Response.success

@ExperimentalCoroutinesApi
class HomeModelTests {

    private val api = mock<RedditApi>()
    private val postRepository = PostRepository(RedditService(api = api), log = mock())
    private val log = mock<Log>()

    private val model = HomeModel(postRepository, log)

    private val scope = TestManagedCoroutineScope(TestCoroutineScope())

    private val result = PostResult(
        kind = Listing,
        data = PostResultData(
            modhash = "modhash",
            children = listOf(
                PostInfo(
                    kind = "t3",
                    post = Post(
                        title = "Test post"
                    )
                )
            )
        )
    )

    @Test
    fun `test home model emits correct view state for success`() = runBlockingTest {
        whenever(api.getPosts(any())).thenReturn(success(result))
        val viewStates = mutableListOf<HomeViewState>()
        val job = launch { model.viewState.toList(viewStates) }

        model.init(scope)

        assertThat(viewStates.size).isEqualTo(3)
        assertThat(viewStates[0].isLoading).isEqualTo(false)
        assertThat(viewStates[1].isLoading).isEqualTo(true)
        assertThat(viewStates[2].isLoading).isEqualTo(false)
        assertThat(viewStates[2].showLoadingError).isEqualTo(false)
        assertThat(viewStates[2].showEmptyPosts).isEqualTo(false)
        assertThat(viewStates[2].posts).isNotEmpty()
        job.cancel()
    }

    @Test
    fun `test home model emits view state with loading error`() = runBlockingTest {
        whenever(api.getPosts(any())).thenReturn(
            error(400, "{}".toResponseBody("application/json".toMediaType()))
        )
        val viewStates = mutableListOf<HomeViewState>()
        val job = launch { model.viewState.toList(viewStates) }

        model.init(scope)

        assertThat(viewStates.size).isEqualTo(3)
        assertThat(viewStates[2].showLoadingError).isEqualTo(true)
        job.cancel()
    }

}
