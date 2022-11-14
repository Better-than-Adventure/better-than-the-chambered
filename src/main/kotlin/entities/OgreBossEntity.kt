package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound

class OgreBossEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 2, Art.getCol(0xFFFF00)) {
    private var shootDelay: Int = 0
    private var shootPhase: Int = 0

    init {
        this.health = 10
        this.r = 0.4
        this.spinSpeed = 0.05
    }

    override fun die() {
        Sound.bosskill.play()
        this.level!!.addEntity(KeyEntity(x, z))
    }

    override fun tick() {
        super.tick()
        if (this.shootDelay > 0.0) {
            this.shootDelay--
        } else {
            this.shootDelay = 5
            val salva = 10

            for (i in 0 until 4) {
                val rot = Math.PI / 2 * (i + shootPhase / salva % 2 * 0.5)
                this.level!!.addEntity(Bullet(this, x, z, rot, 0.4, 1, defaultColor))
            }

            shootPhase++
            if (shootPhase % salva == 0) {
                shootDelay = 40
            }
        }
    }
}