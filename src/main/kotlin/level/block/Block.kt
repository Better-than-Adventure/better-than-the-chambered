package com.mojang.escape.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Sprite
import com.mojang.escape.lang.IStringUnit
import com.mojang.escape.level.Level
import java.util.*
import kotlin.collections.ArrayList

open class Block {
    protected val random = Random()

    protected var blocksMotion = false
    var solidRender = false

    val messages: Array<IStringUnit>? = null

    val sprites = ArrayList<Sprite>()
    val entities = ArrayList<Entity>()

    var tex = -1
    var col = -1

    var floorCol = -1
    var ceilCol = -1

    var floorTex = -1
    var ceilTex = -1

    var level: Level? = null
    var x = 0
    var y = 0

    var id = 0

    fun addSprite(sprite: Sprite) {
        sprites.add(sprite)
    }

    open fun use(level: Level, item: Item): Boolean {
        return false
    }

    open fun tick() {
        val spritesToRemove = hashSetOf<Sprite>()
        for (sprite in sprites) {
            sprite.tick()
            if (sprite.removed) {
                spritesToRemove.add(sprite);
            }
        }
        sprites.removeAll(spritesToRemove)
    }

    open fun removeEntity(entity: Entity) {
        entities.remove(entity)
    }

    open fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    open fun blocks(entity: Entity): Boolean {
        return blocksMotion
    }

    open fun decorate(level: Level, x: Int, y: Int) {
    }

    open fun getFloorHeight(e: Entity): Double {
        return 0.0
    }

    open fun getWalkSpeed(player: Player): Double {
        return 1.0
    }

    open fun getFriction(player: Player): Double {
        return 0.6
    }

    open fun trigger(pressed: Boolean) {
    }

}