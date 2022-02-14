package com.drimov.pokedex.data.remote.dto

data class PokemonListGeneration(
    val abilities: List<AbilityX>,
    val id: Int,
    val main_region: MainRegion,
    val moves: List<Move>,
    val name: String,
    val names: List<Name>,
    val pokemon_species: List<PokemonSpecy>,
    val types: List<Types>,
    val version_groups: List<VersionGroup>
)