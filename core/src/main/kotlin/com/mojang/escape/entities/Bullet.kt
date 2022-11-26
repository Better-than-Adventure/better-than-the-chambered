package com.mojang.escape.entities

import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import kotlin.math.cos
import kotlin.math.sin

class Bullet(
    pos: Point3D,
    rot: Double,
    pow: Double,
    art: Bitmap,
    tex: Int,
    col: Int,
    val owner: Entity,
): Entity(
    pos = Point3D(
        pos.x - (cos(rot) * 0.2 * pow) / 2,
        0.0,
        pos.z - (sin(rot) * 0.2 * pow) / 2
    ),
    rot = rot,
    vel = Point3D(
        sin(rot) * 0.2 * pow,
        0.0,
        cos(rot) * 0.2 * pow
    ),
    rotVel = 0.0,
    flying = true
), ISpriteEntity, ICollidableEntity {
    override val sprites: MutableList<Sprite> = mutableListOf()
    override val collisionBox: RelativeAABB = RelativeAABB(0.01)
    
    private var oldVel = vel.copy()
    
    init {
        this.sprites.add(BasicSprite(0.0, 0.0, 0.0, tex, col, art))
    }

    override fun onInit(level: Level) {
        // Do nothing
    }

    override fun onTick(level: Level) {
        if ((this.vel.x == 0.0 && this.vel.z == 0.0) || this.vel != oldVel) {
            this.removed = true
        }
        
        oldVel = vel
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        if (entity is Bullet || entity == this.owner) {
            return false
        }
        
        return true
    }

    override fun onCollideWithEntity(level: Level, other: Entity) {
        if (this.owner == other) {
            return
        }
        if (other !is EnemyEntity) {
            return
        }
        this.removed = true
        other.onHurt(level, this.vel.x, this.vel.z)
    }
}