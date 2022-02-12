package com.drimov.pokedex.di

import com.drimov.pokedex.data.remote.PokedexApi
import com.drimov.pokedex.data.repository.PokemonRepositoryImpl
import com.drimov.pokedex.domain.repository.PokemonRepository
import com.drimov.pokedex.domain.use_case.GetPokemon
import com.drimov.pokedex.domain.use_case.GetPokemonList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokemonModule {

    @Provides
    @Singleton
    fun providePokemonRepository(api: PokedexApi): PokemonRepository {
        return PokemonRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetPokemonUseCase(repository: PokemonRepository): GetPokemon {
        return GetPokemon(repository)
    }

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(repository: PokemonRepository): GetPokemonList {
        return GetPokemonList(repository)
    }
}