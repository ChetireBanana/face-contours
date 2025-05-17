package com.example.facecontours.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class DataStore @Inject constructor(
    private val context: Context
) {

    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
    }

    val isFirstLaunchFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[FIRST_LAUNCH_KEY] != false }


    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false
        }
    }
}