package com.mojang.escape.level.block

import com.mojang.escape.Art
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.gui.Sprite
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.Level
import java.util.*
import kotlin.collections.ArrayList

open class Block(val wallArt: Bitmap = Art.missing, val floorArt: Bitmap) {
    protected val random = Random()

    protected var blocksMotion = false
    var solidRender = false

    val messages: Array<StringUnit>? = null

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
    
    open fun onPlayerEnter(player: Player) {
        // Do nothing
    }
    
    open fun onPlayerWalk(player: Player, xm: Double, zm: Double): Boolean {
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

    open fun decorate(blocks: Array<Block>, blocksWidth: Int, blocksHeight: Int, x: Int, y: Int) {
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
    
    open fun doRender(bitmap: Bitmap3D, x: Int, z: Int, center: Block, east: Block, south: Block): Boolean {
        return false
    }

}