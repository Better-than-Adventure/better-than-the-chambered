package com.mojang.escape.entities

import com.mojang.escape.lang.StringUnitTranslatable
import com.mojang.escape.toTranslatable

/**
 * Represents an item in the game, with a given [icon], [color], [itemName], and [description].
 */
enum class Item(val icon: Int, val color: Int, val itemName: StringUnitTranslatable, val description: StringUnitTranslatable) {
    /**
     * Default item.
     */
    None(-1, 0xFFC363, "item.none.name".toTranslatable(), "item.none.desc".toTranslatable()),

    /**
     * Allows the player to punch boulders and enemies.
     */
    PowerGlove(0, 0xFFC363, "item.powerGlove.name".toTranslatable(), "item.powerGlove.desc".toTranslatable()),

    /**
     * Shoots projectiles.
     */
    Pistol(1, 0xEAEAEA, "item.pistol.name".toTranslatable(), "item.pistol.desc".toTranslatable()),

    /**
     * Lets the player move through water.
     */
    Flippers(2, 0x7CBBFF, "item.flippers.name".toTranslatable(), "item.flippers.desc".toTranslatable()),

    /**
     * Cuts through iron bars.
     */
    Cutters(3, 0xCCCCCC, "item.cutters.name".toTranslatable(), "item.cutters.desc".toTranslatable()),

    /**
     * Lets the player move freely across ice.
     */
    Skates(4, 0xAE70FF, "item.skates.name".toTranslatable(), "item.skates.desc".toTranslatable()),

    /**
     * Unlocks doors. Not obtainable.
     */
    Key(5, 0xFF4040, "item.key.name".toTranslatable(), "item.key.desc".toTranslatable()),

    /**
     * Heals the player.
     */
    Potion(6, 0x4AFF47, "item.potion.name".toTranslatable(), "item.potion.desc".toTranslatable());
}