package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.toTranslatable
class IceLevel(game: Game, levelProvider: ILevelProvider) : Level(game, levelProvider) {
    override fun switchLevel(id: Int) {
        if (id == 1) {
            game.switchLevel("overworld", 5)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) {
            game.getLoot(Item.Skates)
        }
    }
}