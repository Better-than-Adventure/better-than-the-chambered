package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.entities.KeyEntity
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class EyeBossEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 4, Art.getCol(0xFFFF00), ModArt.sprites) {
    init {
        this.health = 10
        this.r = 0.3
        this.runSpeed = 4.0
        this.spinSpeed *= 1.5

        flying = true
    }

    override fun die() {
        ModSound.bosskill.play()
        level?.addEntity(KeyEntity(this.x, this.z))
    }
}