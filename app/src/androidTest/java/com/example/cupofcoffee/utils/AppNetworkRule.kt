package com.example.cupofcoffee.utils

import androidx.test.espresso.Espresso
import com.example.cupofcoffee.createOkHttp
import com.example.cupofcoffee.helpers.test.Hooks
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class AppNetworkRule : TestRule {

    private val mockServer: MockWebServer = MockWebServer()

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                mockServer.start()
                Hooks.baseUrl = mockServer.url("").toString().run {
                    this.substring(0, this.length - 1)
                }
                Hooks.okHttpClient = createOkHttp().also {
                    val idlingResource = OkHttp3IdlingResource
                        .create("okhttp", it)
                    Espresso.registerIdlingResources(idlingResource)
                }
                base?.evaluate()
                Hooks.baseUrl = null
                mockServer.shutdown()
            }
        }
    }


    fun enqueueResponse(response: MockResponse) {
        mockServer.enqueue(response)
    }
}
