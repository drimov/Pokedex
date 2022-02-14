package com.drimov.pokedex.util

import androidx.compose.ui.graphics.Color
import com.drimov.pokedex.presentation.ui.theme.*

object Constants {
    const val BASE_URL = "https://pokeapi.co/api/v2/"
    const val httpExceptionErr = "Oops, something went wrong!"
    const val ioExceptionErr = "Couldn't reach server, check your internet connection."

    enum class MultiFabState {
        COLLAPSED, EXPANDED
    }

    val genList = listOf<String>(
        "Generation I",
        "Generation II",
        "Generation III",
        "Generation IV",
        "Generation V",
        "Generation VI",
        "Generation VII",
        "Generation VIII",
    )
    val listColor: List<Color> = listOf(
        TypeNormal,
        TypeFire,
        TypeWater,
        TypeElectric,
        TypeGrass,
        TypeIce,
        TypeFighting,
        TypePoison,
        TypeGround,
        TypeFlying,
        TypePsychic,
        TypeBug,
        TypeRock,
        TypeGhost,
        TypeDragon,
        TypeDark,
        TypeSteel,
        TypeFairy
    )
    val listTypes: List<String> = listOf(
        "TypeNormal",
        "TypeFire",
        "TypeWater",
        "TypeElectric",
        "TypeGrass",
        "TypeIce",
        "TypePoison",
        "TypeGround",
        "TypeFighting",
        "TypeFlying",
        "TypePsychic",
        "TypeBug",
        "TypeRock",
        "TypeGhost",
        "TypeDragon",
        "TypeDark",
        "TypeSteel",
        "TypeFairy"
    )

}