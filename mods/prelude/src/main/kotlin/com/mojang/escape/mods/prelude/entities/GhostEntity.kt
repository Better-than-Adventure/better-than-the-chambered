package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.col
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GhostEntity(
    pos: Point3D,
    rot: Double,
    vel: Point3D,
    rotVel: Double
): EnemyEntity(
    pos = pos,
    rot = rot,
    vel = vel,
    rotVel = rotVel,
    flying = true,
    collisionBox = RelativeAABB(0.3),
    art = ModArt.sprites,
    texA = 8 * 4 + 4,
    texB = 8 * 4 + 5,
    col = 0xFFFFFF.col
) {
    private var rotatePos = 0.0

    override fun onTick(level: Level) {
        animTime++
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

        vel = vel.copy(
            x = vel.x * 0.9,
            z = vel.z * 0.9
        )

        var xd = (level.session.player.pos.x + sin(rotatePos) * 2) - pos.x
        var zd = (level.session.player.pos.z + cos(rotatePos) * 2) - pos.z
        var dd = xd * xd + zd + zd

        if (dd < 1) {
            rotatePos += 0.04
        } else {
            rotatePos = level.session.player.rot
        }

        if (dd < 4 * 4) {
            dd = sqrt(dd)

            xd /= dd
            zd /= dd

            vel = vel.copy(
                x = vel.x + xd * 0.006,
                z = vel.z + zd * 0.006
            )
        }
    }

    override fun onHurt(level: Level, xd: Double, zd: Double) {
        // Do nothing
    }

    override fun onDeath(level: Level) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }
}