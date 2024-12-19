package com.bitplugins.lottery.configuration

import com.bitplugins.lottery.core.config.ConfigManager
import com.bitplugins.lottery.model.EditItemLotteryConfig
import com.bitplugins.lottery.model.ItemConfig
import eu.decentsoftware.holograms.api.utils.items.ItemBuilder
import org.spongepowered.configurate.ConfigurationNode

class EditItemLotteryConfigManager : ConfigManager<EditItemLotteryConfig>(
    fileName = "edit_item_lottery.yml",
    defaultConfig = EditItemLotteryConfig(),
    defaultResourcePath = "interface/edit_lottery.yml"
) {

    override val config: EditItemLotteryConfig = EditItemLotteryConfig()

    override fun mapConfig(node: ConfigurationNode) {
        config.title = node.node("title").getString(config.title)
        config.pattern = node.node("pattern").getList(String::class.java) ?: config.pattern
        config.items = node.node("items").childrenMap().map { (key, value) ->
            val itemNode = value as ConfigurationNode
            key.toString() to ItemConfig(
                code = itemNode.node("code").string ?: "",
                action = itemNode.node("action").string ?: "",
                name = itemNode.node("name").string ?: "",
                lore = itemNode.node("lore").getList(String::class.java) ?: emptyList(),
                material = itemNode.node("material").string ?: "",
                glow = itemNode.node("glow").boolean
            )
        }.toMap()

    }

    override fun mapToNode(node: ConfigurationNode, config: EditItemLotteryConfig) {
        node.node("title").set(config.title)
        node.node("pattern").setList(String::class.java, config.pattern)
        config.items.forEach { (key, item) ->
            val itemNode = node.node("items", key)
            itemNode.node("code").set(item.code)
            itemNode.node("action").set(item.action)
            itemNode.node("name").set(item.name)
            itemNode.node("lore").setList(String::class.java, item.lore)
            itemNode.node("material").set(item.material)
            itemNode.node("glow").set(item.glow)
        }
    }
}