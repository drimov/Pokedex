package com.drimov.pokedex.domain.repository

import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonListGeneration
import com.drimov.pokedex.data.remote.dto.PokemonSpecy
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow


interface PokemonRepository {
    suspend fun getPokemonList(generation:String): Flow<Resource<PokemonListGeneration>>
    suspend fun getPokemon(name: String): Flow<Resource<Pokemon>>
}