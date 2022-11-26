package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.*
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class BoulderEntity(
    pos: Point2I,
    rot: Double,
    vel: Point2D,
    rotVel: Double
): Entity(
    pos = Point3D(pos.x + 0.5, 0.0, pos.z + 0.5),
    rot = rot,
    vel = Point3D(vel.x, 0.0, vel.z),
    rotVel = rotVel,
    flying = false
), ISpriteEntity, IUsableEntity, ICollidableEntity {
    companion object {
        val COLOR = Art.getCol(0xAFA293)
    }

    override val sprites = mutableListOf<Sprite>()
    override val collisionBox = RelativeAABB(0.5)

    private var rollDist: Double = 0.0
    
    private var velO = this.vel

    init {
        val sprite = BasicSprite(0.0, 0.0, 0.0, 8 * 2 + 0, COLOR, ModArt.sprites)
        this.sprites.add(sprite)
    }

    override fun onInit(level: Level) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }

    override fun onCollideWithEntity(level: Level, other: Entity) {
        // Do nothing
    }

    override fun onTick(level: Level) {
        if (vel.x == 0.0 && velO.x != 0.0) {
            vel = vel.copy(x = -velO.x * 0.3)
        }
        if (vel.z == 0.0 && velO.z != 0.0) {
            vel = vel.copy(z = -velO.z * 0.3)
        }
        vel = vel.copy(
            x = vel.x * 0.98,
            z = vel.z * 0.98
        )
        if (vel.x * vel.x + vel.z * vel.z < 0.0001) {
            vel = vel.copy(x = 0.0, z = 0.0)
        }
        this.rollDist += sqrt(vel.x * vel.x + vel.z * vel.z)
        this.sprites[0].tex = 8 * 1 + ((rollDist * 4).toInt() and 1)
        velO = vel
    }

    override fun onUsed(level: Level, source: Entity, item: Item) {
        if (item != Item.PowerGlove) {
            return
        }
        ModSound.roll.play()
        
        this.vel = vel.copy(
            x = vel.x + sin(source.rot) * 0.1,
            z = vel.z + cos(source.rot) * 0.1
        )
    }
}