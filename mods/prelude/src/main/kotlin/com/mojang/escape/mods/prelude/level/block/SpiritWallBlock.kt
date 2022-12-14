package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.level.block.Block
import com.mojang.escape.mods.prelude.ModArt

class SpiritWallBlock: Block(ModArt.walls, ModArt.floors) {
    init {
        floorTex = 7
        ceilTex = 7
        blocksMotion = true
        for (i in 0 until 6) {
            val x = (random.nextDouble() - 0.5)
            val y = (random.nextDouble() - 0.7) * 0.3
            val z = (random.nextDouble() - 0.5)
            addSprite(BasicSprite(x, y, z, 4 * 8 + 6 + random.nextInt(2), Art.getCol(0x202020), ModArt.sprites))
        }
    }

    override fun blocks(entity: Entity): Boolean {
        if (entity is Bullet) return false
        return super.blocks(entity)
    }
}