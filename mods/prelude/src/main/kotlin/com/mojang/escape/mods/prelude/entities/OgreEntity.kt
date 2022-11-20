package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.EnemyEntity
import kotlin.math.atan2

class OgreEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 2, Art.getCol(0x82A821)) {
    
    var shootDelay: Int = 0
    

    init {
        this.health = 6
        this.r = 0.4
        this.spinSpeed = 0.05
    }

    override fun hurt(xd: Double, zd: Double) {
        super.hurt(xd, zd)
        this.shootDelay = 50
    }

    override fun tick() {
        super.tick()
        if (shootDelay > 0) {
            shootDelay--
        } else if (random.nextInt(40) == 0) {
            shootDelay = 40
            level!!.addEntity(Bullet(this, x, z, atan2(level!!.player.x - x, level!!.player.z - z), 0.3, 1, defaultColor))
        }
    }
}