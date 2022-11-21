package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.mods.prelude.ModArt
import kotlin.math.atan2
import kotlin.math.sin
import kotlin.math.sqrt

class GhostBossEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 6, Art.getCol(0xFFFF00), ModArt.sprites) {
    private var rotatePos = 0.0
    private var shootDelay = 0

    init {
        this.health = 10
        this.flying = true
    }

    override fun tick() {
        animTime++
        sprite.tex = defaultTex + animTime / 10 % 2

        var xd = ((level!!.player.x ?: 0.0) + sin(rotatePos) * 2) - x
        var zd = ((level!!.player.z ?: 0.0) + sin(rotatePos) * 2) - z
        var dd = xd * xd + zd * zd

        if (dd < 1) {
            rotatePos += 0.04
        } else {
            rotatePos = level!!.player.rot
        }

        if (dd < 4 * 4) {
            dd = sqrt(dd)

            xd /= dd
            zd /= dd

            xa += xd * 0.006
            za += zd * 0.006

            if (shootDelay > 0) {
                shootDelay--
            } else if (random.nextInt(10) == 0) {
                shootDelay = 10
                level?.addEntity(Bullet(this, x, z, atan2((level?.player?.x ?: 0.0) - x, (level?.player?.z ?: 0.0) - z), 0.20, 1, defaultColor))
            }
        }
        
        move()
        
        xa *= 0.9
        za *= 0.9
    }

    override fun hurt(xd: Double, zd: Double) {
        // Do nothing
    }

    override fun move() {
        x += xa
        z += za
    }
}