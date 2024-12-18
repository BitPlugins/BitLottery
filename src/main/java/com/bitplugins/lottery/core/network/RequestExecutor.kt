package com.bitplugins.lottery.core.network

import com.bitplugins.lottery.core.BitCore
import com.bitplugins.lottery.core.network.response.ResponseWrapper
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import kotlinx.coroutines.*

object RequestExecutor {

    /**
     * Executa uma chamada de rede de forma síncrona.
     */
    fun <T> executeSync(call: () -> ResponseWrapper<T>): ResponseWrapper<T> {
        return call()
    }

    /**
     * Executa uma chamada de rede de forma assíncrona.
     */
    fun <T> executeAsync(call: () -> ResponseWrapper<T>, callback: (ResponseWrapper<T>) -> Unit) {
        BitCore.context.plugin.launch(Dispatchers.IO){
            val response = call()
            withContext(BitCore.context.plugin.minecraftDispatcher){
                callback(response)
            }
        }
    }
}
