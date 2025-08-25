package com.example.timer_quiz.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PreferencesManager(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val SCHEDULED_TIME_KEY = longPreferencesKey("scheduled_time")
    }

    fun getScheduledTime(): Flow<Long?> {
        return dataStore.data.map { preferences ->
            preferences[SCHEDULED_TIME_KEY]
        }
    }

    suspend fun setScheduledTime(timestamp: Long) {
        dataStore.edit { preferences ->
            preferences[SCHEDULED_TIME_KEY] = timestamp
        }
    }

    suspend fun clearScheduledTime() {
        dataStore.edit { preferences ->
            preferences.remove(SCHEDULED_TIME_KEY)
        }
    }
}