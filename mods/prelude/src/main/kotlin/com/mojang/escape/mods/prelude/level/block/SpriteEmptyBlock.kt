package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.EmptyBlock
import com.mojang.escape.level.physics.Point2I

open class SpriteEmptyBlock(
    pos: Point2I,
    floorArt: Bitmap,
    floorTex: Int,
    floorCol: Int,
    ceilArt: Bitmap,
    ceilTex: Int,
    ceilCol: Int,
    spriteArt: Bitmap,
    spriteTex: Int,
    spriteCol: Int
): EmptyBlock(
    pos = pos,
    floorArt = floorArt,
    floorTex = floorTex,
    floorCol = floorCol,
    ceilArt = ceilArt,
    ceilTex = ceilTex,
    ceilCol = ceilCol
) {
    protected var sprite = BasicSprite(0.0, 0.0, 0.0, spriteTex, spriteCol, spriteArt)

    override fun doRender(level: Level, bitmap: Bitmap3D) {
        bitmap.renderSprite(pos.x.toDouble(), 0.0, pos.z.toDouble(), sprite.tex, sprite.col, sprite.art)
    }
}