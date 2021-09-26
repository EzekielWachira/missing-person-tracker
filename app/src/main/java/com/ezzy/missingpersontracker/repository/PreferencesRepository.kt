package com.ezzy.missingpersontracker.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ezzy.missingpersontracker.util.Constants.PREFERENCE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepository(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCE_NAME)

    private object PreferencesKeys {
        val userEmail = stringPreferencesKey("user_email")
        val isUserLoggedIn = booleanPreferencesKey("is_user_logged_in")
        val hasUserWalkedThrough = booleanPreferencesKey("has_user_walked_through")
        val isDarkThemeEnabled = booleanPreferencesKey("is_dark_theme_enabled")
    }

    suspend fun saveUserEmailToDataStore(userEmail: String) {
        context.dataStore.edit { preference ->
            preference[PreferencesKeys.userEmail] = userEmail
        }
    }

    suspend fun saveUserLoginStatus(loginStatus: Boolean) {
        context.dataStore.edit { preference ->
            preference[PreferencesKeys.isUserLoggedIn] = loginStatus
        }
    }

    suspend fun saveWalkThroughStatus(walkThroughStatus: Boolean) {
        context.dataStore.edit { preference ->
            preference[PreferencesKeys.hasUserWalkedThrough] = walkThroughStatus
        }
    }

    suspend fun configureAppTheme(isDarkThemeEnabled: Boolean) {
        context.dataStore.edit { preference ->
            preference[PreferencesKeys.isDarkThemeEnabled] = isDarkThemeEnabled
        }
    }

    val userEmailFromDataStore: Flow<String> =context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[PreferencesKeys.userEmail] ?: ""
        }

    val loginStatusFromDataStore: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[PreferencesKeys.isUserLoggedIn] ?: false
        }

    val walkThroughStatusFromDataStore = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[PreferencesKeys.hasUserWalkedThrough] ?: false
        }

    val themeStatus = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
            else throw exception
        }.map { preferences ->
            preferences[PreferencesKeys.isDarkThemeEnabled] ?: false
        }
}