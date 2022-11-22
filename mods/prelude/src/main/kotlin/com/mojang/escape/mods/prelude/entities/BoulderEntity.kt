package com.mojang.escape.mods.prelude.entities

import com.mojang.escape.Art
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class BoulderEntity(x: Int, z: Int): Entity(ModArt.sprites) {
    companion object {
        val COLOR = Art.getCol(0xAFA293)
    }

    private val sprite: Sprite
    private var rollDist: Double = 0.0

    init {
        this.x = x.toDouble()
        this.z = z.toDouble()
        this.sprite = BasicSprite(0.0, 0.0, 0.0, 16, COLOR, ModArt.sprites)
        this.sprites.add(this.sprite)
    }

    override fun tick(level: Level) {
        this.rollDist += sqrt(xa * xa + za * za)
        this.sprite.tex = 8 + ((rollDist * 4).toInt() and 1)
        val xao = xa
        val zao = za
        this.move()
        if (xa == 0.0 && xao != 0.0) {
            xa = -xao * 0.3
        }
        if (za == 0.0 && zao != 0.0) {
            za = -zao * 0.3
        }
        xa *= 0.98
        za *= 0.98
        if (xa * xa + za * za < 0.0001) {
            xa = 0.0
            za = 0.0
        }
    }

    override fun use(source: Entity, item: Item): Boolean {
        if (item != Item.PowerGlove) {
            return false
        }
        ModSound.roll.play()

        xa += sin(source.rot) * 0.1
        za += cos(source.rot) * 0.1
        return true
    }
}