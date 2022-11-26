package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.col
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt

class BatEntity(
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
    col = 0x82666E.col
) {
    init {
        this.health = 2
    }

    override fun onDeath(level: Level) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        if (entity is BatEntity || entity is BatBossEntity) return false
        return true
    }
}