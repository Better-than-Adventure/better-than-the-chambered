package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item

class DungeonLevel: Level() {
    init {
        wallCol = 0xC64954
        floorCol = 0x8E4A51
        ceilCol = 0x8E4A51
        name = "The Dungeons"
    }

    override fun init(game: Game, name: String, w: Int, h: Int, pixels: IntArray) {
        super.init(game, name, w, h, pixels)
        super.trigger(6, true)
        super.trigger(7, true)
    }

    override fun switchLevel(id: Int) {
        if (id == 1) {
            game.switchLevel("start", 2)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) game.getLoot(Item.PowerGlove)
    }

    override fun trigger(id: Int, pressed: Boolean) {
        super.trigger(id, pressed)
        if (id == 5) {
            super.trigger(6, !pressed)
        }
        if (id == 4) {
            super.trigger(7, !pressed)
        }
    }
}