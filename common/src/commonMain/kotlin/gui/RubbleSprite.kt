package com.mojang.escape.gui

import kotlin.random.Random

class RubbleSprite: Sprite(Random.nextDouble() - 0.5, Random.nextDouble() * 0.8, Random.nextDouble() - 0.5, 2, 0x555555) {
    private var xa = Random.nextDouble() - 0.5
    private var ya = Random.nextDouble()
    private var za = Random.nextDouble() - 0.5

    override fun tick() {
        x += xa * 0.03
        y += ya * 0.03
        z += za * 0.03
        ya -= 0.1
        if (y < 0) {
            y = 0.0
            xa *= 0.8
            za *= 0.8
            if (Random.nextDouble() < 0.04) {
                removed = true
            }
        }
    }
}