package com.example.cupofcoffee.helpers.network

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class NetworkError<T>(val code: Int, val t: Throwable? = null) : Result<T>()
    data class NetworkFailure<T>(val t: Throwable) : Result<T>()
}
