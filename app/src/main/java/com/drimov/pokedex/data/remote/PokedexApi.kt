package com.drimov.pokedex.data.remote

import com.drimov.pokedex.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokedexApi {

    @GET("generation/{gen}")
    suspend fun gePokemonList(
        @Path("gen") generation: String
    ): PokemonListGeneration

    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): Response<Pokemon>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): Response<PokemonSpecies>

    // -------------
    @GET("growth-rate/{id}")
    suspend fun getGrowthRate(
        @Path("id") id: Int
    ): Response<GrowthRateTranslate>

    @GET("stat/{id}")
    suspend fun getStatTranslate(
        @Path("id") id: Int
    ): Response<StatTranslate>

    @GET("pokemon-habitat/{id}")
    suspend fun getHabitat(
        @Path("id") id: Int
    ): Response<HabitatTranslate>

    @GET("pokemon-shape/{id}")
    suspend fun getShape(
        @Path("id") id: Int
    ): Response<ShapeTranslate>

    @GET("ability/{id}")
    suspend fun getAbility(
        @Path("id") id: Int
    ): Response<AbilityTranslate>

    @GET("egg-group/{id}")
    suspend fun getEgg(
        @Path("id") id: Int
    ): Response<EggTranslate>

}