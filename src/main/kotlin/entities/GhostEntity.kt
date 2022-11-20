package com.mojang.escape.entities

import com.mojang.escape.Art
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GhostEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 6, Art.getCol(0xFFFFFF)) {
    private var rotatePos = 0.0

    init {
        this.health = 4
        this.r = 0.3

        this.flying = true
    }

    override fun tick() {
        this.animTime++
        this.sprite.tex = defaultTex + animTime / 10 % 2

        var xd = (level!!.player.x + sin(rotatePos)) - x
        var zd = (level!!.player.z + cos(rotatePos)) - z
        var dd = xd * xd + zd * zd

        if (dd < 4 * 4) {
            if (dd < 1) {
                this.rotatePos += 0.04
            } else {
                this.rotatePos = level!!.player.rot
                this.xa += (random.nextDouble() - 0.5) * 0.02
                this.za += (random.nextDouble() - 0.5) * 0.02
            }

            dd = sqrt(dd)

            xd /= dd
            zd /= dd

            xa += xd * 0.004
            za += zd * 0.004
        }

        this.move()

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