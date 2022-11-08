package com.mojang.escape.gui

/**
 * Represents a texture being drawn in a physical location in the game world.
 *
 * [x], [y], and [z] mark location.
 *
 * [tex] indicates the texture to use.
 *
 * [col] is the display colour.
 */
open class Sprite(var x: Double, var y: Double, var z: Double, var tex: Int, var col: Int) {
    /**
     * Indicates whether the sprite has been removed from the game world.
     */
    var removed: Boolean = false;

    /**
     * Called every tick to update the sprite.
     */
    open fun tick() {
    }
}