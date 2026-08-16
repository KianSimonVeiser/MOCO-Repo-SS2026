package com.moco.DBNavigatorAlternative.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(
    name = "settings"
)

private val ONLY_DEUTSCHLANDTICKET_CONNECTIONS =
    booleanPreferencesKey("only_deutschlandticket_connections")

class SettingsPreference(
    private val context: Context
) {

    val onlyDeutschlandticketConnections: Flow<Boolean> =
        context.dataStore.data.map { settings ->
            settings[ONLY_DEUTSCHLANDTICKET_CONNECTIONS] ?: false
        }

    suspend fun setOnlyDeutschlandticketConnections(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[ONLY_DEUTSCHLANDTICKET_CONNECTIONS] = value
        }
    }
}