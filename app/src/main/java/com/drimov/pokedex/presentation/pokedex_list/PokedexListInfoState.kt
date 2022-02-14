package com.drimov.pokedex.presentation.pokedex_list

import com.drimov.pokedex.domain.model.PokedexListEntry

data class PokedexListInfoState(
    val pokemonItem: List<PokedexListEntry> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)