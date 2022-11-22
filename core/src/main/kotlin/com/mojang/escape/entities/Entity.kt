package com.mojang.escape.entities

import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.physics.AABB
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point2I
import kotlin.math.abs
import java.util.Random
import kotlin.math.floor

abstract class Entity(
    val art: Bitmap,
    var pos: Point2D = Point2D(0.0, 0.0),
    var rot: Double = 0.0,
    var vel: Point2D = Point2D(0.0, 0.0),
    var rotVel: Double = 0.0,
    val flying: Boolean = false,
    val collisionBox: AABB = AABB(0.4)
) {
    protected val random = Random()

    val tilePos: Point2I
        get() = Point2I((pos.x + 0.5).toInt(), (pos.y + 0.5).toInt())
    
    val sprites = arrayListOf<Sprite>()
    var removed = false

    protected open fun move(level: Level) {
        val xSteps = (abs(xa * 100) + 1).toInt()
        for (i in xSteps downTo 1) {
            val xxa = xa
            if (isFree(level, x + xxa * i / xSteps, z)) {
                x += xxa * i / xSteps
                break
            } else {
                xa = 0.0
            }
        }

        val zSteps = (abs(za * 100) + 1).toInt()
        for (i in zSteps downTo 1) {
            val zza = za
            if (isFree(level, x, z + zza * i / zSteps)) {
                z += zza * i / zSteps
                break
            } else {
                za = 0.0
            }
        }
    }
    
    abstract fun onCollideWithEntity(level: Level, other: Entity)

    fun isFree(level: Level, xx: Double, yy: Double): Boolean {
        val x0 = floor(xx + 0.5 - r).toInt()
        val x1 = floor(xx + 0.5 + r).toInt()
        val y0 = floor(yy + 0.5 - r).toInt()
        val y1 = floor(yy + 0.5 + r).toInt()

        if (level.getBlock(x0, y0).blocks(this)) {
            return false
        }
        if (level.getBlock(x1, y0).blocks(this)) {
            return false
        }
        if (level.getBlock(x0, y1).blocks(this)) {
            return false
        }
        if (level.getBlock(x1, y1).blocks(this)) {
            return false
        }

        val xc = floor(xx + 0.5).toInt()
        val zc = floor(yy + 0.5).toInt()
        val rr = 2
        for (z in (zc - rr)..(zc + rr)) {
            for (x in (xc - rr)..(xc + rr)) {
                val es = level.getBlock(x, z).entities
                for (e in es) {
                    if (e == this) {
                        continue
                    }

                    if (!e.blocks(level, this, this.x, this.z, r) && e.blocks(level, this, xx, yy, r)) {
                        e.collide(level, this)
                        this.collide(level, e)
                        return false
                    }
                }
            }
        }

        return true
    }

    open fun blocks(level: Level, entity: Entity, x2: Double, z2: Double, r2: Double): Boolean {
        if (entity is Bullet) {
            if (entity.owner == this) {
                return false
            }
        }
        if (x + r <= x2 - r2) {
            return false
        }
        if (x - r >= x2 + r2) {
            return false
        }

        if (z + r <= z2 - r2) {
            return false
        }
        if (z - r >= z2 + r2) {
            return false
        }

        return true
    }

    fun contains(x2: Double, z2: Double): Boolean {
        if (x + r <= x2) return false;
        if (x - r >= x2) return false;

        if (z + r <= z2) return false;
        if (z - r >= z2) return false;

        return true;
    }

    fun isInside(x0: Double, z0: Double, x1: Double, z1: Double): Boolean {
        if (x + r <= x0) {
            return false
        }
        if (x - r >= x1) {
            return false
        }

        if (z + r <= z0) {
            return false
        }
        if (z - r >= z1) {
            return false
        }

        return true
    }

    abstract fun use(level: Level, source: Entity, item: Item): Boolean

    abstract fun tick(level: Level)
}