package com.mojang.escape.level

import com.mojang.escape.level.block.Block

class StartLevel: Level() {
    init {
        name = "The Prison"
    }

    override fun switchLevel(id: Int) {
        when (id) {
            1 -> game.switchLevel("overworld", 1)
            2 -> game.switchLevel("dungeon", 1)
        }
    }
}