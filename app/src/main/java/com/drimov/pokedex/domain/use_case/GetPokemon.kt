package com.drimov.pokedex.domain.use_case

import com.drimov.pokedex.domain.model.PokemonData
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

class GetPokemon(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): Flow<Resource<PokemonData>> {
        return repository.getPokemon(id)
    }
}