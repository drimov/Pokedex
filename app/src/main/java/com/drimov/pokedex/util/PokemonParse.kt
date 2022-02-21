package com.drimov.pokedex.util

import androidx.compose.ui.graphics.Color
import com.drimov.pokedex.data.remote.dto.StatX
import com.drimov.pokedex.data.remote.dto.Type
import com.drimov.pokedex.presentation.ui.theme.*
import java.util.*


fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.White
    }
}

fun parseStatToColor(stat: StatX): Color {
    return when (stat.name) {
        "hp", "defense", "speed" -> Blue700
        else -> Color.White
    }
}

fun parseNumberToIndex(number: Int): String {
    return when (number) {
        in 0..9 -> "#00${number}"
        in 10..99 -> "#0${number}"
        else -> "#$number"
    }
}

fun parseNbToRomanNb(number: Int): String {
    return when (number) {
        in 1..3 -> "I".repeat(number)
        4 -> "IV"
        in 5..8 -> "V".plus("I".repeat(number - 5))
        else -> "Error"
    }
}

fun parseGeneration(gen: String): String {
    val genR = "generation-"
    val length = gen.length

    return when (gen.subSequence(startIndex = 0, endIndex = 1)) {
        "I" -> when (length) {
            in 1..3 -> genR.plus("i".repeat(length))
            else -> genR.plus("i".repeat(length - 1).plus("v"))
        }
        "V" -> genR.plus("v".plus("i".repeat(length - 1)))
        else -> "Error"
    }
}