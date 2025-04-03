package com.brunodegan.pokedex.base.errors

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

fun <T> Flow<T>.customErrorHandler(): Flow<Result<T>> = transform { value ->
    try {
        emit(Result.success(value))
    } catch (e: Exception) {
        emit(Result.failure(e))
    }
}