package com.bitplugins.lottery.core.config

import com.bitplugins.lottery.core.BitCore
import com.bitplugins.lottery.core.util.FileUtils
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File

abstract class ConfigManager<T : Any>(
    private val fileName: String, // Nome do arquivo .yml
    private val defaultConfig: T, // Configuração padrão
    private val defaultResourcePath: String? = null // Caminho do arquivo padrão no diretório resources
) {

    // Diretório base do plugin
    private val dataFolder = BitCore.context.dataFolder

    // Instância de FileUtils
    private val fileUtils = FileUtils(dataFolder)

    // Caminho do arquivo .yml
    private val configPath: String = fileName

    // Loader para o arquivo .yml
    private val loader: YamlConfigurationLoader by lazy {
        YamlConfigurationLoader.builder()
            .path(File(dataFolder, configPath).toPath())
            .build()
    }

    // Propriedades que espelham o arquivo .yml
    abstract val config: T

    init {
        ensureConfigFileExists()
    }

    // Carrega a configuração do arquivo .yml
    fun load() {
        val node = try {
            loader.load()
        } catch (e: Exception) {
            e.printStackTrace()
            createDefaultConfig()
            loader.load()
        }

        // Mapeia os dados do arquivo .yml para a configuração
        mapConfig(node)
    }

    // Salva a configuração no arquivo .yml
    fun save() {
        val node = loader.createNode()

        // Mapeia as propriedades da configuração para o arquivo .yml
        mapToNode(node, config)

        loader.save(node)
    }

    // Garante que o arquivo .yml exista, copiando o arquivo padrão caso necessário
    private fun ensureConfigFileExists() {
        if (!fileUtils.exists(configPath)) {
            defaultResourcePath?.let {
                fileUtils.copyDefaultFileIfNotExists(it, configPath)
            }
        }
    }

    // Cria o arquivo .yml com os valores padrão
    private fun createDefaultConfig() {
        val node = loader.createNode()
        mapToNode(node, defaultConfig)
        loader.save(node)
    }

    // Método abstrato para mapear os dados do arquivo .yml para a configuração
    protected abstract fun mapConfig(node: ConfigurationNode)

    // Método abstrato para mapear as propriedades da configuração para o arquivo .yml
    protected abstract fun mapToNode(node: ConfigurationNode, config: T)
}