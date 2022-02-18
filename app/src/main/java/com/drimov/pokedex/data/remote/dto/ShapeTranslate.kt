package com.drimov.pokedex.data.remote.dto


data class ShapeTranslate(
    val awesome_names: List<AwesomeName>,
    val id: Int,
    val name: String,
    val names: List<Name>,
    val pokemon_species: List<PokemonSpecy>
)