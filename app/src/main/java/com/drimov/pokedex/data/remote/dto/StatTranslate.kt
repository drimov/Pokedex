package com.drimov.pokedex.data.remote.dto


data class StatTranslate(
    val affecting_moves: AffectingMoves,
    val affecting_natures: AffectingNatures,
    val characteristics: List<Characteristic>,
    val game_index: Int,
    val id: Int,
    val is_battle_only: Boolean,
    val move_damage_class: Any,
    val name: String,
    val names: List<Name>
)