package com.mojang.escape.entities

import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
abstract class ItemEntity(
    pos: Point3D,
    rot: Double,
    vel: Point3D, 
    rotVel: Double,
    override val collisionBox: RelativeAABB,
    art: Bitmap,
    tex: Int,
    col: Int
): Entity(
    pos = pos,
    rot = rot,
    vel = vel,
    rotVel = rotVel, 
    flying = false
), ISpriteEntity, ICollidableEntity {
    final override val sprites = mutableListOf<Sprite>()
    
    init {
        this.sprites.add(BasicSprite(0.0, 0.0, 0.0, tex, col, art))
    }

    override fun onInit(level: Level) {
        // Do nothing
    }
    override fun onTick(level: Level) {
        this.sprites[0].y = this.pos.y
    }

    override fun onCollideWithEntity(level: Level, other: Entity) {
        if (other is Player) {
            Sound.pickup.play()
            onPickup(level, other)
            this.removed = true
        }
    }

     override fun blocksEntity(level: Level, entity: Entity): Boolean {
         return entity is Player
     }
    
    protected abstract fun onPickup(level: Level, player: Player)
}