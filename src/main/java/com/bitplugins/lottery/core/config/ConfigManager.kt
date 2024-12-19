package com.bitplugins.lottery.core.config

import com.bitplugins.lottery.core.BitCore
import com.bitplugins.lottery.core.util.FileUtils
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File

abstract class ConfigManager<T : Any>(
    private val fileName: String,
    private val defaultConfig: T,
    private val defaultResourcePath: String? = null
) {

    private val dataFolder = BitCore.context.dataFolder

    private val fileUtils = FileUtils(dataFolder)

    private val configPath: String = fileName

    private val loader: YamlConfigurationLoader by lazy {
        YamlConfigurationLoader.builder()
            .path(File(dataFolder, configPath).toPath())
            .build()
    }

    abstract val config: T

    init {
        ensureConfigFileExists()
    }

    fun load() {
        val node = try {
            loader.load()
        } catch (e: Exception) {
            e.printStackTrace()
            createDefaultConfig()
            loader.load()
        }

        mapConfig(node)
    }

    fun save() {
        val node = loader.createNode()

        mapToNode(node, config)

        loader.save(node)
    }

    private fun ensureConfigFileExists() {
        if (!fileUtils.exists(configPath)) {
            defaultResourcePath?.let {
                fileUtils.copyDefaultFileIfNotExists(it, configPath)
            }
        }
    }

    private fun createDefaultConfig() {
        val node = loader.createNode()
        mapToNode(node, defaultConfig)
        loader.save(node)
    }

    protected abstract fun mapConfig(node: ConfigurationNode)

    protected abstract fun mapToNode(node: ConfigurationNode, config: T)
}