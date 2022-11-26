package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.col
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.KeyEntity
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class EyeBossEntity(
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
    col = 0xFFFF00.col
) {
    init {
        this.health = 10
        this.runSpeed = 4.0
        this.spinSpeed *= 1.5
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
}