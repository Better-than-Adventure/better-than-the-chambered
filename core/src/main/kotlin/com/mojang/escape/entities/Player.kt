package com.mojang.escape.entities

import com.mojang.escape.Art
import com.mojang.escape.GameSession
import com.mojang.escape.Sound
import com.mojang.escape.level.ITriggerable
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.block.IUsableBlock
import com.mojang.escape.level.physics.Point2D
import com.mojang.escape.level.physics.Point3D
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.menu.GotLootMenu
import com.mojang.escape.menu.LoseMenu
import com.sun.org.apache.bcel.internal.generic.IUSHR
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Player(
    private val session: GameSession,
    pos: Point3D,
    rot: Double,
    vel: Point3D,
    rotVel: Double
): Entity(
    pos = pos,
    rot = rot,
    vel = vel,
    rotVel = rotVel,
    flying = false
), ICollidableEntity {
    override val collisionBox = RelativeAABB(0.3)
    
    var bob = 0.0
    var bobPhase = 0.0
    var turnBob = 0.0

    var selectedSlot = 0
    var itemUseTime = 0

    var hurtTime = 0
    var health = 20

    var keys = 0
    var loot = 0
    var ammo = 0
    var potions = 0

    var dead = false

    var deadTime = 0

    var time = 0
    
    var xm = 0
    var zm = 0

    var inLevelChangeBlock = false
    val selectedItem: Item
        get() = items[selectedSlot]

    private var lastBlock: Block? = null

    var items = Array(8) {
        Item.None
    }

    init {
        if (false) {
            items = Item.values()
            health = 999
            ammo = 999
            keys = 999
            potions = 999
        }
    }

    override fun onInit(level: Level) {
        // Do nothing
    }

    override fun onTick(level: Level) {
        var allowMovement = true
        if (dead) {
            allowMovement = false
            deadTime++
            if (deadTime > 60 * 2) {
                session.showMenu(LoseMenu(this))
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
        
        val onBlock = level[(pos.x + 0.5).toInt(), (pos.z + 0.5).toInt()]
        
        if (onBlock is EmptyBlock && allowMovement) {
            var fh = onBlock.getFloorHeight(level, this)
            if (lastBlock != null && onBlock::class != lastBlock!!::class) {
                if (lastBlock is EmptyBlock) {
                    onBlock.onEntityLeave(level, this)
                }
                onBlock.onEntityEnter(level, this)
            }

            lastBlock = onBlock

            if (dead) {
                fh = -0.6
            }
            if (fh > pos.y) {
                pos = pos.copy(y = pos.y + (fh - pos.y) * 0.2)
                vel = vel.copy(y = 0.0)
            } else {
                vel = vel.copy(y = vel.y - 0.1)
                pos = pos.copy(y = pos.y + vel.y)
                if (pos.y < fh) {
                    pos = pos.copy(y = fh)
                    vel = vel.copy(y = 0.0)
                }
            }

            val walkSpeed = 0.03 * onBlock.getWalkSpeed(level, this)

            var dd = (xm * xm + zm * zm).toDouble()
            dd = if (dd > 0.0) {
                sqrt(dd)
            } else {
                1.0
            }
            val xm = this.xm / dd
            val zm = this.zm / dd

            bob *= 0.6
            turnBob *= 0.8
            turnBob += rotVel
            val bobMod = sqrt(xm * xm + zm * zm)
            bob += bobMod
            bobPhase += bobMod * onBlock.getWalkSpeed(level, this)

            if (xm != 0.0 && zm != 0.0) {
                onBlock.onEntityMoveWhileInside(level, this, xm, zm)
            }

            vel = vel.copy(
                x = vel.x - (xm * cos(rot) + zm * sin(rot)) * walkSpeed,
                z = vel.z - (zm * cos(rot) - xm * sin(rot)) * walkSpeed
            )

            val friction = onBlock.getFriction(level, this)

            vel = vel.copy(x = vel.x * friction, z = vel.z * friction)
            rot += rotVel
            rotVel *= 0.4
            println(pos)
            println(vel)
        }
    }

    fun activate(level: Level) {
        if (dead || itemUseTime > 0) {
            return
        }
        val item = selectedItem
        if (item == Item.Pistol) {
            if (ammo > 0) {
                Sound.shoot.play()
                itemUseTime = 10
                level.entities.add(Bullet(
                    Point3D(pos.x, 0.0, pos.z),
                    rot,
                    1.0,
                    Art.sprites,
                    0,
                    0xFFFFFF,
                    this
                ))
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
        
        val possibleHits = arrayListOf<IUsableEntity>()
        for (e in level.entities) {
            if (e == this) {
                continue
            }
            if (e !is IUsableEntity) {
                continue
            }
            if (pos.toPoint2D().distanceToSquared(e.pos.toPoint2D()) < 9.0) {
                possibleHits.add(e)
            }
        }

        val divs = 100
        for (i in 0 until divs) {
            val xx = pos.x + xa * i / divs
            val zz = pos.z + za * i / divs
            for (e in possibleHits) {
                if (e is ICollidableEntity && e.offsetCollisionBox.contains(Point2D(xx, zz))) {
                    e.onUsed(level, this, item)
                    return
                }
            }
            val xt = (xx + 0.5).toInt()
            val zt = (zz + 0.5).toInt()
            val block = level[xt, zt]
            if (block is ICollidableBlock && block.offsetCollisionBox.contains(Point2D(xx, zz))) {
                if (block is IUsableBlock) {
                    block.onUsed(level, this, item)
                    return
                }
            }
        }
    }

    fun addLoot(level: Level, item: Item) {
        if (item == Item.Pistol) {
            ammo += 20
        }
        if (item == Item.Potion) {
            potions += 1
        }
        for (i in items) {
            if (i == item) {
                session.showMenu(GotLootMenu(item))
                return;
            }
        }

        for (i in items.indices) {
            if (items[i] == Item.None) {
                items[i] = item
                selectedSlot = i
                itemUseTime = 0
                session.showMenu(GotLootMenu(item))
                return
            }
        }
    }

    fun hurt(enemy: Entity, dmg: Int) {
        if (hurtTime > 0 || dead) {
            return
        }

        hurtTime = 40
        health -= dmg

        if (health <= 0) {
            health = 0
            Sound.death.play()
            dead = true
        }

        Sound.hurt.play()

        val xd = enemy.pos.x - pos.x
        val zd = enemy.pos.z - pos.z
        val dd = sqrt(xd * xd + zd * zd)
        vel = vel.copy(vel.x - xd / dd * 0.1, vel.z - zd / dd * 0.1)
        rotVel += (random.nextDouble() - 0.5) * 0.2
    }

    override fun onCollideWithEntity(level: Level, other: Entity) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }
}