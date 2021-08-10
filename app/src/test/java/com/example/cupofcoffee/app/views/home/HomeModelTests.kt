package com.example.cupofcoffee.app.views.home

import com.example.cupofcoffee.app.data.models.*
import com.example.cupofcoffee.app.data.models.ResultType.Listing
import com.example.cupofcoffee.app.data.network.RedditApi
import com.example.cupofcoffee.app.data.network.RedditService
import com.example.cupofcoffee.app.data.repository.PostRepository
import com.example.cupofcoffee.app.data.repository.UserSettingsRepository
import com.example.cupofcoffee.app.data.store.usersettings.UserSettingsDataStore
import com.example.cupofcoffee.helpers.coroutine.TestManagedCoroutineScope
import com.example.cupofcoffee.helpers.log.Log
import com.example.cupofcoffee.helpers.start
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response.error
import retrofit2.Response.success

@ExperimentalCoroutinesApi
class HomeModelTests {

    private val api = mock<RedditApi>()
    private val postRepository = PostRepository(RedditService(api = api), log = mock())
    private val userSettingsDataStore = mock<UserSettingsDataStore>()
    private val userSettingsRepository = UserSettingsRepository(userSettingsDataStore)
    private val log = mock<Log>()

    private val model = HomeModel(postRepository, userSettingsRepository, log)

    private val scope = TestManagedCoroutineScope(TestCoroutineScope())

    private val result = ApiResult(
        resultType = Listing,
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

    @Test
    fun `test home model emits correct view state for success`() = runBlockingTest {
        whenever(api.getPosts(any(), anyOrNull())).thenReturn(success(result))

        val selectedSubRedditFlow = MutableStateFlow(SubReddit("test"))
        whenever(userSettingsDataStore.getSelectedSubReddit()).thenReturn(selectedSubRedditFlow)

        val viewStates = mutableListOf<HomeViewState>()
        val job = launch { model.viewState.toList(viewStates) }

        model.init(scope)

        assertThat(viewStates.start().isLoading).isEqualTo(true)

        assertThat(viewStates.last().isLoading).isEqualTo(false)
        assertThat(viewStates.last().showLoadingError).isEqualTo(false)
        assertThat(viewStates.last().showEmptyPosts).isEqualTo(false)
        assertThat(viewStates.last().posts).isNotEmpty()
        job.cancel()
    }

    @Test
    fun `test home model emits view state with loading error`() = runBlockingTest {
        whenever(api.getPosts(any(), anyOrNull())).thenReturn(
            error(400, "{}".toResponseBody("application/json".toMediaType()))
        )

        val selectedSubRedditFlow = MutableStateFlow(SubReddit("test"))
        whenever(userSettingsDataStore.getSelectedSubReddit()).thenReturn(selectedSubRedditFlow)

        val viewStates = mutableListOf<HomeViewState>()
        val job = launch { model.viewState.toList(viewStates) }

        model.init(scope)

        assertThat(viewStates.size).isEqualTo(3)
        assertThat(viewStates.start().isLoading).isEqualTo(true)
        assertThat(viewStates.last().showLoadingError).isEqualTo(true)
        job.cancel()
    }

}
