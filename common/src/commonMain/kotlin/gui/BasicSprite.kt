package com.mojang.escape.gui

class BasicSprite(x: Double, y: Double, z: Double, tex: Int, col: Int) : Sprite(x, y, z, tex, col) {
    override fun tick() {
        // Does nothing when ticked
    }
}