package com.mojang.escape.mods.prelude.gui

import com.mojang.escape.gui.Sprite

class RubbleSprite: Sprite(Math.random() - 0.5, Math.random() * 0.8, Math.random() - 0.5, 2, 0x555555) {
    private var xa = Math.random() - 0.5
    private var ya = Math.random()
    private var za = Math.random() - 0.5

    override fun tick() {
        x += xa * 0.03
        y += ya * 0.03
        z += za * 0.03
        ya -= 0.1
        if (y < 0) {
            y = 0.0
            xa *= 0.8
            za *= 0.8
            if (Math.random() < 0.04) {
                removed = true
            }
        }
    }
}