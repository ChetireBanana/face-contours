package com.example.facecontours.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "settings")


interface DataStoreInterface {
    val isFirstLaunchFlow: Flow<Boolean>
    suspend fun setFirstLaunchDone()
}

@Singleton
class DataStore @Inject constructor(
    @ApplicationContext private val context: Context
) : DataStoreInterface {

    companion object {
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("is_first_launch")
    }

    override val isFirstLaunchFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[FIRST_LAUNCH_KEY] != false }


    override suspend fun setFirstLaunchDone() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false
        }
    }
}