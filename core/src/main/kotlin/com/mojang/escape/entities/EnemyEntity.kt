package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.PoofSprite
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class EnemyEntity(x: Double, z: Double, protected val defaultTex: Int, protected val defaultColor: Int, art: Bitmap): Entity(art) {
    protected val sprite: Sprite
    protected var hurtTime = 0
    protected var animTime = 0
    protected var health = 3
    protected var spinSpeed = 0.1
    protected var runSpeed = 1.0

    init {
        this.x = x
        this.z = z
        this.sprite = BasicSprite(0.0, 0.0, 0.0, 4 * 8, defaultColor, art)
        this.sprites.add(this.sprite)
        this.r = 0.3
    }

    override fun tick(level: Level) {
        if (hurtTime > 0) {
            hurtTime--
            if (hurtTime == 0) {
                this.sprite.col = defaultColor
            }
        }
        this.animTime++
        this.sprite.tex = defaultTex + animTime / 10 % 2
        this.move()
        if (xa == 0.0 || za == 0.0) {
            this.rota += (random.nextGaussian() * random.nextDouble())
        }

        this.rota += (random.nextGaussian() * random.nextDouble()) * spinSpeed
        rot += rota
        rota *= 0.8
        xa *= 0.8
        za *= 0.8
        xa += sin(rot) * 0.004 * runSpeed
        za += cos(rot) * 0.004 * runSpeed
    }

    override fun use(source: Entity, item: Item): Boolean {
        if (hurtTime > 0 || item != Item.PowerGlove) {
            return false
        }
        
        hurt(sin(source.rot), cos(source.rot))

        return true
    }
    
    protected open fun hurt(xd: Double, zd: Double) {
        sprite.col = Art.getCol(0xFF0000)
        hurtTime = 15

        val dd = sqrt(xd * xd + zd * zd)
        xa += xd / dd * 0.2
        za += zd / dd * 0.2
        Sound.hurt2.play()
        health--
        if (health <= 0) {
            val xt = (x + 0.5).toInt()
            val zt = (z + 0.5).toInt()
            level?.getBlock(xt, zt)?.addSprite(PoofSprite(x - xt, 0.0, z - zt))
            this.die()
            this.remove()
            Sound.kill.play()
        }
    }

    protected open fun die() {
    }

    override fun collide(level: Level, entity: Entity) {
        if (entity is Bullet) {
            if (entity.owner is EnemyEntity) {
                return
            }
            if (hurtTime > 0) {
                return
            }
            entity.remove()
            this.hurt(entity.xa, entity.za)
        }
        if (entity is Player) {
            entity.hurt(this, 1)
        }
    }
}