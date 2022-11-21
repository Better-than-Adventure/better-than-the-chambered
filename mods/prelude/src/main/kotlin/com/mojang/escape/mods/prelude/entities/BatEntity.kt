package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.mods.prelude.ModArt

class BatEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8, Art.getCol(0x82666E), ModArt.sprites) {
    init {
        this.health = 2
        this.r = 0.3

        this.flying = true
    }
}