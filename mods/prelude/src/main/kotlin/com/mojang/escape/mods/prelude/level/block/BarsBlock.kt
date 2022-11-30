package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Sound
import com.mojang.escape.col
import com.mojang.escape.entities.Bullet
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Sprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.block.IUsableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt
import com.mojang.escape.mods.prelude.ModSound

class BarsBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int
): SpriteEmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol,
    spriteArt = ModArt.sprites,
    spriteTex = 8 * 0 + 0,
    spriteCol = 0x202020
), ICollidableBlock, IUsableBlock {
    override val collisionBox = RelativeAABB(0.5)

    override fun onUsed(level: Level, source: Entity, item: Item) {
        if (item == Item.Cutters) {
            ModSound.cut.play()
            level[pos.x, pos.z] = SpriteEmptyBlock(pos, floorArt, floorTex, floorCol, ceilArt, ceilTex, ceilCol, sprite.art, 8 * 0 + 1, sprite.col)
        }
    }

    override fun onEntityCollision(level: Level, entity: Entity) {
        // Do nothing
    }

    override fun blocksEntity(level: Level, entity: Entity): Boolean {
        return true
    }
}