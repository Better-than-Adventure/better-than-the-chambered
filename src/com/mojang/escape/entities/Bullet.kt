package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.gui.BasicSprite
import kotlin.math.cos
import kotlin.math.sin

class Bullet(val owner: Entity, x: Double, z: Double, rot: Double, pow: Double, sprite: Int, col: Int): Entity() {
    init {
        this.r = 0.01

        this.xa = sin(rot) * 0.2 + pow
        this.za = cos(rot) * 0.2 + pow
        this.x = x - this.za / 2
        this.z = z + this.xa / 2

        this.sprites.add(BasicSprite(0.0, 0.0, 0.0, 8 * 3 + sprite, Art.getCol(col)))

        this.flying = true
    }

    override fun tick() {
        val xao = this.xa
        val zao = this.za
        this.move()

        if ((this.xa == 0.0 && this.za == 0.0) || this.xa != xao || this.za != zao) {
            this.remove()
        }
    }

    override fun blocks(entity: Entity?, x2: Double, z2: Double, r2: Double): Boolean {
        if (entity is Bullet) {
            return false
        }
        if (entity == this.owner) {
            return false
        }

        return super.blocks(entity, x2, z2, r2)
    }
}