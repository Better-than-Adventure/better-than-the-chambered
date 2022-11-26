package com.mojang.escape.entities

import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.*
import java.util.Random

abstract class Entity(
    var pos: Point3D,
    var rot: Double,
    var vel: Point3D,
    var rotVel: Double,
    val flying: Boolean
) {
    protected val random = Random()
    
    val tilePos: Point2I
        get() = Point2I((pos.x + 0.5).toInt(), (pos.z + 0.5).toInt())
    
    var removed = false

    abstract fun onInit(level: Level)
    abstract fun onTick(level: Level)
}

interface ISpriteEntity {
    val sprites: MutableList<Sprite>
}

interface IUsableEntity {
    fun onUsed(level: Level, source: Entity, item: Item)
}

interface ICollidableEntity {
    val pos: Point3D
    val collisionBox: RelativeAABB
    val offsetCollisionBox: AABB
        get() = collisionBox.offset(pos.toPoint2D())

    fun blocksEntity(level: Level, entity: Entity): Boolean

    fun onCollideWithEntity(level: Level, other: Entity)
}

interface IFlyingEntity {
    val flying: Boolean
}