package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound

class BatBossEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8, Art.getCol(0xFFFF00)) {
    init {
        this.health = 5
        this.r = 0.3

        this.flying = true
    }

    override fun die() {
        Sound.bosskill.play()
        level?.addEntity(KeyEntity(x, z))
    }

    override fun tick() {
        super.tick()
        if (random.nextInt(20) == 0) {
            val xx = x + (random.nextDouble() - 0.5) * 2
            val zz = z + (random.nextDouble() - 0.5) * 2
            val batEntity = BatEntity(xx, zz)
            batEntity.level = level

            batEntity.x = -999.0
            batEntity.z = -999.0

            if (batEntity.isFree(xx, zz)) {
                level?.addEntity(batEntity)
            }
        }
    }
}