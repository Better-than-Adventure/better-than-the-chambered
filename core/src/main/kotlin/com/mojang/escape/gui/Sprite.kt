package com.mojang.escape.gui

import com.mojang.escape.Art

/**
 * Represents a texture being drawn in a physical location in the game world.
 *
 * [x], [y], and [z] mark location.
 *
 * [tex] indicates the texture to use.
 *
 * [col] is the display colour.
 * 
 * [art] is the bitmap to use.
 */
abstract class Sprite(var x: Double, var y: Double, var z: Double, var tex: Int, var col: Int, var art: Bitmap) {
    /**
     * Indicates whether the sprite has been removed from the game world.
     */
    var removed: Boolean = false;

    /**
     * Called every tick to update the sprite.
     */
    abstract fun tick();
}