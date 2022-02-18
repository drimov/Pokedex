package com.drimov.pokedex.presentation.pokedex_pokemon

import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonSpecies
import com.drimov.pokedex.domain.model.PokemonData

data class PokedexPokemonInfoState(
//    val pokemon: Pokemon? = null,
    val pokemonData: PokemonData? =null,
    val url: String? = null,
//    val pokemonSpecies: PokemonSpecies? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)