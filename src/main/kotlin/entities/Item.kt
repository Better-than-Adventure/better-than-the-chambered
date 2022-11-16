package com.mojang.escape.entities

import com.mojang.escape.translated

/**
 * Represents an item in the game, with a given [icon], [color], [itemName], and [description].
 */
enum class Item(val icon: Int, val color: Int, val itemName: String, val description: String) {
    /**
     * Default item.
     */
    None(-1, 0xFFC363, "item.none.name", "item.none.desc"),

    /**
     * Allows the player to punch boulders and enemies.
     */
    PowerGlove(0, 0xFFC363, "item.powerGlove.name", "item.powerGlove.desc"),

    /**
     * Shoots projectiles.
     */
    Pistol(1, 0xEAEAEA, "item.pistol.name", "item.pistol.desc"),

    /**
     * Lets the player move through water.
     */
    Flippers(2, 0x7CBBFF, "item.flippers.name", "item.flippers.desc"),

    /**
     * Cuts through iron bars.
     */
    Cutters(3, 0xCCCCCC, "item.cutters.name", "item.cutters.desc"),

    /**
     * Lets the player move freely across ice.
     */
    Skates(4, 0xAE70FF, "item.skates.name", "item.skates.desc"),

    /**
     * Unlocks doors. Not obtainable.
     */
    Key(5, 0xFF4040, "item.key.name", "item.key.desc"),

    /**
     * Heals the player.
     */
    Potion(6, 0x4AFF47, "item.potion.name", "item.potion.desc");
}