package com.drimov.pokedex.data

import androidx.room.RoomDatabase

abstract class PokedexDatabase: RoomDatabase() {

    companion object{
        const val DB_NAME = "Pokedex_db"
    }
}