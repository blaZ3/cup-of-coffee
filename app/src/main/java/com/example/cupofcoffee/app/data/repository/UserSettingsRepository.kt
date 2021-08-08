package com.example.cupofcoffee.app.data.repository

import com.example.cupofcoffee.app.data.models.UserSettings
import com.example.cupofcoffee.app.data.store.usersettings.UserSettingsDataStore
import javax.inject.Inject

class UserSettingsRepository @Inject constructor(
    private val userSettingsDataStore: UserSettingsDataStore
) {

    suspend fun getUserSettings(): UserSettings {
        return userSettingsDataStore.getUserSettings()
    }

}
