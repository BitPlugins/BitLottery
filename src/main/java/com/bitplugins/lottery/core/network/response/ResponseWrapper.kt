package com.bitplugins.lottery.core.network.response

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val data: T) : ResponseWrapper<T>()
    data class Loading(val progress: Int? = null) : ResponseWrapper<Nothing>()
    data class Failure(val error: String) : ResponseWrapper<Nothing>()
}


 fun <T> ResponseWrapper<T>.onState(
    onSuccess: (T) -> Unit,
    onFailure: ((String) -> Unit)? = null,
    onLoading: ((Int?) -> Unit)? = null
) {
    when (this) {
        is ResponseWrapper.Success -> onSuccess(data)
        is ResponseWrapper.Loading -> onLoading?.invoke(progress ?: 0)
        is ResponseWrapper.Failure -> onFailure?.invoke(error)
    }
}