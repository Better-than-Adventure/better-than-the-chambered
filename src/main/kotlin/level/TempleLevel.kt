package com.mojang.escape.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.toTranslatable

class TempleLevel(game: Game, levelProvider: ILevelProvider) : Level(game, levelProvider) {
    private var triggerMask = 0

    override fun switchLevel(id: Int) {
        if (id == 1) {
            game.switchLevel("overworld", 3)
        }
    }

    override fun getLoot(id: Int) {
        super.getLoot(id)
        if (id == 1) {
            game.getLoot(Item.Skates)
        }
    }

    override fun trigger(id: Int, pressed: Boolean) {
        triggerMask = triggerMask or (1 shl id)
        if (!pressed) {
            triggerMask = triggerMask xor (1 shl id)
        }

        if (triggerMask == 14) {
            super.trigger(1, true)
        } else {
            super.trigger(1, false)
        }
    }
}