package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.EnemyEntity

class BatEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8, Art.getCol(0x82666E)) {
    init {
        this.health = 2
        this.r = 0.3

        this.flying = true
    }
}