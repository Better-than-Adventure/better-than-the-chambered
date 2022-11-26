package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.Sound
import com.mojang.escape.col
import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Player
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.ICollidableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.level.physics.RelativeAABB
import com.mojang.escape.mods.prelude.ModArt

class LootBlock(
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
    spriteTex = 8 * 2 + 2,
    spriteCol = 0xFFFF80.col
) {
    override fun onEntityEnter(level: Level, entity: Entity) {
        if (entity is Player) {
            entity.loot++
            Sound.pickup.play()
            level[pos] = EmptyBlock(
                pos = pos,
                floorArt = floorArt,
                floorTex = floorTex,
                floorCol = floorCol,
                ceilArt = ceilArt,
                ceilTex = ceilTex,
                ceilCol = ceilCol
            )
        }
    }
}