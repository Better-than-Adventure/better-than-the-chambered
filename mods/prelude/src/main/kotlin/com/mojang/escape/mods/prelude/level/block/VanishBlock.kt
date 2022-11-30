package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.entities.Entity
import com.mojang.escape.entities.Item
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.mods.prelude.gui.RubbleSprite
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.block.IUsableBlock
import com.mojang.escape.level.block.WallBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.mods.prelude.ModSound

class VanishBlock(
    pos: Point2I,
    wallArt: Bitmap,
    wallTex: Int,
    wallCol: Int,
    val floorArt: Bitmap,
    val floorTex: Int,
    val floorCol: Int,
    val ceilArt: Bitmap,
    val ceilTex: Int,
    val ceilCol: Int
): WallBlock(
    pos = pos,
    art = wallArt,
    tex = wallTex,
    col = wallCol
), IUsableBlock {
    override fun onUsed(level: Level, source: Entity, item: Item) {
        ModSound.crumble.play()
        
        for (i in 0 until 31) {
            val sprite = RubbleSprite()
            sprite.col = this.col
            sprite.x = pos.x + 0.5
            sprite.y = 0.5
            sprite.z = pos.z + 0.5
            level.sprites.add(sprite)
        }
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