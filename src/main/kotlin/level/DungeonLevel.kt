package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.toTranslatable

class DungeonLevel(game: Game, levelProvider: ILevelProvider) : Level(game, levelProvider) {
    override fun postInit() {
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