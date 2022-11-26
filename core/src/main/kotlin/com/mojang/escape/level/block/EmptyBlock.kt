package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2I

open class EmptyBlock(
    pos: Point2I,
    var floorArt: Bitmap,
    var floorTex: Int,
    var floorCol: Int,
    var ceilArt: Bitmap,
    var ceilTex: Int,
    var ceilCol: Int
): Block(
    pos = pos,
    occludesAdjacentBlocks = false
) {
    override fun onInit(level: Level) {
        // Do nothing
    }

    override fun doRender(level: Level, bitmap: Bitmap3D) {
        // Floor rendering is flood fill, so do nothing
    }
    
    open fun onEntityEnter(level: Level, entity: Entity) {
        // Do nothing
    }

    open fun onEntityLeave(level: Level, entity: Entity) {
        // Do nothing
    }

    open fun onEntityMoveWhileInside(level: Level, entity: Entity, dx: Double, dz: Double) {
        // Do nothing
    }

    open fun getFloorHeight(level: Level, entity: Entity): Double {
        // Do nothing
        return 0.0
    }

    open fun getWalkSpeed(level: Level, entity: Entity): Double {
        // Do nothing
        return 1.0
    }

    open fun getFriction(level: Level, entity: Entity): Double {
        // Do nothing
        return 0.6
    }
}