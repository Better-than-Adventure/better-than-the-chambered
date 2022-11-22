package com.mojang.escape.level

import com.mojang.escape.*
import com.mojang.escape.entities.*
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.block.*
import com.mojang.escape.level.provider.ILevelProvider
import com.mojang.escape.menu.GotLootMenu
import kotlin.math.floor

abstract class Level(
    val name: StringUnit,
    val width: Int,
    val height: Int,
    val xSpawn: Int,
    val ySpawn: Int,
    val blocks: Array<Block>,
    val entities: MutableList<Entity>
) {
    val sprites = mutableListOf<Sprite>()

    /**
     * Returns the block at the coordinates [x] and [y], or throws an [ArrayIndexOutOfBoundsException] if [x] and [y] are out of range.
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    operator fun get(x: Int, y: Int): Block {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw ArrayIndexOutOfBoundsException("Block coordinate ($x, $y) was out of bounds! width = $width, height = $height")
        }
        
        return blocks[x + y * width]
    }

    /**
     * Sets the block at the coordinates [x] and [y] to [value], or throws an [ArrayIndexOutOfBoundsException] if [x] and [y] are out of range.
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    operator fun set(x: Int, y: Int, value: Block) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw ArrayIndexOutOfBoundsException("Block coordinate ($x, $y) was out of bounds! width = $width, height = $height")
        }
        
        blocks[x + y * width] = value
    }

    fun containsBlockingEntity(x0: Double, y0: Double, x1: Double, y1: Double): Boolean =
        entities.any { entity -> entity.isInside(x0, y0, x1, y1) }

    fun containsBlockingNonFlyingEntity(x0: Double, y0: Double, x1: Double, y1: Double): Boolean =
        entities.any { entity -> !entity.flying && entity.isInside(x0, y0, x1, y1) }

    fun tick() {
        var i = 0
        while (i < entities.size) {
            val e = entities[i]
            e.tick(this)
            if (e.removed) {
                entities.removeAt(i--)
            }
            i++
        }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val block = this[x, y]
                if (block is ITickableBlock) {
                    block.tick(this)
                }
            }
        }
        
        postTick()
    }
    
    protected abstract fun postTick()

    fun trigger(source: Entity?, triggerId: Int, state: Int) {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val block = this[x, y]
                if (block is ITriggerable && block.triggerId == triggerId) {
                    block.onTrigger(this, source, state)
                }
            }
        }
    }
}