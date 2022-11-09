package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite

class LadderBlock(down: Boolean): Block() {
    companion object {
        private const val LADDER_COLOR = 0xDB8E53
    }
    var wait: Boolean = false

    init {
        if (down) {
            floorTex = 1
            addSprite(BasicSprite(0.0, 0.0, 0.0, 8 + 3, Art.getCol(LADDER_COLOR)))
        } else {
            ceilTex = 1
            addSprite(BasicSprite(0.0, 0.0, 0.0, 8 + 4, Art.getCol(LADDER_COLOR)))
        }
    }

    override fun removeEntity(entity: Entity) {
        super.removeEntity(entity)
        if (entity is Player) {
            wait = false
        }
    }

    override fun addEntity(entity: Entity) {
        super.addEntity(entity)

        if (!wait && entity is Player) {
            level?.switchLevel(id)
            Sound.ladder.play()
        }
    }
}