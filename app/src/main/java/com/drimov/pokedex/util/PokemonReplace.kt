package com.drimov.pokedex.util

import com.drimov.pokedex.util.Constants.typeUrlImgP1
import com.drimov.pokedex.util.Constants.typeUrlImgP2
import java.util.*

fun String.ucFirst(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
}

fun String.digit(suffix: String): String {
    return when {
        this.endsWith(suffix) -> this.dropLast(1).takeLastWhile { it.isDigit() }
        else -> this.takeLastWhile { it.isDigit() }
    }
}

fun calWithRef(number: Double, ref: Double): String {
    return String.format(Locale.ROOT, "%.2f", number / ref)
}

fun String.urlType(): String {
    return typeUrlImgP1.plus(this).plus(typeUrlImgP2)
}