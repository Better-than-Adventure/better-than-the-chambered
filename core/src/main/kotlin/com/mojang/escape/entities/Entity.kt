package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import kotlin.math.abs
import java.util.Random
import kotlin.math.floor

open class Entity(val art: Bitmap) {
    companion object {
        val random = Random()
    }

    val sprites = arrayListOf<Sprite>()

    var x = 0.0
    var z = 0.0
    var rot = 0.0

    var xa = 0.0
    var za = 0.0
    var rota = 0.0

    var r = 0.4

    var level: Level? = null

    var xTileO = -1
    var zTileO = -1
    var flying = false

    private var removed = false

    fun updatePos() {
        val xTile = (x + 0.5).toInt()
        val zTile = (z + 0.5).toInt()
        if (xTile != xTileO || zTile != zTileO) {
            level?.getBlock(xTileO, zTileO)?.removeEntity(this)

            xTileO = xTile
            zTileO = zTile

            if (!removed) {
                level?.getBlock(xTileO, zTileO)?.addEntity(this)
            }
        }
    }

    fun isRemoved(): Boolean {
        return removed
    }

    fun remove() {
        level?.getBlock(xTileO, zTileO)?.removeEntity(this)
        removed = true
    }

    protected open fun move() {
        val xSteps = (abs(xa * 100) + 1).toInt()
        for (i in xSteps downTo 1) {
            val xxa = xa
            if (isFree(x + xxa * i / xSteps, z)) {
                x += xxa * i / xSteps
                break
            } else {
                xa = 0.0
            }
        }

        val zSteps = (abs(za * 100) + 1).toInt()
        for (i in zSteps downTo 1) {
            val zza = za
            if (isFree(x, z + zza * i / zSteps)) {
                z += zza * i / zSteps
                break
            } else {
                za = 0.0
            }
        }
    }

    fun isFree(xx: Double, yy: Double): Boolean {
        val x0 = floor(xx + 0.5 - r).toInt()
        val x1 = floor(xx + 0.5 + r).toInt()
        val y0 = floor(yy + 0.5 - r).toInt()
        val y1 = floor(yy + 0.5 + r).toInt()

        if (level!!.getBlock(x0, y0).blocks(this)) {
            return false
        }
        if (level!!.getBlock(x1, y0).blocks(this)) {
            return false
        }
        if (level!!.getBlock(x0, y1).blocks(this)) {
            return false
        }
        if (level!!.getBlock(x1, y1).blocks(this)) {
            return false
        }

        val xc = floor(xx + 0.5).toInt()
        val zc = floor(yy + 0.5).toInt()
        val rr = 2
        for (z in (zc - rr)..(zc + rr)) {
            for (x in (xc - rr)..(xc + rr)) {
                val es = level!!.getBlock(x, z).entities
                for (e in es) {
                    if (e == this) {
                        continue
                    }

                    if (!e.blocks(this, this.x, this.z, r) && e.blocks(this, xx, yy, r)) {
                        e.collide(this)
                        this.collide(e)
                        return false
                    }
                }
            }
        }

        return true
    }

    protected open fun collide(entity: Entity) {
    }

    open fun blocks(entity: Entity, x2: Double, z2: Double, r2: Double): Boolean {
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

    open fun use(source: Entity, item: Item): Boolean {
        return false
    }

    open fun tick() {
    }
}