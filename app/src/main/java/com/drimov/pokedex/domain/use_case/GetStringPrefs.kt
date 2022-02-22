package com.drimov.pokedex.domain.use_case

import com.drimov.pokedex.domain.repository.UserPrefsRepository
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

class GetStringPrefs(
    private val repository: UserPrefsRepository
) {
    suspend operator fun invoke(key:String): Flow<Resource<String?>> {
        return repository.getStringPrefs(key)
    }
}