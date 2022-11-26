package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.col
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.block.ITickableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class WaterBlock(
    pos: Point2I,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): EmptyBlock(
    pos = pos,
    floorArt = ModArt.floors,
    floorTex = 8 * 1 + 0,
    floorCol = 0x0000FF.col,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
), ICollidableBlock, ITickableBlock {
    override val collisionBox = RelativeAABB(0.5)
    private var steps = 0

    override fun onTick(level: Level) {
        steps--
        if (steps <= 0) {
            floorTex = 8 * 1 + random.nextInt(3)
            floorCol = 0x0000FF.col
            steps = 16
        }
    }

    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }

    override fun onEntityEnter(level: Level, entity: Entity) {
        ModSound.splash.play()
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        if (entity is Player && entity.selectedItem == Item.Flippers) {
            return false
        }
        if (entity is Bullet) {
            return false
        }
        return true
    }

    override fun getFloorHeight(level: Level, entity: Entity): Double {
        return -0.5
    }

    override fun getWalkSpeed(level: Level, entity: Entity): Double {
        return 0.4
    }
}