package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.mods.prelude.ModSound
import com.mojang.escape.mods.prelude.entities.EyeBossEntity
import com.mojang.escape.mods.prelude.entities.EyeEntity
import kotlin.math.cos
import kotlin.math.sin

class IceBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): EmptyBlock(
    pos,
    floorArt,
    floorTex,
    floorCol,
    ceilArt,
    ceilTex,
    ceilCol
) {

    override fun getWalkSpeed(level: Level, entity: Entity): Double {
        if (entity is Player && entity.selectedItem == Item.Skates) {
            return 0.05
        }
        return 1.4
    }

    override fun getFriction(level: Level, entity: Entity): Double {
        if (entity is Player && entity.selectedItem == Item.Skates) {
            return 0.98
        }
        
        return 1.0
    }

    override fun onEntityEnter(level: Level, entity: Entity) {
        if (entity !is IFlyingEntity || !entity.flying) {
            ModSound.slide.play()
        }
    }

    override fun onEntityMoveWhileInside(level: Level, entity: Entity, dx: Double, dz: Double) {
        if ((entity is Player && entity.selectedItem != Item.Skates) ||
                entity !is IFlyingEntity || !entity.flying) {
            if (entity.vel.x * entity.vel.x > entity.vel.z * entity.vel.z) {
                entity.vel = entity.vel.copy(
                    z = 0.0,
                    x = if (entity.vel.x > 0.0) {
                        0.08
                    } else {
                        -0.08
                    }
                )
                entity.pos = entity.pos.copy(z = entity.pos.z + ((entity.pos.z + 0.5).toInt() - entity.pos.z) * 0.2)
            } else if (entity.vel.x * entity.vel.x < entity.vel.z * entity.vel.z) {
                entity.vel = entity.vel.copy(
                    x = 0.0,
                    z = if (entity.vel.z > 0) {
                        0.08
                    } else {
                        -0.08
                    }
                )
                entity.pos = entity.pos.copy(x = entity.pos.x + ((entity.pos.x + 0.5)) * 0.2)
            } else {
                entity.vel = entity.vel.copy(
                    x = entity.vel.x - (dx * cos(entity.rot) + dz * sin(entity.rot)) * 0.1,
                    z = entity.vel.z - (dz * cos(entity.rot) - dx * sin(entity.rot)) * 0.1
                )
            }
        }
    }
}