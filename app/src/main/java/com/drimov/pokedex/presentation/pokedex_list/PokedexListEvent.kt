package com.drimov.pokedex.presentation.pokedex_list

sealed class PokedexListEvent {
    data class OnPokemonClick(val name: String,val url: String): PokedexListEvent()
    data class OnGenerationClick(val gen: String,val number: Int): PokedexListEvent()
}