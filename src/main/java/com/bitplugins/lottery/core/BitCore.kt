package com.bitplugins.lottery.core

import com.bitplugins.lottery.configuration.EditItemLotteryConfigManager
import com.bitplugins.lottery.core.network.NetworkClient
import com.bitplugins.lottery.core.network.RequestExecutor.networkCall
import com.bitplugins.lottery.core.network.di.FileType
import com.bitplugins.lottery.core.network.response.onState
import com.bitplugins.lottery.core.util.Logger
import kotlinx.coroutines.runBlocking
import org.bukkit.plugin.java.JavaPlugin

abstract class BitCore : JavaPlugin() {


    companion object {
        lateinit var context: BitCoreContext
            private set
    }


    override fun onEnable() {
        context = BitCoreContext(this)
        Logger.info("🚀 Launching the BitLottery plugin.")
        //testNetwork()
        //testConfig()
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
        configManager.config.items["on-back"]?.name = "&7Voltar Modifcateds"

        // Salvar as alterações
        configManager.save()
        Logger.info("Configuração salva com sucesso!")
    }

    fun testNetwork() = runBlocking {
        val client = NetworkClient()

        networkCall({ client.get("https://api.github.com") }) { response ->
            response.onState(
                onSuccess = { data -> Logger.info("✅ Dados recebidos: $data") },
                onLoading = { Logger.info("Carregando") },
                onFailure = { error -> Logger.error("❌ Erro: $error") }
            )
        }

        networkCall({ client.post("https://httpbin.org/post", "param1=value1") }) { response ->
            response.onState(
                onSuccess = { data -> Logger.info("✅ Resposta POST: $data") },
                onLoading = { Logger.info("Carregando") },
                onFailure = { error -> Logger.error("❌ Erro: $error") }
            )
        }

        networkCall({
            client.download(
                url = "https://www.google.com/logos/doodles/2024/seasonal-holidays-2024-6753651837110333.4-s.png",
                destination = "google",
                fileName = "GoogleImage",
                fileType = FileType.PNG,
            )
        }){ fileResponse ->
            fileResponse.onState(
                onSuccess = { file -> Logger.info("📥 File downloaded in: ${file.absolutePath}") },
                onLoading = { progress -> Logger.warning("Downloaded: $progress%") },
                onFailure = { error -> Logger.error("❌ Erro ao baixar arquivo: $error") }
            )
        }


    }

}