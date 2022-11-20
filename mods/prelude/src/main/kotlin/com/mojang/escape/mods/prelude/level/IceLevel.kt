package com.mojang.escape.mods.prelude.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.mods.prelude.level.provider.PNGLevelProvider

class IceLevel(game: Game, levelProvider: PNGLevelProvider) : PNGLevel(game, levelProvider) {
    override fun switchLevel(id: Int) {
        if (id == 1) {
            switchLevel("overworld", 5)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) {
            game.getLoot(Item.Skates)
        }
    }
}