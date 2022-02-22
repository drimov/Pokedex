package com.drimov.pokedex.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.drimov.pokedex.domain.repository.UserPrefsRepository
import com.drimov.pokedex.util.Constants
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException

private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES)

class UserPrefsRepositoryImpl(
    private val context: Context
) : UserPrefsRepository {

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun putStringPrefs(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun getStringPrefs(key: String): Flow<Resource<String?>> =
        flow {
            emit(Resource.Loading<String?>())
            try {
                val preferencesKey = stringPreferencesKey(key)
                val preferences = context.dataStore.data.first()
                emit(Resource.Success<String?>(preferences[preferencesKey]))

            } catch (e: IOException) {
                emit(
                    Resource.Error<String?>(
                        message = Constants.ioExceptionErr
                    )
                )
            }
        }
}