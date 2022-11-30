package com.mojang.escape.level

import com.mojang.escape.*
import com.mojang.escape.entities.*
import com.mojang.escape.gui.Sprite
import com.mojang.escape.lang.StringUnit
import com.mojang.escape.level.block.*
import com.mojang.escape.level.physics.AABB
import com.mojang.escape.level.physics.PhysicsManager
import com.mojang.escape.level.physics.Point2I

abstract class Level(
    val session: GameSession,
    val name: StringUnit,
    val lengthX: Int,
    val lengthZ: Int,
    val spawn: Point2I,
    val blocks: Array<Block>,
    val entities: MutableList<Entity>
) {
    val sprites = mutableListOf<Sprite>()
    val physics = PhysicsManager()
    
    /**
     * Returns the block at the coordinates [x] and [z], or null if [x] and [z] are out of range.
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    operator fun get(x: Int, z: Int): Block? {
        if (x < 0 || z < 0 || x >= lengthX || z >= lengthZ) {
            return null
        }
        
        return blocks[x + z * lengthX]
    }
    
    /**
     * Returns the block at [point], or null if [point] is out of range.
     */
    operator fun get(point: Point2I) = 
        get(point.x, point.z)

    /**
     * Sets the block at the coordinates [x] and [z] to [value], if those coordinates are within range.
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    operator fun set(x: Int, z: Int, value: Block) {
        if (x < 0 || z < 0 || x >= lengthX || z >= lengthZ) {
            return
        }
        
        blocks[x + z * lengthX].onDeinit(this)
        blocks[x + z * lengthX] = value
    }

    /**
     * Sets the block at [point] to [value], if those coordinates are within range.
     */
    operator fun set(point: Point2I, value: Block) = 
        set(point.x, point.z, value)
    
    fun onInit() {
        for (block in blocks) {
            block.onInit(this)
        }
        
        for (entity in entities) {
            entity.onInit(this)
        }
    }

    fun onTick() {
        var i = 0
        while (i < entities.size) {
            val e = entities[i]
            e.onTick(this)
            if (e.removed) {
                entities.removeAt(i--)
            }
            i++
        }

        for (z in 0 until lengthZ) {
            for (x in 0 until lengthX) {
                val block = this[x, z]
                if (block is ITickableBlock) {
                    block.onTick(this)
                }
            }
        }
        
        i = 0
        while (i < sprites.size) {
            val s = sprites[i]
            s.tick()
            if (s.removed) {
                sprites.removeAt(i--)
            }
            i++
        }
    }

    fun trigger(source: Entity?, triggerId: Int, state: Int) {
        for (z in 0 until lengthZ) {
            for (x in 0 until lengthX) {
                val block = this[x, z]
                if (block is ITriggerable && block.triggerId == triggerId) {
                    block.onTrigger(this, source, state)
                }
            }
        }
        for (entity in entities) {
            if (entity is ITriggerable && entity.triggerId == triggerId) {
                entity.onTrigger(this, source, state)
            }
        }
    }
    
    fun entitiesInAABB(box: AABB) = entities.filter { entity ->
        entity is ICollidableEntity && box.intersects(entity.offsetCollisionBox)
    }
    
    fun switchLevel(source: ILevelChangeBlock) {
        session.switchLevel(source.targetLevel, source.levelChangeIdOut)
    }
}