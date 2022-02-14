package com.drimov.pokedex.data.remote.dto

data class MoveX(
    val move: Move,
    val version_group_details: List<VersionGroupDetail>
)