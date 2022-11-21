package com.mojang.escape.entities

import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite

abstract class ItemEntity(x: Double, z: Double, spriteIndex: Int, spriteColor: Int, art: Bitmap): Entity(art) {
    private val sprite: Sprite
    private var y: Double
    private var ya: Double
    
    init {
        this.x = x
        this.z = z
        this.y = 0.5
        this.ya = 0.025
        this.sprite = BasicSprite(0.0, 0.0, 0.0, spriteIndex, spriteColor, art)
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
            Sound.pickup.play()
            onPickup(entity)
            this.remove()
        }
    }
    
    protected abstract fun onPickup(player: Player)
}