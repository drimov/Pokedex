package com.drimov.pokedex.domain.repository

import com.drimov.pokedex.data.remote.dto.*
import com.drimov.pokedex.domain.model.PokemonData
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {
    suspend fun getPokemonList(generation: String): Flow<Resource<PokemonListGeneration>>
    suspend fun getPokemon(id: Int): Flow<Resource<PokemonData>>
}