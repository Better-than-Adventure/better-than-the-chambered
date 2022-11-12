package com.mojang.escape.entities

import com.mojang.escape.Art

class BatEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8, Art.getCol(0x82666E)) {
    init {
        this.health = 2
        this.r = 0.3

        this.flying = true
    }
}