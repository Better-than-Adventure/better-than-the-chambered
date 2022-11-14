package com.mojang.escape.entities

import com.mojang.escape.Art

class EyeEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 4, Art.getCol(0x84ECFF)) {
    init {
        this.health = 4
        this.r = 0.3
        this.runSpeed = 2.0
        this.spinSpeed *= 1.5

        this.flying = true
    }
}