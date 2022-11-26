package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.col
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.KeyEntity
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class BatBossEntity(
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
    texA = 8 * 4 + 0,
    texB = 8 * 4 + 1,
    col = 0xFFFF00.col
) {
    init {
        this.health = 5
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
        if (entity is BatEntity || entity is BatBossEntity) return false
        return true
    }

    override fun onTick(level: Level) {
        super.onTick(level)
        if (random.nextInt(20) == 0) {
            val xx = pos.x + (random.nextDouble() - 0.5) * 2
            val zz = pos.z + (random.nextDouble() - 0.5) * 2
            val batEntity = BatEntity(
                pos = Point3D(xx, 0.0, zz),
                rot = 0.0,
                vel = Point3D(0.0, 0.0, 0.0),
                rotVel = 0.0
            )
            if (level.physics.canEntityMoveTo(level, batEntity, xx, zz)) {
                level.entities.add(batEntity)
            }
        }
    }
}