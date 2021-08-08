package com.example.cupofcoffee.app.data.models


data class UserSettings(
    val selectedSubReddit: String,
    val subReddits: List<SubReddit>
)


data class SubReddit(
    val name: String
)
