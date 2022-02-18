package com.drimov.pokedex.data.remote.dto


data class HabitatTranslate(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val pokemon_species: List<PokemonSpecy>
)