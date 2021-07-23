package com.example.cupofcoffee


sealed class Error {
    object NetworkError : Error()
}
