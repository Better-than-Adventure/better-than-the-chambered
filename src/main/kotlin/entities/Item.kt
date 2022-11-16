package com.mojang.escape.entities

import com.mojang.escape.translatable
import com.mojang.escape.lang.Translatable

/**
 * Represents an item in the game, with a given [icon], [color], [itemName], and [description].
 */
enum class Item(val icon: Int, val color: Int, val itemName: Translatable, val description: Translatable) {
    /**
     * Default item.
     */
    None(-1, 0xFFC363, "item.none.name".translatable, "item.none.desc".translatable),

    /**
     * Allows the player to punch boulders and enemies.
     */
    PowerGlove(0, 0xFFC363, "item.powerGlove.name".translatable, "item.powerGlove.desc".translatable),

    /**
     * Shoots projectiles.
     */
    Pistol(1, 0xEAEAEA, "item.pistol.name".translatable, "item.pistol.desc".translatable),

    /**
     * Lets the player move through water.
     */
    Flippers(2, 0x7CBBFF, "item.flippers.name".translatable, "item.flippers.desc".translatable),

    /**
     * Cuts through iron bars.
     */
    Cutters(3, 0xCCCCCC, "item.cutters.name".translatable, "item.cutters.desc".translatable),

    /**
     * Lets the player move freely across ice.
     */
    Skates(4, 0xAE70FF, "item.skates.name".translatable, "item.skates.desc".translatable),

    /**
     * Unlocks doors. Not obtainable.
     */
    Key(5, 0xFF4040, "item.key.name".translatable, "item.key.desc".translatable),

    /**
     * Heals the player.
     */
    Potion(6, 0x4AFF47, "item.potion.name".translatable, "item.potion.desc".translatable);
}