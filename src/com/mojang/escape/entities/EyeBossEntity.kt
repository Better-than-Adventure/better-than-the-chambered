package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound

class EyeBossEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 4, Art.getCol(0xFFFF00)) {
    init {
        this.health = 10
        this.r = 0.3
        this.runSpeed = 4.0
        this.spinSpeed *= 1.5

        flying = true
    }

    override fun die() {
        Sound.bosskill.play()
        level.addEntity(KeyEntity(this.x, this.z))
    }
}