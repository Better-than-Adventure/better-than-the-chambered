package com.mojang.escape.level.physics

import com.mojang.escape.entities.Entity
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.ICollidableBlock
import kotlin.math.floor

object PhysicsManager {
    private fun canEntityMoveTo(level: Level, entity: Entity, x: Double, y: Double) {
        val x0 = floor(x + 0.5 - entity.rot).toInt()
        val x1 = floor(x + 0.5 + entity.rot).toInt()
        val y0 = floor(y + 0.5 - entity.rot).toInt()
        val y1 = floor(y + 0.5 + entity.rot).toInt()
        
        for (block in arrayOf(level[x0, y0], level[x1, y0], level[x0, y1], level[x1, y1])) {
            if (block is ICollidableBlock) {
                if (block.collisionBox.)
            }
        }
        
        val xc = floor(x + 0.5).toInt()
        val zc = floor(y + 0.5).toInt()
        val rr = 2
        for (zb in (zc - rr)..(zc + rr)) {
            for (xb in (xc - rr)..(xc + rr)) {
                val entities = level.entities
                for (blockEntity in entities) {
                    if (blockEntity == entity) {
                        continue
                    }
                    
                    if (!blockEntity.blocks(level, entity, entity.pos.x, entity.pos.y, entity.rot) && 
                        blockEntity.blocks(level, blockEntity, x,y, entity.rot)) {
                        entity.onCollideWithEntity(level, blockEntity)
                    }
                }
            }
        }
    }
    
    private fun doesEntityIntersect()
}