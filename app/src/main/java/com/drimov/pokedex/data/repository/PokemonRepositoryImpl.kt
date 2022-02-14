package com.drimov.pokedex.data.repository

import com.drimov.pokedex.data.remote.PokedexApi
import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonListGeneration
import com.drimov.pokedex.data.remote.dto.PokemonSpecies
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.util.Constants
import com.drimov.pokedex.util.Resource
import com.drimov.pokedex.util.ResourceMultiple
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
                        message = Constants.httpExceptionErr
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error<PokemonListGeneration>(
                        message = Constants.ioExceptionErr
                    )
                )
            }

        }

    override suspend fun getPokemon(id: Int): Flow<ResourceMultiple<Pokemon, PokemonSpecies>> =
        flow {

            emit(ResourceMultiple.Loading<Pokemon, PokemonSpecies>())
            try {
                val result1 = api.getPokemon(id)
                val result2 = api.getPokemonSpecies(id)

                if (result1.isSuccessful && result2.isSuccessful) {
                    emit(ResourceMultiple.Success(result1.body(), result2.body()))
                }
            } catch (e: HttpException) {
                emit(
                    ResourceMultiple.Error<Pokemon, PokemonSpecies>(
                        message = Constants.httpExceptionErr
                    )
                )
            } catch (e: IOException) {
                emit(
                    ResourceMultiple.Error<Pokemon, PokemonSpecies>(
                        message = Constants.ioExceptionErr
                    )
                )
            }
        }
}