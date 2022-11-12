package com.mojang.escape.entities

import com.mojang.escape.Sound
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.IceBlock
import com.mojang.escape.level.block.WaterBlock
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.time.Duration.Companion.minutes

class Player: Entity() {
    var bob = 0.0
    var bobPhase = 0.0
    var turnBob = 0.0

    var selectedSlot = 0
    var itemUseTime = 0

    var y = 0.0
    var ya = 0.0

    var hurtTime = 0
    var health = 0

    var keys = 0
    var loot = 0
    var ammo = 0
    var potions = 0

    var dead = false

    var deadTime = 0

    var time = 0

    val selectedItem: Item
    get() = items[selectedSlot]

    private var sliding = false

    private var lastBlock: Block? = null

    private var items = arrayOf(Item.None, Item.None, Item.None, Item.None, Item.None, Item.None, Item.None, Item.None)

    init {
        this.r = 0.3
    }

    fun tick(up: Boolean, down: Boolean, left: Boolean, right: Boolean, turnLeft: Boolean, turnRight: Boolean) {
        var allowMovement = true
        if (dead) {
            allowMovement = false
            deadTime++
            if (deadTime > 60 * 2) {
                level.lose()
            }
        } else {
            time++
        }
        if (itemUseTime > 0) {
            itemUseTime--
        }
        if (hurtTime > 0) {
            hurtTime--
        }

        val onBlock = level.getBlock((x + 0.5).toInt(), (z + 0.5).toInt())

        var fh = onBlock.getFloorHeight(this)
        if (onBlock is WaterBlock && lastBlock !is WaterBlock) {
            Sound.splash.play()
        }

        lastBlock = onBlock

        if (dead) {
            fh = -0.6
        }
        if (fh > y) {
            y += (fh - y) * 0.2
            ya = 0.0
        } else {
            ya -= 0.1
            y += ya
            if (y < fh) {
                y = fh
                ya = 0.0
            }
        }

        val rotSpeed = 0.05
        val walkSpeed = 0.03 * onBlock.getWalkSpeed(this)

        if (turnLeft && allowMovement) {
            rota += rotSpeed
        }
        if (turnRight && allowMovement) {
            rota -= rotSpeed
        }

        var xm = 0.0
        var zm = 0.0
        if (up && allowMovement) {
            zm--
        }
        if (down && allowMovement) {
            zm++
        }
        if (left && allowMovement) {
            xm--
        }
        if (right && allowMovement) {
            xm++
        }
        var dd = xm * xm + zm * zm
        dd = if (dd > 0.0) {
            sqrt(dd)
        } else {
            1.0
        }
        xm /= dd
        zm /= dd

        bob *= 0.6
        turnBob *= 0.8
        turnBob += rota
        val bobMod = sqrt(xm * xm + zm * zm)
        bob += bobMod
        bobPhase += bobMod * onBlock.getWalkSpeed(this)
        val wasSliding = sliding
        sliding = false

        if (onBlock is IceBlock && selectedItem != Item.Skates) {
            if (xa * xa > za * za) {
                sliding = true
                za = 0.0
                xa = if (xa > 0.0) {
                    0.08
                } else {
                    -0.08
                }
                z += ((z + 0.5).toInt() - z) * 0.2
            } else if (xa * xa < za * za) {
                sliding = true
                xa = 0.0
                za = if (za > 0) {
                    0.08
                } else {
                    -0.08
                }
                x += ((x + 0.5).toInt() - x) * 0.2
            } else {
                xa -= (xm * cos(rot) + zm * sin(rot)) * 0.1
                za -= (zm * cos(rot) - xm * sin(rot)) * 0.1
            }

            if (!wasSliding && sliding) {
                Sound.slide.play()
            }
        } else {
            xa -= (xm * cos(rot) + zm * sin(rot)) * walkSpeed
            za -= (zm * cos(rot) - xm * sin(rot)) * walkSpeed
        }

        this.move()

        val friction = onBlock.getFriction(this)
        xa *= friction
        za *= friction
        rot += rota
        rota *= 0.4
    }

    fun activate() {
        if (dead || itemUseTime > 0) {
            return
        }
        val item = selectedItem
        if (item == Item.Pistol) {
            if (ammo > 0) {
                Sound.shoot.play()
                itemUseTime = 10
                level.addEntity(Bullet(this, x, z, rot, 1.0, 0, 0xFFFFFF))
                ammo--
            }
            return
        }
        if (item == Item.Potion) {
            if (potions > 0 && health < 20) {
                Sound.potion.play()
                itemUseTime = 20
                health += (5 + random.nextInt(6))
                if (health > 20) {
                    health = 20
                }
                potions--
            }
            return
        }
        if (item == Item.Key) itemUseTime = 10
        if (item == Item.PowerGlove) itemUseTime = 10
        if (item == Item.Cutters) itemUseTime = 10

        val xa = (2 * sin(rot))
        val za = (2 * cos(rot))

        val rr = 3
        val xc = (x + 0.5).toInt()
        val zc = (z + 0.5).toInt()
        var possibleHits = arrayListOf<Entity>()
        for (z in (zc - rr)..(zc + rr)) {
            for (x in (xc - rr)..(xc + rr)) {
                val es = level.getBlock(x, z).entities
                for (e in es) {
                    if (e == this) {
                        continue
                    }
                    possibleHits.add(e)
                }
            }
        }

        val divs = 100
        for (i in 0 until divs) {
            val xx = x + xa * i / divs
            val zz = z + za * i / divs
            for (e in possibleHits) {
                if (e.contains(xx, zz)) {
                    if (e.use(this, item)) {
                        return
                    }
                }
            }
            val xt = (xx + 0.5).toInt()
            val zt = (zz + 0.5).toInt()
            if (xt != (x + 0.5).toInt() || zt != (z + 0.5).toInt()) {
                var block = level.getBlock(xt, zt)
                if (block.use(level, item)) {
                    return
                }
                if (block.blocks(this)) {
                    return
                }
            }
        }
    }

    override fun blocks(entity: Entity?, x2: Double, z2: Double, r2: Double): Boolean {
        return super.blocks(entity, x2, z2, r2)
    }

    fun addLoot(item: Item) {

    }

    fun hurt(entity: Entity, dmg: Int) {

    }

    override fun collide(entity: Entity?) {
        super.collide(entity)
    }

    fun win() {
        level.win()
    }
}