package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.col
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import kotlin.math.atan2

class OgreEntity(
    pos: Point3D,
    rot: Double,
    vel: Point3D,
    rotVel: Double
): EnemyEntity(
    pos = pos,
    rot = rot,
    vel = vel,
    rotVel = rotVel,
    flying = false,
    collisionBox = RelativeAABB(0.4),
    art = ModArt.sprites,
    texA = 8 * 4 + 2,
    texB = 8 * 4 + 3,
    col = 0x82A821.col
) {
    var shootDelay: Int = 0
    
    init {
        this.health = 6
        this.spinSpeed = 0.05
    }

    override fun onHurt(level: Level, xd: Double, zd: Double) {
        super.onHurt(level, xd, zd)
        this.shootDelay = 50
    }

    override fun onDeath(level: Level) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }

    override fun onTick(level: Level) {
        super.onTick(level)
        if (shootDelay > 0) {
            shootDelay--
        } else if (random.nextInt(40) == 0) {
            shootDelay = 40
            level.entities += Bullet(
                pos = pos,
                rot = atan2(level.session.player.pos.x - pos.x, level.session.player.pos.z - pos.z),
                pow = 0.3,
                art = Art.sprites,
                tex = 8 * 1 + 1,
                col = this.col,
                owner = this
            )
        }
    }
}