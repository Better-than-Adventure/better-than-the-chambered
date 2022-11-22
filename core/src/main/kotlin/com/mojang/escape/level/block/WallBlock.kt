package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.AABB

open class WallBlock(
    x: Int,
    y: Int,
    val art: Bitmap,
    val tex: Int,
    val col: Int
): Block(
    x = x,
    y = y,
    occludesAdjacentBlocks = true
), ICollidableBlock {
    override val collisionBox: AABB = AABB(0.0, 0.0, 1.0, 1.0)
    
    override fun init(level: Level) {
        // Do nothing
    }

    override fun doRender(bitmap: Bitmap3D, level: Level) {
        val xPos = level.getBlock(x + 1, y + 0)
        val yPos = level.getBlock(x + 0, y + 1)
        val xNeg = level.getBlock(x - 1, y + 0)
        val yNeg = level.getBlock(x + 0, y - 1)
        
        if (!xNeg.occludesAdjacentBlocks) {
            bitmap.renderWall(x + 0.0, y + 0.0, x + 0.0, y + 1.0, tex, (col and 0xFEFEFE) shr 1, art)
        }
        if (!yNeg.occludesAdjacentBlocks) {
            bitmap.renderWall(x + 0.0, y + 0.0, x + 1.0, y + 0.0, tex, col, art)
        }
        if (!xPos.occludesAdjacentBlocks) {
            bitmap.renderWall(x + 1.0, y + 0.0, x + 1.0, y + 1.0, tex, (col and 0xFEFEFE) shr 1, art)
        }
        if (!yPos.occludesAdjacentBlocks) {
            bitmap.renderWall(x + 0.0, y + 1.0, x + 1.0, y + 1.0, tex, col, art)
        }
    }

    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }
}