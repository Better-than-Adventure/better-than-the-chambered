package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.mods.prelude.entities.BoulderEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class PitBlock: Block(ModArt.walls, ModArt.floors) {
    private var filled = false

    init {
        floorTex = 1
        blocksMotion = true
    }

    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        if (!filled && entity is BoulderEntity) {
            entity.remove()
            filled = true
            blocksMotion = false
            addSprite(BasicSprite(0.0, 0.0, 0.0, 8 + 2, BoulderEntity.COLOR, ModArt.sprites))
            ModSound.thud.play()
        }
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is BoulderEntity) {
            return false
        }
        return blocksMotion
    }
}