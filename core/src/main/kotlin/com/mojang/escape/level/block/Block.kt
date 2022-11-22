package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.AABB
import java.util.*

abstract class Block(
    val x: Int,
    val y: Int,
    val occludesAdjacentBlocks: Boolean
) {
    protected val random = Random()

    abstract fun init(level: Level)

    abstract fun doRender(bitmap: Bitmap3D, level: Level)
}

interface ITickableBlock {
    fun tick(level: Level)
}

interface ICollidableBlock {
    val collisionBox: AABB
    
    fun onEntityCollision(level: Level, entity: Entity)
}