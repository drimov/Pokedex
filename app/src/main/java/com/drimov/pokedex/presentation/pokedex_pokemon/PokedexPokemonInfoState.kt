package com.drimov.pokedex.presentation.pokedex_pokemon

import com.drimov.pokedex.data.remote.dto.Pokemon
import com.drimov.pokedex.data.remote.dto.PokemonSpecies

data class PokedexPokemonInfoState(
    val pokemon: Pokemon? = null,
    val url: String? = null,
    val pokemonSpecies: PokemonSpecies? = null,
    val isLoading: Boolean = false
)