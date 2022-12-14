package com.mojang.escape.mods.prelude.level

import com.mojang.escape.Game
import com.mojang.escape.entities.Item
import com.mojang.escape.mods.prelude.level.provider.PNGLevelProvider

class TempleLevel(game: Game, levelProvider: PNGLevelProvider) : PNGLevel(game, levelProvider) {
    private var triggerMask = 0

    override fun switchLevel(id: Int) {
        if (id == 1) {
            switchLevel("overworld", 3)
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