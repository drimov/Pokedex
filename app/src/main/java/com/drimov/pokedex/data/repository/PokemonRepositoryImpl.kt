package com.drimov.pokedex.data.repository

import com.drimov.pokedex.data.remote.PokedexApi
import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonListGeneration
import com.drimov.pokedex.data.remote.dto.PokemonSpecy
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class PokemonRepositoryImpl(
    private val api: PokedexApi
) : PokemonRepository {

    override suspend fun getPokemonList(generation: String): Flow<Resource<PokemonListGeneration>> =
        flow {

            emit(Resource.Loading<PokemonListGeneration>())
            try {
                val result = api.gePokemonList(generation)
                emit(Resource.Success(result))

            } catch (e: HttpException) {
                emit(
                    Resource.Error<PokemonListGeneration>(
                        message = Resource.httpExceptionErr
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error<PokemonListGeneration>(
                        message = Resource.ioExceptionErr
                    )
                )
            }

        }

    override suspend fun getPokemon(name: String): Flow<Resource<Pokemon>> =
        flow {

            emit(Resource.Loading<Pokemon>())
            try {
                val result = api.getPokemon(name)
                emit(Resource.Success(result))

            } catch (e: HttpException) {
                emit(
                    Resource.Error<Pokemon>(
                        message = Resource.httpExceptionErr
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error<Pokemon>(
                        message = Resource.ioExceptionErr
                    )
                )
            }
        }
}