package com.example.cupofcoffee.app.data.store.usersettings

import androidx.datastore.core.DataStore
import com.example.cupofcoffee.UserSettingsDS
import com.example.cupofcoffee.app.data.models.SubReddit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserSettingsDataStore @Inject constructor(
    @UserSettingsDS private val userSettingsDS: DataStore<com.example.cupofcoffee.app.proto.UserSettings>
) {
    fun getSelectedSubReddit(): Flow<SubReddit> {
        return userSettingsDS.data.map { SubReddit(it.selectedSubReddit) }
    }

    fun getSubReddits(): Flow<List<SubReddit>> {
        return userSettingsDS.data.map { it.subRedditsList.map { sub -> SubReddit(sub.name) } }
    }

    suspend fun updateSelectedSubreddit(subReddit: SubReddit) {
        userSettingsDS.updateData {
            it.toBuilder()
                .setSelectedSubReddit(subReddit.name)
                .build()
        }
    }


    suspend fun updateListOfSubReddits(newList: List<SubReddit>) {
        userSettingsDS.updateData {
            it.toBuilder()
                .clearSubReddits()
                .build()
        }

        userSettingsDS.updateData {
            it.toBuilder()
                .addAllSubReddits(newList.map { sub ->
                    com.example.cupofcoffee.app.proto.SubReddit.newBuilder().setName(sub.name)
                        .build()
                })
                .build()
        }
    }
}
