package com.drimov.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Other(
    val dream_world: DreamWorld,
    val home: Home,
    @SerializedName("official-artwork")val officialArtwork: OfficialArtwork
)