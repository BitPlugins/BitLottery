package com.bitplugins.lottery.core.network

import com.bitplugins.lottery.core.BitPlugin
import com.bitplugins.lottery.core.network.response.ResponseWrapper
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

typealias NetworkCall <T> = suspend () -> Flow<ResponseWrapper<T>>
typealias NetworkCallback <T> = (ResponseWrapper<T>) -> Unit

object RequestExecutor {

    inline fun <T> networkCall(
        crossinline call: NetworkCall<T>,
        crossinline callback: NetworkCallback<T>
    ) {
        BitPlugin.context.plugin.launch(Dispatchers.IO) {
            call().collect {
                withContext(BitPlugin.context.plugin.minecraftDispatcher) {
                    callback(it)
                }
            }
        }
    }
}
