package com.bitplugins.lottery.core.network.response

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val data: T) : ResponseWrapper<T>()
    data class Failure(val error: String) : ResponseWrapper<Nothing>()
}