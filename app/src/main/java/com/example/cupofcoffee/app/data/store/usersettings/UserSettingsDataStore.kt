package com.example.cupofcoffee.app.data.store.usersettings

import com.example.cupofcoffee.app.data.models.SubReddit
import kotlinx.coroutines.flow.Flow

interface UserSettingsDataStore {
    fun getSelectedSubReddit(): Flow<SubReddit>
    fun getSubReddits(): Flow<List<SubReddit>>

    suspend fun updateSelectedSubreddit(subReddit: SubReddit)
    suspend fun updateListOfSubReddits(newList: List<SubReddit>)
}
