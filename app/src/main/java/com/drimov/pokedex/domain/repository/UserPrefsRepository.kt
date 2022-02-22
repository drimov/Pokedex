package com.drimov.pokedex.domain.repository

import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserPrefsRepository {

    suspend fun putStringPrefs(key: String,value: String)
    suspend fun getStringPrefs(key: String): Flow<Resource<String?>>
}