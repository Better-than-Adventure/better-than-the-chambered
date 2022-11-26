package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.entities.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.ITriggerable
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.*
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.entities.OgreEntity

open class DoorBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int,
    val doorArt: Bitmap,
    val doorTex: Int,
    val doorCol: Int, 
    override val triggerId: Int
): EmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
), ICollidableBlock, IUsableBlock, ITickableBlock, ITriggerable {
    var open = false
    var openness = 0.0
    override val collisionBox = RelativeAABB(0.5)
    override var triggerState = 0

    override fun onUsed(level: Level, source: Entity, item: Item) {
        open = !open
        if (open) {
            Sound.click1
        } else {
            Sound.click2
        }.play()
    }

    override fun onTick(level: Level) {
        if (open) {
            openness += 0.2
        } else {
            openness -= 0.2
        }
        if (openness < 0) openness = 0.0
        if (openness > 1) openness = 1.0
        
        val openLimit = 7 / 8.0
        if (openness < openLimit && !open) {
            if (level.entitiesInAABB(offsetCollisionBox).isNotEmpty()) {
                openness = openLimit
            }
        }
    }

    override fun onTrigger(level: Level, source: Entity?, state: Int) {
        open = state % 2 == 0
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return !open
    }

    override fun doRender(level: Level, bitmap: Bitmap3D) {
        val rr = 1 / 8.0
        val openness = 1 - openness * 7 / 8
        val east = level[pos.x + 1, pos.z]
        if (east != null && east.occludesAdjacentBlocks) {
            bitmap.renderWall(pos.x + openness, pos.z + 0.5 - rr, pos.x.toDouble(), pos.z + 0.5 - rr, doorTex, (doorCol and 0xFEFEFE) shr 1, doorArt, 0.0, openness)
            bitmap.renderWall(pos.x.toDouble(), pos.z + 0.5 + rr, pos.x + openness, pos.z + 0.5 + rr, doorTex, (doorCol and 0xFEFEFE) shr 1, doorArt, openness, 0.0)
            bitmap.renderWall(pos.x + openness, pos.z + 0.5 + rr, pos.x + openness, pos.z + 0.5 - rr, doorTex,  doorCol, doorArt, 0.5 - rr, 0.5 + rr)
        } else {
            bitmap.renderWall(pos.x + 0.5 - rr, pos.z.toDouble(), pos.x + 0.5 - rr, pos.z + openness, doorTex,  doorCol, doorArt, openness, 0.0)
            bitmap.renderWall(pos.x + 0.5 + rr, pos.z + openness, pos.x + 0.5 + rr, pos.z.toDouble(), doorTex,  doorCol, doorArt, 0.0, openness)
            bitmap.renderWall(pos.x + 0.5 - rr, pos.z + openness, pos.x + 0.5 + rr, pos.z + openness, doorTex, (doorCol and 0xFEFEFE) shr 1, doorArt, 0.5 - rr, 0.5 + rr)
        }
    }

    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }
}