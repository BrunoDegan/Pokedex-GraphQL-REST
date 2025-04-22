package com.brunodegan.pokedex.base.network.base

sealed class ErrorType(val message: String?) {
    data class Generic(val customMessage: String?) : ErrorType(customMessage)
}

sealed class NetworkResponse<T> {
    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Error<T>(val error: ErrorType? = null) : NetworkResponse<T>()
}