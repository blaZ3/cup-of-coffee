package com.example.cupofcoffee.helpers.json

import com.example.cupofcoffee.app.data.models.ApiResult
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader

object ApiResultEmptyStringToNullAdapter {
    @FromJson
    fun fromJson(jsonReader: JsonReader, apiAdapter: JsonAdapter<ApiResult>): ApiResult? {
        if (jsonReader.peek() == JsonReader.Token.STRING) {
            jsonReader.nextString()
            return null
        }
        return apiAdapter.fromJson(jsonReader)
    }
}
