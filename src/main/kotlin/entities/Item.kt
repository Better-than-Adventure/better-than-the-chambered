package com.mojang.escape.entities

import com.mojang.escape.translated
import com.mojang.escape.util.TranslatedString

/**
 * Represents an item in the game, with a given [icon], [color], [itemName], and [description].
 */
enum class Item(val icon: Int, val color: Int, val itemName: TranslatedString, val description: TranslatedString) {
    /**
     * Default item.
     */
    None(-1, 0xFFC363, "item.none.name".translated, "item.none.desc".translated),

    /**
     * Allows the player to punch boulders and enemies.
     */
    PowerGlove(0, 0xFFC363, "item.powerGlove.name".translated, "item.powerGlove.desc".translated),

    /**
     * Shoots projectiles.
     */
    Pistol(1, 0xEAEAEA, "item.pistol.name".translated, "item.pistol.desc".translated),

    /**
     * Lets the player move through water.
     */
    Flippers(2, 0x7CBBFF, "item.flippers.name".translated, "item.flippers.desc".translated),

    /**
     * Cuts through iron bars.
     */
    Cutters(3, 0xCCCCCC, "item.cutters.name".translated, "item.cutters.desc".translated),

    /**
     * Lets the player move freely across ice.
     */
    Skates(4, 0xAE70FF, "item.skates.name".translated, "item.skates.desc".translated),

    /**
     * Unlocks doors. Not obtainable.
     */
    Key(5, 0xFF4040, "item.key.name".translated, "item.key.desc".translated),

    /**
     * Heals the player.
     */
    Potion(6, 0x4AFF47, "item.potion.name".translated, "item.potion.desc".translated);
}