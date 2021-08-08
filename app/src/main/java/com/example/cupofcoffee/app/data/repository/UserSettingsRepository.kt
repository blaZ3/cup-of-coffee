package com.example.cupofcoffee.app.data.repository

import com.example.cupofcoffee.app.data.store.usersettings.UserDataStore
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataStore: UserDataStore
) {
}
