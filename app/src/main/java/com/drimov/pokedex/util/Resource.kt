package com.drimov.pokedex.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String?) : Resource<T>(data, message)

    companion object {
        const val httpExceptionErr = "Oops, something went wrong!"
        const val ioExceptionErr = "Couldn't reach server, check your internet connection."
    }
}