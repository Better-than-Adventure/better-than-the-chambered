package com.mojang.escape.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.BoulderEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.BasicSprite

class PitBlock: Block() {
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
            addSprite(BasicSprite(0.0, 0.0, 0.0, 8 + 2, BoulderEntity.COLOR))
            Sound.thud.play()
        }
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is BoulderEntity) {
            return false
        }
        return blocksMotion
    }
}