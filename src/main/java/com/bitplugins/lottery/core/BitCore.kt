package com.bitplugins.lottery.core

import com.bitplugins.lottery.configuration.EditItemLotteryConfigManager
import com.bitplugins.lottery.core.network.NetworkClient
import com.bitplugins.lottery.core.network.RequestExecutor
import com.bitplugins.lottery.core.network.response.ResponseWrapper
import com.bitplugins.lottery.core.util.ExceptionLogger
import com.bitplugins.lottery.core.util.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

abstract class BitCore : JavaPlugin() {


    companion object {
        lateinit var context: BitCoreContext
            private set
    }


    override fun onEnable() {
        context = BitCoreContext(this)
        Logger.info("🚀 Launching the BitLottery plugin.")
        testNetwork()
        testConfig()
    }

    override fun onDisable() {
        super.onDisable()
    }

    fun testConfig() {
        val configManager = EditItemLotteryConfigManager()
        configManager.load()

        Logger.info("Title: ${configManager.config.title}")
        Logger.info("Pattern: ${configManager.config.pattern}")
        Logger.info("Items: ${configManager.config.items}")

        // Modificar um item
        configManager.config.items["on-back"]?.name = "&7Voltar Modifcated"

        // Salvar as alterações
        configManager.save()
        Logger.info("Configuração salva com sucesso!")
    }

    fun testNetwork() = runBlocking {
        val client = NetworkClient()

        // Chamada GET síncrona
        RequestExecutor.executeAsync( { client.get("https://api.github.com") }) { response ->
            when (response) {
                is ResponseWrapper.Success -> Logger.info("✅ Dados recebidos: ${response.data}")
                is ResponseWrapper.Failure -> Logger.error("❌ Erro: ${response.error}")
            }
        }

        delay(10000)

        // Chamada POST assíncrona
        RequestExecutor.executeAsync({ client.post("https://httpbin.org/post", "param1=value1") }) { response ->
            when (response) {
                is ResponseWrapper.Success -> Logger.info("✅ Resposta POST: ${response.data}")
                is ResponseWrapper.Failure -> Logger.error("❌ Erro: ${response.error}")
            }
        }

        delay(10000)

        // Download de arquivo
        val fileResponse = RequestExecutor.executeSync {
            client.downloadFile("https://example.com/file.txt", "downloaded_file.txt")
        }
        when (fileResponse) {
            is ResponseWrapper.Success -> Logger.info("📥 Arquivo baixado em: ${fileResponse.data.absolutePath}")
            is ResponseWrapper.Failure -> Logger.error("❌ Erro ao baixar arquivo: ${fileResponse.error}")
        }
    }

}