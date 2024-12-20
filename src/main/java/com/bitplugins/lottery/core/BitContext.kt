package com.bitplugins.lottery.core

import com.bitplugins.lottery.core.config.ConfigManager
import com.bitplugins.lottery.core.util.FileUtils
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.logging.Logger

class BitCoreContext(val plugin: JavaPlugin) {
    val dataFolder: File = plugin.dataFolder

    val fileUtils: FileUtils = FileUtils(dataFolder)
    val adventure = BukkitAudiences.create(plugin)

    val legacyComponentSerializer: LegacyComponentSerializer by lazy {
        LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .character(LegacyComponentSerializer.SECTION_CHAR)
            .character(LegacyComponentSerializer.AMPERSAND_CHAR)
            .character(LegacyComponentSerializer.HEX_CHAR)
            .build()
    }
}
