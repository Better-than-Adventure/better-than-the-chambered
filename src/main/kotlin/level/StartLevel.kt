package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.level.provider.PNGLevelProvider
import com.mojang.escape.toTranslatable

class StartLevel(game: Game, levelProvider: PNGLevelProvider) : PNGLevel(game, levelProvider) {
    override fun switchLevel(id: Int) {
        when (id) {
            1 -> switchLevel("overworld", 1)
            2 -> switchLevel("dungeon", 1)
        }
    }
}