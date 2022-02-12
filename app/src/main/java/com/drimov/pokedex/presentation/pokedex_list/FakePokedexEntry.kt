package com.drimov.pokedex.presentation.pokedex_list

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.drimov.pokedex.domain.model.PokedexListEntry

class FakePokedexEntry() : PreviewParameterProvider<PokedexListEntry> {


    override val values: Sequence<PokedexListEntry>
        get() {
            val listEntry = sequence<PokedexListEntry> {

                PokedexListEntry(
                    name = "zubat",
                    url = "https://pokeapi.co/api/v2/pokemon-species/41/",
                    id = 41
                )
                PokedexListEntry(
                    name = "oddish",
                    url = "https://pokeapi.co/api/v2/pokemon-species/43/",
                    id = 43
                )
                PokedexListEntry(
                    name = "venonat",
                    url = "https://pokeapi.co/api/v2/pokemon-species/48/",
                    id = 48
                )
                PokedexListEntry(
                    name = "meowth",
                    url = "https://pokeapi.co/api/v2/pokemon-species/52/",
                    id = 52
                )
                PokedexListEntry(
                    name = "mankey",
                    url = "https://pokeapi.co/api/v2/pokemon-species/56/",
                    id = 56
                )
                PokedexListEntry(
                    name = "growlithe",
                    url = "https://pokeapi.co/api/v2/pokemon-species/58/",
                    id = 58
                )
            }
            return listEntry
        }
}