package com.drimov.pokedex.data.repository

import android.util.Log
import com.drimov.pokedex.data.remote.PokedexApi
import com.drimov.pokedex.data.remote.dto.AbilityTranslate
import com.drimov.pokedex.data.remote.dto.EggTranslate
import com.drimov.pokedex.data.remote.dto.PokemonListGeneration
import com.drimov.pokedex.data.remote.dto.StatTranslate
import com.drimov.pokedex.domain.model.PokemonData
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.util.Constants
import com.drimov.pokedex.util.Resource
import com.drimov.pokedex.util.digit
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class PokemonRepositoryImpl(
    private val api: PokedexApi
) : PokemonRepository {

    override suspend fun getPokemonList(
        generation: String,
        language: String
    ): Flow<Resource<PokemonListGeneration>> =
        flow {

            emit(Resource.Loading<PokemonListGeneration>())
            try {
                val result = api.gePokemonList(generation)
//                result.pokemon_species.forEachIndexed {index, pokemonSpecy ->
//                    coroutineScope {
//                        val newResult = api.getPokemonSpecies(pokemonSpecy.url.digit("/").toInt())
//                        if (newResult.isSuccessful) {
//                            newResult.body()?.names?.forEach { name ->
//                                if (name.language.name == language) {
//                                    result.pokemon_species[index].name = name.name
//                                }
//                            }
//                        }
//                    }
//                }

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

    override suspend fun getPokemon(id: Int): Flow<Resource<PokemonData>> =
        flow {

            emit(Resource.Loading<PokemonData>())
            try {
                val pokemon = api.getPokemon(id)
                var requestEgg = false
                var requestAbilityDesc = false
                val pokemonSpecies = api.getPokemonSpecies(id)

                // Success needed
                if (pokemon.isSuccessful && pokemonSpecies.isSuccessful) {
                    var requestStats = false
                    var stats: List<StatTranslate>? = null
                    coroutineScope {
                        val result = pokemon.body()!!.stats.map {
                            async {
                                api.getStatTranslate(it.stat.url.digit("/").toInt())
                            }
                        }.awaitAll()
                        stats = result.map {
                            it.body()!!
                        }
                        requestStats = true
                    }


                    val growthRateTrl = api.getGrowthRate(
                        pokemonSpecies.body()!!.growth_rate.url.digit("/").toInt()
                    )
                    val habitatTrl = pokemonSpecies.body()!!.habitat?.url?.digit("/")?.toInt()
                        ?.let { api.getHabitat(it) }
                    val shapeTrl = pokemonSpecies.body()!!.shape?.url?.digit("/")?.toInt()
                        ?.let { api.getShape(it) }

                    var abilityTrl: List<AbilityTranslate>? = null
                    coroutineScope {
                        val result = pokemon.body()!!.abilities.map {
                            async {
                                api.getAbility(it.ability.url.digit("/").toInt())
                            }
                        }.awaitAll()
                        abilityTrl = result.map {
                            it.body()!!
                        }
                        requestAbilityDesc = true
                    }

                    var eggs: List<EggTranslate>? = null
                    coroutineScope {
                        val result = pokemonSpecies.body()!!.egg_groups.map {
                            async {
                                api.getEgg(it.url.digit("/").toInt())
                            }
                        }.awaitAll()
                        eggs = result.map {
                            it.body()!!
                        }
                        requestEgg = true
                    }
//                    Log.d("growthRate", "${growthRateTrl.isSuccessful}")
//                    Log.d("Shape", "${shapeTrl?.isSuccessful}")
//                    Log.d("habitat", "${habitatTrl?.isSuccessful}")
//                    Log.d("abs", "$requestAbilityDesc")
//                    Log.d("egg", "$requestEgg")
                    if (growthRateTrl.isSuccessful && requestAbilityDesc && requestEgg && requestStats) {
                        val pokemonData =
                            PokemonData(
                                pokemon = pokemon.body()!!,
                                pokemonSpecies = pokemonSpecies.body()!!,
                                growthRate = growthRateTrl.body()!!,
                                habitat = habitatTrl?.body(),
                                shape = shapeTrl?.body(),
                                abilities = abilityTrl,
                                egg = eggs,
                                stats = stats!!
                            )
//                        Log.d("pokemonData", "is create")
                        emit(Resource.Success(pokemonData))
                    }
                }
            } catch (e: HttpException) {
                emit(
                    Resource.Error<PokemonData>(
                        message = Constants.httpExceptionErr
                    )
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error<PokemonData>(
                        message = Constants.ioExceptionErr
                    )
                )
            }
        }
}