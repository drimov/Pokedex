package com.drimov.pokedex.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String?) : Resource<T>(data, message)
}

sealed class ResourceMultiple<T, Y>(
    val dataT: T? = null,
    val dataY: Y? = null,
    val message: String? = null
) {
    class Loading<T, Y>(dataT: T? = null, dataY: Y? = null) : ResourceMultiple<T, Y>(dataT, dataY)
    class Success<T, Y>(dataT: T?, dataY: Y?) : ResourceMultiple<T, Y>(dataT, dataY)
    class Error<T, Y>(dataT: T? = null, dataY: Y? = null, message: String?) :
        ResourceMultiple<T, Y>(dataT, dataY, message)
}