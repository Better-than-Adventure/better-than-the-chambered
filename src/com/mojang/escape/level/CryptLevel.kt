package com.mojang.escape.level

import com.mojang.escape.entities.Item

class CryptLevel: Level() {
    init {
        floorCol = 0x404040
        ceilCol = 0x404040
        wallCol = 0x404040
        name = "The Crypt"
    }

    override fun switchLevel(id: Int) {
        if (id == 1) {
            game.switchLevel("overworld", 2)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) {
            game.getLoot(Item.Flippers)
        }
    }
}