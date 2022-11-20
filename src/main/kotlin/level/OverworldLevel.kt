package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.toTranslatable

class OverworldLevel(game: Game, levelProvider: ILevelProvider) : Level(game, levelProvider) {
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