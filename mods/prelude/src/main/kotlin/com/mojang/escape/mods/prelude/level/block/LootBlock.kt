package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt

class LootBlock: Block(ModArt.walls, ModArt.floors) {
    private var taken = false
    private val sprite = BasicSprite(0.0, 0.0, 0.0, 16 + 2, Art.getCol(0xFFFF80), ModArt.sprites)

    init {
        addSprite(sprite)
        blocksMotion = true
    }

    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        if (!taken && entity is Player) {
            sprite.removed = true
            taken = true
            blocksMotion = false
            entity.loot++
            Sound.pickup.play()
        }
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is Player) return false
        return blocksMotion
    }
}