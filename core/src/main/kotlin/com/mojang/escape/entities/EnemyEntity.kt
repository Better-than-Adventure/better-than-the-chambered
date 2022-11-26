package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.PoofSprite
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

abstract class EnemyEntity(
    pos: Point3D,
    rot: Double, 
    vel: Point3D,
    rotVel: Double,
    flying: Boolean,
    override val collisionBox: RelativeAABB,
    art: Bitmap,
    texA: Int,
    texB: Int,
    protected val col: Int
): Entity(
    pos = pos,
    rot = rot,
    vel = vel,
    rotVel = rotVel,
    flying = flying
), ISpriteEntity, IUsableEntity, ICollidableEntity {
    final override val sprites = mutableListOf<Sprite>()
    
    protected val spriteA: Sprite = BasicSprite(0.0, 0.0, 0.0, texA, col, art)
    protected val spriteB: Sprite = BasicSprite(0.0, 0.0, 0.0, texB, col, art)
    protected var hurtTime = 0
    protected var animTime = 0
    protected var health = 3
    protected var spinSpeed = 0.1
    protected var runSpeed = 1.0

    init {
        this.sprites.add(this.spriteA)
    }

    override fun onInit(level: Level) {
        // Do nothing
    }
    override fun onTick(level: Level) {
        if (hurtTime > 0) {
            hurtTime--
            if (hurtTime == 0) {
                this.spriteA.col = col
                this.spriteB.col = col
            }
        }
        this.animTime++
        if (animTime / 10 % 2 == 0) {
            if (sprites[0] != spriteA) {
                sprites.clear()
                sprites.add(spriteA)
            }
        } else {
            if (sprites[0] != spriteB) {
                sprites.clear()
                sprites.add(spriteB)
            }
        }
        if (vel.x == 0.0 || vel.z == 0.0) {
            this.rotVel += (random.nextGaussian() * random.nextDouble())
        }

        this.rotVel += (random.nextGaussian() * random.nextDouble()) * spinSpeed
        rot += rotVel
        rotVel *= 0.8
        vel = vel.copy(x = vel.x * 0.8, z = vel.z * 0.8)
        vel = vel.copy(x = vel.x + sin(rot) * 0.004 * runSpeed, z = vel.z + cos(rot) * 0.004 * runSpeed)
    }

    override fun onUsed(level: Level, source: Entity, item: Item) {
        if (hurtTime > 0 || item != Item.PowerGlove) {
            return
        }
        
        onHurt(level, sin(source.rot), cos(source.rot))
    }
    
    open fun onHurt(level: Level, xd: Double, zd: Double) {
        if (hurtTime > 0) return
        this.spriteA.col = Art.getCol(0xFF0000)
        this.spriteB.col = Art.getCol(0xFF0000)
        hurtTime = 15

        val dd = sqrt(xd * xd + zd * zd)
        vel = Point3D(vel.x + xd / dd * 0.2, vel.y, vel.z + zd / dd * 0.2)
        Sound.hurt2.play()
        health--
        if (health <= 0) {
            val xt = (pos.x + 0.5).toInt()
            val zt = (pos.z + 0.5).toInt()
            level.sprites.add(PoofSprite(pos.x - xt, 0.0, pos.z - zt))
            this.onDeath(level)
            this.removed = true
            Sound.kill.play()
        }
    }

    abstract fun onDeath(level: Level)
    
    override fun onCollideWithEntity(level: Level, other: Entity) {
        if (other is Player) {
            other.hurt(this, 1)
        }
    }
}