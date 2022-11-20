package com.mojang.escape.mods.prelude.level

import com.mojang.escape.Game
import com.mojang.escape.mods.prelude.level.provider.PNGLevelProvider

class StartLevel(game: Game, levelProvider: PNGLevelProvider) : PNGLevel(game, levelProvider) {
    override fun switchLevel(id: Int) {
        when (id) {
            1 -> switchLevel("overworld", 1)
            2 -> switchLevel("dungeon", 1)
        }
    }
}