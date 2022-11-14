package com.mojang.escape.entities

/**
 * Represents an item in the game, with a given [icon], [color], [itemName], and [description].
 */
enum class Item(val icon: Int, val color: Int, val itemName: String, val description: String) {
    /**
     * Default item.
     */
    None(       -1, 0xFFC363,   "",             ""),

    /**
     * Allows the player to punch boulders and enemies.
     */
    PowerGlove( 0,  0xFFC363,   "Power Glove",  "Smaaaash!!"),

    /**
     * Shoots projectiles.
     */
    Pistol(     1,  0xEAEAEA,   "Pistol",       "Pew, pew, pew!"),

    /**
     * Lets the player move through water.
     */
    Flippers(   2,  0x7CBBFF,   "Flippers",     "Splish splash!"),

    /**
     * Cuts through iron bars.
     */
    Cutters(    3,  0xCCCCCC,   "Cutters",      "Snip, snip!"),

    /**
     * Lets the player move freely across ice.
     */
    Skates(     4,  0xAE70FF,   "Skates",       "Sharp!"),

    /**
     * Unlocks doors. Not obtainable.
     */
    Key(        5,  0xFF4040,   "Key",          "How did you get this?"),

    /**
     * Heals the player.
     */
    Potion(     6,  0x4aFF47,   "Potion",       "Healthy!");
}