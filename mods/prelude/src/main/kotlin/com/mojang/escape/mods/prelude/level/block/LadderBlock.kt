package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class LadderBlock(down: Boolean): Block(ModArt.walls, ModArt.floors) {
    companion object {
        private const val LADDER_COLOR = 0xDB8E53
    }
    var wait: Boolean = false

    init {
        if (down) {
            floorTex = 1
            addSprite(BasicSprite(0.0, 0.0, 0.0, 8 + 3, Art.getCol(LADDER_COLOR), ModArt.sprites))
        } else {
            ceilTex = 1
            addSprite(BasicSprite(0.0, 0.0, 0.0, 8 + 4, Art.getCol(LADDER_COLOR), ModArt.sprites))
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
            ModSound.ladder.play()
        }
    }
}