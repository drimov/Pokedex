package com.drimov.pokedex.presentation.pokedex_pokemon

import com.drimov.pokedex.data.remote.dto.Pokemon

data class PokedexPokemonInfoState(
    val pokemon: Pokemon? = null,
    val url: String? = null,
    val isLoading: Boolean = false
)