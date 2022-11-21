package com.mojang.escape.gui

import com.mojang.escape.Art

class BasicSprite(x: Double, y: Double, z: Double, tex: Int, col: Int, art: Bitmap) : Sprite(x, y, z, tex, col, art) {
    override fun tick() {
        // Does nothing when ticked
    }
}