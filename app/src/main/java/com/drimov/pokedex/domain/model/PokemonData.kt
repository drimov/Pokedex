package com.drimov.pokedex.domain.model

import com.drimov.pokedex.data.remote.dto.*

data class PokemonData(
    val pokemon: Pokemon,
    val pokemonSpecies: PokemonSpecies,
    val growthRate: GrowthRateTranslate,
    val habitat: HabitatTranslate?,
    val shape: ShapeTranslate?,
    val abilities: List<AbilityTranslate>?,
    val egg: List<EggTranslate>?,
    val stats : List<StatTranslate>
)