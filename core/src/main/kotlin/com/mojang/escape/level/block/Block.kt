package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.AABB
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import java.util.*

abstract class Block(
    val pos: Point2I,
    val occludesAdjacentBlocks: Boolean
) {
    protected val random = Random()

    abstract fun onInit(level: Level)
    
    abstract fun onDeinit(level: Level)

    abstract fun doRender(level: Level, bitmap: Bitmap3D)
    
    fun entitiesInBlock(level: Level): List<Entity> {
        return level.entitiesInAABB(AABB(pos.toPoint2D(), Point2D(pos.x.toDouble() + 1.0, pos.z.toDouble() + 1.0)))
    }
}

interface IUsableBlock {
    fun onUsed(level: Level, source: Entity, item: Item)

}

interface ITickableBlock {
    fun onTick(level: Level)
}

interface ICollidableBlock {
    val pos: Point2I
    val collisionBox: RelativeAABB
    val offsetCollisionBox 
        get() = collisionBox.offset(pos.toPoint2D())
    
    fun onEntityCollision(level: Level, entity: Entity)

    fun blocksEntity(level: Level, entity: Entity): Boolean
}

interface ILevelChangeBlock {
    val levelChangeIdOut: Int
    val levelChangeIdIn: Int
    val targetLevel: String
    
    fun onLevelEnter(level: Level, entity: Entity)
    
    fun onLevelLeave(level: Level, entity: Entity)
}

interface ITriggerEmitterBlock {
    val triggerEmitId: Int
}