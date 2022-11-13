package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Sprite

class KeyEntity(x: Double, z: Double): Entity() {
    companion object {
        val COLOR = Art.getCol(0x00FFFF)
    }

    private val sprite: Sprite
    private var y: Double
    private var ya: Double

    init {
        this.x = x
        this.z = z
        this.y = 0.5
        this.ya = 0.025
        this.sprite = BasicSprite(0.0, 0.0, 0.0, 16 + 3, COLOR)
        this.sprites.add(this.sprite)
    }

    override fun tick() {
        this.move()
        this.y += this.ya
        if (this.y < 0.0) {
            this.y = 0.0
        }
        this.ya -= 0.005
        this.sprite.y = this.y
    }

    override fun collide(entity: Entity) {
        if (entity is Player) {
            Sound.key.play()
            entity.keys++
            this.remove()
        }
    }
}