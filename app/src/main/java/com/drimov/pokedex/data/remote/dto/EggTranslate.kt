package com.drimov.pokedex.data.remote.dto

data class EggTranslate(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val pokemon_species: List<PokemonSpecy>
)