package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.level.provider.PNGLevelProvider
import com.mojang.escape.toTranslatable

class OverworldLevel(game: Game, levelProvider: PNGLevelProvider) : PNGLevel(game, levelProvider) {
    override fun switchLevel(id: Int) {
        when (id) {
            1 -> switchLevel("start", 1)
            2 -> switchLevel("crypt", 1)
            3 -> switchLevel("temple", 1)
            5 -> switchLevel("ice", 1)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) game.getLoot(Item.Cutters)
    }
}