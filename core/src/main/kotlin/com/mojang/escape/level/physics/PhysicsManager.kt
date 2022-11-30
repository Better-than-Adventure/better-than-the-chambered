package com.mojang.escape.level.physics

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.ICollidableEntity
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.ICollidableBlock
import kotlin.math.abs
import kotlin.math.floor

class PhysicsManager {
    fun linearMoveEntity(level: Level, entity: Entity) {
        var x = entity.pos.x
        var y = entity.pos.y
        var z = entity.pos.z
        var xa = entity.vel.x
        var ya = entity.vel.y
        var za = entity.vel.z
        
        // Gravity
        if (entity.pos.y > 0.0) {
            ya -= 0.005
        }

        val xSteps = (abs(xa * 100) + 1).toInt()
        for (i in xSteps downTo 1) {
            val xxa = xa
            if (canEntityMoveTo(level, entity, x + xxa * i / xSteps, z)) {
                x += xxa * i / xSteps
                break
            } else {
                xa = 0.0
            }
        }

        val ySteps = (abs(ya * 100) + 1).toInt()
        for (i in ySteps downTo 1) {
            val yya = ya
            if (y + yya * i / ySteps > 0.0) {
                y += yya * i / ySteps
                break
            } else {
                ya = 0.0
            }
        }

        val zSteps = (abs(za * 100) + 1).toInt()
        for (i in zSteps downTo 1) {
            val zza = za
            if (canEntityMoveTo(level, entity, x, z + zza * i / zSteps)) {
                z += zza * i / zSteps
                break
            } else {
                za = 0.0
            }
        }
        
        entity.pos = Point3D(x, y, z)
        entity.vel = Point3D(xa, ya, za)
    }


    fun canEntityMoveTo(level: Level, entity: Entity, newX: Double, newZ: Double): Boolean {
        if (entity !is ICollidableEntity) {
            return true
        }
        
        val x0 = floor(newX + 0.5 + entity.collisionBox.min.x).toInt()
        val x1 = floor(newX + 0.5 + entity.collisionBox.max.x).toInt()
        val z0 = floor(newZ + 0.5 + entity.collisionBox.min.z).toInt()
        val z1 = floor(newZ + 0.5 + entity.collisionBox.max.z).toInt()
        
        for (block in arrayOf(level[x0, z0], level[x1, z0], level[x0, z1], level[x1, z1])) {
            if (block is ICollidableBlock && block.blocksEntity(level, entity)) {
                if (block.offsetCollisionBox.intersects(entity.collisionBox.offset(0.5 + newX, 0.5 + newZ))) {
                    return false
                }
            }
        }
        
        val xc = floor(newX + 0.5).toInt()
        val zc = floor(newZ + 0.5).toInt()
        val rr = 2
        for (zb in (zc - rr)..(zc + rr)) {
            for (xb in (xc - rr)..(xc + rr)) {
                val entities = level.entities
                for (other in entities) {
                    if (other == entity) {
                        continue
                    }
                    if (other !is ICollidableEntity) {
                        continue
                    }
                    if (!other.blocksEntity(level, entity)) {
                        continue
                    }
                    
                    if (!other.offsetCollisionBox.intersects(entity.offsetCollisionBox) &&
                        other.offsetCollisionBox.intersects(entity.collisionBox.offset(newX, newZ))) {
                        entity.onCollideWithEntity(level, other)
                        return false
                    }
                }
            }
        }
        
        return true
    }
}