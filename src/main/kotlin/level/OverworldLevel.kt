package com.mojang.escape.level

import com.mojang.escape.entities.Item
import com.mojang.escape.toTranslatable

class OverworldLevel: Level() {
    init {
        ceilTex = -1
        floorCol = 0x508253
        floorTex = 8 + 3
        wallCol = 0xa0a0a0
        name = "level.island.name".toTranslatable()
    }

    override fun switchLevel(id: Int) {
        when (id) {
            1 -> game.switchLevel("start", 1)
            2 -> game.switchLevel("crypt", 1)
            3 -> game.switchLevel("temple", 1)
            5 -> game.switchLevel("ice", 1)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) game.getLoot(Item.Cutters)
    }
}