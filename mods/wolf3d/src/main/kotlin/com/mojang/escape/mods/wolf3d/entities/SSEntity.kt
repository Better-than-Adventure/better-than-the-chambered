package com.mojang.escape.mods.wolf3d.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.EnemyEntity
import com.mojang.escape.level.Level
import com.mojang.escape.mods.wolf3d.ModArt
import kotlin.math.atan2

class SSEntity(x: Double, z: Double): EnemyEntity(x, z, 4 * 8 + 2, Art.getCol(0x0000FF), ModArt.sprites) {
   
    private var shootDelay: Int = 0
    private var shootCount = 4 
    

    init {
        this.health = 10
        this.r = 0.4
        this.spinSpeed = 0.05
    }

    override fun die() {
        super.die()
        this.level!!.addEntity(AmmoEntity(x, z))
    }

    override fun hurt(xd: Double, zd: Double) {
        super.hurt(xd, zd)
        this.shootDelay = 50
    }

    override fun tick(level: Level) {
        super.tick()
        if (shootDelay > 0) {
            shootDelay--
        } else if (random.nextInt(40) == 0) {
            if (shootCount > 0) {
                shootCount--
                shootDelay = 5
            } else {
                shootCount = 4
                shootDelay = 40
            }
            level!!.addEntity(Bullet(this, x, z, atan2(level!!.player.x - x, level!!.player.z - z), 0.3, 1, defaultColor))
        }
    }
}