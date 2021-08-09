package com.example.cupofcoffee.app.data.repository

import com.example.cupofcoffee.app.data.models.POPULAR_SUB_REDDIT
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.data.models.defaultSubs
import com.example.cupofcoffee.app.data.store.usersettings.UserSettingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val userSettingsDataStore: UserSettingsDataStore
) {

    fun getUserSelectedSubReddit(): Flow<SubReddit> = flow {
        userSettingsDataStore.getSelectedSubReddit()
            .distinctUntilChanged()
            .collect { if (it.name.isEmpty()) emit(POPULAR_SUB_REDDIT) else emit(it) }
    }

    fun getUserSubReddits(): Flow<List<SubReddit>> = flow {
        userSettingsDataStore.getSubReddits()
            .collect {
                if (it.isNullOrEmpty()) {
                    emit(defaultSubs)
                } else {
                    emit(defaultSubs.toMutableList().also { mutableList ->
                        mutableList.addAll(it)
                    })
                }
            }
    }

    suspend fun changeSelectedSubReddit(subReddit: SubReddit) {
        userSettingsDataStore.updateSelectedSubreddit(subReddit)
    }

    suspend fun updateSubReddits(newList: List<SubReddit>) {
        userSettingsDataStore.updateListOfSubReddits(newList)
    }
}
