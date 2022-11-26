package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt

class SpiritWallBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): EmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
), ICollidableBlock {
    private val sprites = Array<Sprite>(7) {
        val x = (random.nextDouble() - 0.5)
        val y = (random.nextDouble() - 0.7) * 0.3
        val z = (random.nextDouble() - 0.5)
        BasicSprite(x, y, z, 4 * 8 + 6 + random.nextInt(2), Art.getCol(0x202020), ModArt.sprites)
    }

    override val collisionBox = RelativeAABB(0.5)

    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return entity !is Bullet
    }

    override fun doRender(level: Level, bitmap: Bitmap3D) {
        super.doRender(level, bitmap)
        for (sprite in sprites) {
            bitmap.renderSprite(sprite.x + pos.x + 0.5, sprite.y, sprite.z + pos.z + 0.5, sprite.tex, sprite.col, sprite.art)
        }
    }
}