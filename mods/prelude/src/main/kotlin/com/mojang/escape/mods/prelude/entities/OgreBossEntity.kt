package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.col
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.KeyEntity
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound
import kotlin.math.atan2

class OgreBossEntity(
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
    col = 0xFFFF00.col
) {
    private var shootDelay: Int = 0
    private var shootPhase: Int = 0

    init {
        this.health = 10
        this.spinSpeed = 0.05
    }

    override fun onHurt(level: Level, xd: Double, zd: Double) {
        super.onHurt(level, xd, zd)
        this.shootDelay = 50
    }

    override fun onDeath(level: Level) {
        ModSound.bosskill.play()
        level.entities.add(KeyEntity(
            pos = pos,
            rot = 0.0,
            vel = Point3D(0.0, 0.0, 0.0),
            rotVel = 0.0
        ))
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }

    override fun onTick(level: Level) {
        super.onTick(level)
        if (this.shootDelay > 0.0) {
            this.shootDelay--
        } else {
            this.shootDelay = 5
            val salva = 10

            for (i in 0 until 4) {
                val rot = Math.PI / 2 * (i + shootPhase / salva % 2 * 0.5)
                level.entities += Bullet(
                    pos = pos,
                    rot = rot,
                    pow = 0.4,
                    art = Art.sprites,
                    tex = 8 * 1 + 1,
                    col = this.col,
                    owner = this
                )
            }

            shootPhase++
            if (shootPhase % salva == 0) {
                shootDelay = 40
            }
        }
    }
}