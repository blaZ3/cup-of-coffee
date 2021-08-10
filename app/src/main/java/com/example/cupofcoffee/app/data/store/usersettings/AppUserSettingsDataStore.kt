package com.example.cupofcoffee.app.data.store.usersettings

import androidx.datastore.core.DataStore
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.proto.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppUserSettingsDataStore constructor(
    private val dataStore: DataStore<UserSettings>
) : UserSettingsDataStore {

    override fun getSelectedSubReddit(): Flow<SubReddit> {
        return dataStore.data.map { SubReddit(it.selectedSubReddit) }
    }

    override fun getSubReddits(): Flow<List<SubReddit>> {
        return dataStore.data.map { it.subRedditsList.map { sub -> SubReddit(sub.name) } }
    }

    override suspend fun updateSelectedSubreddit(subReddit: SubReddit) {
        dataStore.updateData {
            it.toBuilder()
                .setSelectedSubReddit(subReddit.name)
                .build()
        }
    }


    override suspend fun updateListOfSubReddits(newList: List<SubReddit>) {
        dataStore.updateData {
            it.toBuilder()
                .clearSubReddits()
                .build()
        }

        dataStore.updateData {
            it.toBuilder()
                .addAllSubReddits(newList.map { sub ->
                    com.example.cupofcoffee.app.proto.SubReddit.newBuilder().setName(sub.name)
                        .build()
                })
                .build()
        }
    }
}
