package com.drimov.pokedex.data.remote.dto

data class GrowthRateTranslate(
    val descriptions: List<Description>,
    val formula: String,
    val id: Int,
    val levels: List<Level>,
    val name: String,
    val pokemon_species: List<PokemonSpecy>
)