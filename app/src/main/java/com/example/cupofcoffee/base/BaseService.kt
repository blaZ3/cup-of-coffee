package com.example.cupofcoffee.base

import com.example.cupofcoffee.helpers.network.Result
import com.example.cupofcoffee.helpers.network.Result.*
import retrofit2.Response

abstract class BaseService {
    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
        val response: Response<T>?
        try {
            response = call.invoke()
        } catch (ex: Exception) {
            return NetworkFailure(ex)
        }
        if (response.isSuccessful) {
            response.body()?.let { return Success(it) } ?: return NetworkFailure(
                IllegalStateException("Response body is null")
            )
        } else {
            return NetworkError(response.code())
        }
    }
}
