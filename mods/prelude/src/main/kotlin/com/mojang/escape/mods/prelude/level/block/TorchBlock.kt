package com.mojang.escape.mods.prelude.level.block

import com.mojang.escape.Art
import com.mojang.escape.col
import com.mojang.escape.gui.BasicSprite
import com.mojang.escape.gui.Bitmap
import com.mojang.escape.gui.Bitmap3D
import com.mojang.escape.level.Level
import com.mojang.escape.level.block.Block
import com.mojang.escape.level.block.ITickableBlock
import com.mojang.escape.level.physics.Point2I
import com.mojang.escape.mods.prelude.ModArt
import java.util.*

class TorchBlock(
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
    spriteTex = 8 * 0 + 3,
    spriteCol = 0xFFFF00.col
), ITickableBlock {
    private var torchSprite = BasicSprite(0.0, 0.0, 0.0, 3, Art.getCol(0xFFFF00), ModArt.sprites)

    override fun onInit(level: Level) {
        val random = Random(((pos.x + pos.z * 1000) * 341871231).toLong())
        val r = 0.4
        for (i in 0 until 1000) {
            val face = random.nextInt(4)
            if (face == 0 && level[pos.x - 1, pos.z + 0]?.occludesAdjacentBlocks == true) {
                sprite.x -= r
            }
            if (face == 1 && level[pos.x + 0, pos.z - 1]?.occludesAdjacentBlocks == true) {
                sprite.z -= r
            }
            if (face == 2 && level[pos.x + 1, pos.z + 0]?.occludesAdjacentBlocks == true) {
                sprite.x += r
            }
            if (face == 3 && level[pos.x + 0, pos.z + 1]?.occludesAdjacentBlocks == true) {
                sprite.z += r
            }
        }
    }

    override fun onTick(level: Level) {
        if (random.nextInt(4) == 0) {
            torchSprite.tex = 3 + random.nextInt(2)
        }
    }
}