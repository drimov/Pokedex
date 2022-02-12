package com.drimov.pokedex.domain.use_case

import com.drimov.pokedex.data.remote.dto.PokemonListGeneration
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow

class GetPokemonList(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(generation: String): Flow<Resource<PokemonListGeneration>> {
        return repository.getPokemonList(generation)
    }
}