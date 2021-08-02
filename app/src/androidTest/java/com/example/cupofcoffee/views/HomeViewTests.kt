package com.example.cupofcoffee.views


import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cupofcoffee.app.views.home.HomeView
import com.example.cupofcoffee.helpers.navigation.AppNavigator
import com.example.cupofcoffee.helpers.test.EmptyActivity
import com.example.cupofcoffee.helpers.test.Hooks
import okhttp3.mockwebserver.MockWebServer
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class HomeViewTests {

    @Rule
    @JvmField
    val rule = ActivityScenarioRule(EmptyActivity::class.java)

    private val mockServer: MockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockServer.start()
        Hooks.baseUrl = mockServer.url("").toString().run {
            this.substring(0, this.length - 1)
        }
    }

    @Test
    fun testHomeViewLoadsPosts() {
        mockServer.enqueue(postsResponse)
        rule.scenario.onActivity {
            val navigator = AppNavigator(it)
            it.frameLayout.removeAllViews()
            it.frameLayout.addView(HomeView(it, navigator))
        }
    }

    @After
    fun tearDown() {
        Hooks.baseUrl = null
        mockServer.shutdown()
    }

}
