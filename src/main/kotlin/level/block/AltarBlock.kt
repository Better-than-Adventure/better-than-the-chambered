package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.GhostBossEntity
import com.mojang.escape.entities.GhostEntity
import com.mojang.escape.entities.KeyEntity
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.RubbleSprite
import com.mojang.escape.gui.Sprite

class AltarBlock: Block() {
    private var filled = false
    private val sprite: Sprite = BasicSprite(0.0, 0.0, 0.0, 16 + 4, Art.getCol(0xE2FFE4))

    init {
        blocksMotion = true
        addSprite(sprite)
    }

    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        if (!filled && (entity is GhostEntity || entity is GhostBossEntity)) {
            entity.remove()
            filled = true
            blocksMotion = false
            sprite.removed = true

            for (i in 0 until 8) {
                val sprite = RubbleSprite()
                sprite.col = this.sprite.col
                addSprite(sprite)
            }

            if (entity is GhostBossEntity) {
                level?.addEntity(KeyEntity(x.toDouble(), y.toDouble()))
                Sound.bosskill.play()
            } else {
                Sound.altar.play()
            }
        }
    }
}