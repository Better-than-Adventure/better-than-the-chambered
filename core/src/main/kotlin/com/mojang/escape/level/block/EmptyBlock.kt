package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level

open class EmptyBlock(
    x: Int,
    y: Int,
    val floorArt: Bitmap,
    val floorTex: Int,
    val floorCol: Int,
    val ceilArt: Bitmap,
    val ceilTex: Int,
    val ceilCol: Int
): Block(
    x = x,
    y = y,
    occludesAdjacentBlocks = false
) {
    override fun init(level: Level) {
        // Do nothing
    }

    override fun doRender(bitmap: Bitmap3D, level: Level) {
        // Floor rendering is flood fill, so do nothing
    }
    
    open fun onEntityEnter(level: Level, entity: Entity) {
        // Do nothing
    }

    open fun onEntityLeave(level: Level, entity: Entity) {
        // Do nothing
    }

    open fun onEntityMoveWhileInside(level: Level, entity: Entity, dx: Double, dy: Double) {
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