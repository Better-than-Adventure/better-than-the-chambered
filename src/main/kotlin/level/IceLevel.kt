package com.mojang.escape.level

import com.mojang.escape.entities.Item

class IceLevel: Level() {
    init {
        floorCol = 0xB8DBE0
        ceilCol = 0xB8DBE0
        wallCol = 0x6BE8FF
        name = "The Frost Cave"
    }

    override fun switchLevel(id: Int) {
        if (id == 1) {
            game.switchLevel("overworld", 5)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) {
            game.getLoot(Item.Skates)
        }
    }
}