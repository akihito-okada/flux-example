package com.example.flux.model

sealed class Response<T> {
    data class Success<T>(var data: T) : Response<T>()
    data class Failure<T>(val e: Throwable) : Response<T>()
    companion object {
        fun <T> success(data: T): Response<T> = Success(data)

        fun <T> failure(e: Throwable): Response<T> = Failure(e)
    }
}
