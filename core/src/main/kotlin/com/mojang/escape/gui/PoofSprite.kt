package com.mojang.escape.gui

import com.mojang.escape.gui.Sprite

/**
 * A sprite that automatically removes itself after 20 ticks.
 */
class PoofSprite(x: Double, y: Double, z: Double) : Sprite(x, y, z, 5, 0x222222) {
    /**
     * The number of ticks remaining until the sprite removes itself.
     */
    private var life: Int = 20

    override fun tick() {
        if (life-- <= 0) removed = true
    }
}