package com.example.cupofcoffee.app.data.store.usersettings

import androidx.datastore.core.DataStore
import com.example.cupofcoffee.UserSettingsDS
import com.example.cupofcoffee.app.data.models.SubReddit
import com.example.cupofcoffee.app.data.models.UserSettings
import javax.inject.Inject

class UserSettingsDataStore @Inject constructor(
    @UserSettingsDS private val userSettingsDS: DataStore<com.example.cupofcoffee.app.proto.UserSettings>
) {

    suspend fun getUserSettings(): UserSettings {
//        return userSettingsDS.data.first().run {
//            UserSettings(
//                selectedSubReddit = this.selectedSubReddit,
//                subReddits = this.subRedditsList.map { SubReddit(name = it.name) }
//            )
//        }


        return UserSettings(
            selectedSubReddit = SubReddit("popular"),
            subReddits = listOf(
                SubReddit("popular"),
                SubReddit("bitcoin"),
                SubReddit("videos"),
                SubReddit("pics"),
            )
        )
    }

}
