package com.drimov.pokedex.presentation.pokedex_list

sealed class PokedexListEvent {
    data class OnPokemonClick(val id: Int,val language: String) : PokedexListEvent()
    data class OnGenerationClick(val gen: String) : PokedexListEvent()
    data class OnReload(val gen: String?) : PokedexListEvent()
    data class OnLanguageClick(val language: String) : PokedexListEvent()
}