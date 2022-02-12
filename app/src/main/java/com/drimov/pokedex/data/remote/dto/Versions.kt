package com.drimov.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Versions(
    @SerializedName("generation-i")val generationI: GenerationI,
    @SerializedName("generation-ii")val generationIi: GenerationIi,
    @SerializedName("generation-iii")val generationIii: GenerationIii,
    @SerializedName("generation-iv")val generationIV: GenerationIv,
    @SerializedName("generation-v")val generationV: GenerationV,
    @SerializedName("generation-vi")val generationVI: GenerationVi,
    @SerializedName("generation-vii")val generationVIi: GenerationVii,
    @SerializedName("generation-viii")val generationVIii: GenerationViii
)