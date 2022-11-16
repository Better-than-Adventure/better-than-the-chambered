package com.mojang.escape.level

import com.mojang.escape.entities.Item
import com.mojang.escape.translatable

class TempleLevel: Level() {
    private var triggerMask = 0

    init {
        floorCol = 0x8A6496
        ceilCol = 0x8A6496
        wallCol = 0xCFADD8
        name = "level.temple.name".translatable
    }

    override fun switchLevel(id: Int) {
        if (id == 1) {
            game.switchLevel("overworld", 3)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) {
            game.getLoot(Item.Skates)
        }
    }

    override fun trigger(id: Int, pressed: Boolean) {
        triggerMask = triggerMask or (1 shl id)
        if (!pressed) {
            triggerMask = triggerMask xor (1 shl id)
        }

        if (triggerMask == 14) {
            super.trigger(1, true)
        } else {
            super.trigger(1, false)
        }
    }
}