package com.bitplugins.lottery.core.builder

import com.bitplugins.lottery.core.BitCore
import com.cryptomorin.xseries.XEnchantment
import com.cryptomorin.xseries.XMaterial
import com.cryptomorin.xseries.XItemStack
import com.cryptomorin.xseries.profiles.builder.XSkull
import com.cryptomorin.xseries.profiles.objects.Profileable
import de.tr7zw.changeme.nbtapi.NBTItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class ItemBuilder(
    private var itemStack: ItemStack
) : Cloneable {

    private val legacyComponent = BitCore.context.legacyComponentSerializer

    constructor(material: Material) : this(XMaterial.matchXMaterial(material).or(XMaterial.STONE).parseItem()!!)
    constructor(material: Material, amount: Int) : this(
        XMaterial.matchXMaterial(material).or(XMaterial.STONE).parseItem()!!.apply { this.amount = amount })

    constructor(material: Material, amount: Int, durability: Short) : this(
        XMaterial.matchXMaterial(material).or(XMaterial.STONE).parseItem()!!.apply {
            this.amount = amount
            this.durability = durability
        })

    constructor(skullUrl: String) : this(XSkull.createItem().profile(Profileable.detect(skullUrl)).apply())

    override fun clone(): ItemBuilder {
        return ItemBuilder(itemStack.clone())
    }

    fun toItemStack(): ItemStack {
        return itemStack
    }

    fun withMaterial(material: Material): ItemBuilder {
        itemStack = XMaterial.matchXMaterial(material).or(XMaterial.STONE).parseItem()!!
        return this
    }

    fun withAmount(amount: Int): ItemBuilder {
        itemStack.amount = amount
        return this
    }

    fun withDurability(durability: Short): ItemBuilder {
        itemStack.durability = durability
        return this
    }

    fun withInfiniteDurability(): ItemBuilder {
        itemStack.durability = 32767
        return this
    }

    fun withName(displayName: String): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta != null) {
            val component = MiniMessage.miniMessage().deserialize(displayName)
            meta.setDisplayName(legacyComponent.serialize(component))
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withEmptyName(): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta != null) {
            meta.setDisplayName("")
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withLore(lines: List<String>): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta != null) {
            val lore = itemStack.itemMeta?.lore ?: mutableListOf()
            val loreComponents = lines.map { MiniMessage.miniMessage().deserialize(it) }
            lore.addAll(loreComponents.map { legacyComponent.serialize(it) })
            meta.lore = lore
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withItemFlags(vararg flags: ItemFlag): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta != null) {
            meta.addItemFlags(*flags)
            itemStack.itemMeta = meta
        }
        return this
    }

    fun removeItemFlags(vararg flags: ItemFlag): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta != null) {
            meta.removeItemFlags(*flags)
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withEnchantment(enchantment: Enchantment, level: Int): ItemBuilder {
        itemStack.addEnchantment(enchantment, level)
        return this
    }

    fun withUnsafeEnchantment(enchantment: Enchantment, level: Int): ItemBuilder {
        itemStack.addUnsafeEnchantment(enchantment, level)
        return this
    }

    fun removeEnchantment(enchantment: Enchantment): ItemBuilder {
        itemStack.removeEnchantment(enchantment)
        return this
    }

    fun withLeatherArmorColor(color: Color): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta is LeatherArmorMeta) {
            meta.setColor(color)
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withPotionType(type: PotionEffectType): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta is PotionMeta) {
            meta.setMainEffect(type)
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withCustomPotionEffect(effect: PotionEffect, overwrite: Boolean): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta is PotionMeta) {
            meta.addCustomEffect(effect, overwrite)
            itemStack.itemMeta = meta
        }
        return this
    }

    fun removeCustomPotionEffect(type: PotionEffectType): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta is PotionMeta) {
            meta.removeCustomEffect(type)
            itemStack.itemMeta = meta
        }
        return this
    }

    fun clearCustomPotionEffects(): ItemBuilder {
        val meta = itemStack.itemMeta
        if (meta is PotionMeta) {
            meta.clearCustomEffects()
            itemStack.itemMeta = meta
        }
        return this
    }

    fun withNBT(key: String, value: String): ItemBuilder {
        val nbtItem = NBTItem(itemStack)
        nbtItem.setString(key, value)
        itemStack = nbtItem.item
        return this
    }

    fun getNBT(key: String): String? {
        val nbtItem = NBTItem(itemStack)
        return nbtItem.getString(key)
    }

    fun removeNBT(key: String): ItemBuilder {
        val nbtItem = NBTItem(itemStack)
        nbtItem.removeKey(key)
        itemStack = nbtItem.item
        return this
    }

    fun build(): ItemStack {
        return itemStack
    }
}