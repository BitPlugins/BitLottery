package com.bitplugins.lottery.core

import com.bitplugins.lottery.core.config.ConfigManager
import com.bitplugins.lottery.core.util.FileUtils
import com.bitplugins.lottery.core.util.NetworkUtils
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Logger

class BitCoreContext(val plugin: JavaPlugin) {
    val dataFolder: File = plugin.dataFolder

    val fileUtils: FileUtils = FileUtils(dataFolder)
    val networkUtils: NetworkUtils = NetworkUtils()
    val adventure = BukkitAudiences.create(plugin)

    val legacyComponentSerializer: LegacyComponentSerializer by lazy {
        LegacyComponentSerializer.builder()
            .hexColors() // Suporte a cores hexadecimais
            .useUnusualXRepeatedCharacterHexFormat() // Formato de cores hex incomum
            .character(LegacyComponentSerializer.SECTION_CHAR) // Suporte ao caractere '&'
            .character(LegacyComponentSerializer.AMPERSAND_CHAR) // Suporte ao caractere '§'
            .character(LegacyComponentSerializer.HEX_CHAR) // Suporte ao caractere '#'
            .build()
    }
}
