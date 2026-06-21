package com.jannahkurniawati0024.ecosnap.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jannahkurniawati0024.ecosnap.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class AuthManager(private val context: Context) {

    companion object {
        val KEY_USER_ID = stringPreferencesKey("user_id")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }

    val userProfileFlow: Flow<UserProfile?> = context.dataStore.data.map { prefs ->
        val id = prefs[KEY_USER_ID]
        if (id.isNullOrEmpty()) null
        else UserProfile(
            id = id,
            name = prefs[KEY_USER_NAME] ?: "",
            email = prefs[KEY_USER_EMAIL] ?: "",
            photoUrl = prefs[KEY_USER_PHOTO_URL] ?: ""
        )
    }

    suspend fun saveUser(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = profile.id
            prefs[KEY_USER_NAME] = profile.name
            prefs[KEY_USER_EMAIL] = profile.email
            prefs[KEY_USER_PHOTO_URL] = profile.photoUrl
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}