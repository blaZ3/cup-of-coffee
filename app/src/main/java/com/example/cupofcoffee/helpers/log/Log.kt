package com.example.cupofcoffee.helpers.log


interface Log {
    fun d(message: String)
    fun w(message: String)
    fun e(message: String)
}


class AndroidLog(private val tag: String) : Log {
    override fun d(message: String) {
        android.util.Log.d(tag, message)
    }

    override fun w(message: String) {
        android.util.Log.w(tag, message)
    }

    override fun e(message: String) {
        android.util.Log.e(tag, message)
    }
}
