package com.drimov.pokedex.domain.use_case

import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonSpecies
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.util.ResourceMultiple
import kotlinx.coroutines.flow.Flow

class GetPokemon(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): Flow<ResourceMultiple<Pokemon,PokemonSpecies>> {
        return repository.getPokemon(id)
    }
}