package com.example.cupofcoffee.app.data.models

import com.example.cupofcoffee.ApplicationJsonAdapterFactory
import com.example.cupofcoffee.app.data.models.DataChild.CommentData
import com.example.cupofcoffee.app.data.models.ResultType.Listing
import com.example.cupofcoffee.helpers.json.ApiResultEmptyStringToNullAdapter
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Test
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.StandardCharsets

class CommentJsonTests {

    private val moshi = Moshi.Builder()
        .add(ApiResultEmptyStringToNullAdapter)
        .add(ApplicationJsonAdapterFactory)
        .build()

    /*
    This test is only to help development while adding JSON parsing not to verify that
    Moshi works, feel free to delete later
    */
    @Test
    fun testCommentJsonParsingWorks() {
        val json = readJson("comments.json")

        val type = Types.newParameterizedType(List::class.java, ApiResult::class.java)
        val adapter = moshi.adapter<List<ApiResult>>(type)

        val commentResult = adapter.fromJson(json)

        assertThat(commentResult?.first()?.resultType).isEqualTo(Listing)
        assertThat(commentResult?.first()?.data?.children?.first())
            .isInstanceOf(DataChild.PostData::class.java)
        assertThat(commentResult?.get(1)?.data?.children?.first())
            .isInstanceOf(CommentData::class.java)

        assertThat((commentResult?.get(1)?.data?.children?.first() as CommentData).data?.replies)
            .isInstanceOf(ApiResult::class.java)
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
