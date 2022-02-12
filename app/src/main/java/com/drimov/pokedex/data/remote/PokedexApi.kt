package com.drimov.pokedex.data.remote

import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonListGeneration
import retrofit2.http.GET
import retrofit2.http.Path

interface PokedexApi {

    @GET("generation/{gen}")
    suspend fun gePokemonList(
        @Path("gen") generation: String
    ): PokemonListGeneration

    @GET("pokemon/{name}")
    suspend fun getPokemon(
        @Path("name") name: String
    ): Pokemon

}