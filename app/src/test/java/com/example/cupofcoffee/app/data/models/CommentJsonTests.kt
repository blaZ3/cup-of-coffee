package com.example.cupofcoffee.app.data.models

import com.example.cupofcoffee.ApplicationJsonAdapterFactory
import com.example.cupofcoffee.app.data.models.ResultType.Listing
import com.google.common.truth.Truth
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Test
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.StandardCharsets

class CommentJsonTests {

    private val moshi = Moshi.Builder()
        .add(ApplicationJsonAdapterFactory)
        .build()

    @Test
    fun test() {
        val json = readJson("comments.json")

        val type = Types.newParameterizedType(List::class.java, ApiResult::class.java)
        val adapter = moshi.adapter<List<ApiResult>>(type)

        val commentResult = adapter.fromJson(json)

        Truth.assertThat(commentResult?.first()?.resultType).isEqualTo(Listing)

    }

    private fun readJson(fileName: String): String {
        val inputStream = this.javaClass.classLoader.getResourceAsStream(fileName)
        val reader: Reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val sb = StringBuilder()
        var value = reader.read()
        while (value != -1) {
            sb.append(value.toChar())
            value = reader.read()
        }
        return sb.toString()
    }
}
