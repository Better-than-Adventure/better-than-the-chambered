package com.mojang.escape.gui

/**
 * A sprite that automatically removes itself after 20 ticks.
 */
class PoofSprite(x: Double, y: Double, z: Double, tex: Int, col: Int) : Sprite(x, y, z, tex, col) {
    /**
     * The number of ticks remaining until the sprite removes itself.
     */
    private var life: Int = 20

    override fun tick() {
        super.tick()

        if (life-- <= 0) removed = true
    }
}