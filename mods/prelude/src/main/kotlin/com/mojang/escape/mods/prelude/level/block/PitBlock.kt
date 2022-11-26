package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.mods.prelude.entities.BoulderEntity
import com.mojang.escape.entities.Entity
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class PitBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): EmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = 8 * 0 + 1,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
), ICollidableBlock {
    override val collisionBox = RelativeAABB(0.5)
    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }

    override fun onEntityEnter(level: Level, entity: Entity) {
        if (entity is BoulderEntity) {
            entity.removed = true
            ModSound.thud.play()
            level[pos] = SpriteEmptyBlock(
                pos = pos,
                floorArt = floorArt,
                floorTex = floorTex,
                floorCol = floorCol,
                ceilArt = ceilArt,
                ceilTex = ceilTex,
                ceilCol = ceilCol,
                spriteArt = ModArt.sprites,
                spriteTex = 8 * 1 + 2,
                spriteCol = BoulderEntity.COLOR
            )
        }
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return entity !is BoulderEntity
    }
}