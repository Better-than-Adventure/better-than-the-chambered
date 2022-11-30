package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB

open class WallBlock(
    pos: Point2I,
    var art: Bitmap,
    var tex: Int,
    var col: Int
): Block(
    pos = pos,
    occludesAdjacentBlocks = true
), ICollidableBlock {
    override val collisionBox: RelativeAABB = RelativeAABB(0.0, 0.0, 1.0, 1.0)
    
    override fun onInit(level: Level) {
        // Do nothing
    }

    override fun onDeinit(level: Level) {
        // Do nothing
    }

    override fun doRender(level: Level, bitmap: Bitmap3D) {
        val xPos = level[pos.x + 1, pos.z + 0]
        val zPos = level[pos.x + 0, pos.z + 1]
        val xNeg = level[pos.x - 1, pos.z + 0]
        val zNeg = level[pos.x + 0, pos.z - 1]
        
        if (xNeg != null && !xNeg.occludesAdjacentBlocks) {
            bitmap.renderWall(pos.x + 0.0, pos.z + 0.0, pos.x + 0.0, pos.z + 1.0, tex, (col and 0xFEFEFE) shr 1, art)
        }
        if (zNeg != null && !zNeg.occludesAdjacentBlocks) {
            bitmap.renderWall(pos.x + 1.0, pos.z + 0.0, pos.x + 0.0, pos.z + 0.0, tex, col, art)
        }
        if (xPos != null && !xPos.occludesAdjacentBlocks) {
            bitmap.renderWall(pos.x + 1.0, pos.z + 1.0, pos.x + 1.0, pos.z + 0.0, tex, (col and 0xFEFEFE) shr 1, art)
        }
        if (zPos != null && !zPos.occludesAdjacentBlocks) {
            bitmap.renderWall(pos.x + 0.0, pos.z + 1.0, pos.x + 1.0, pos.z + 1.0, tex, col, art)
        }
    }

    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }
}