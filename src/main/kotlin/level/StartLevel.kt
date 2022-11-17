package com.mojang.escape.level

import com.mojang.escape.level.block.Block
import com.mojang.escape.toTranslatable

class StartLevel: Level() {
    init {
        name = "level.start.name".toTranslatable()
    }

    override fun switchLevel(id: Int) {
        when (id) {
            1 -> game.switchLevel("overworld", 1)
            2 -> game.switchLevel("random_0", 1)
        }
    }
}